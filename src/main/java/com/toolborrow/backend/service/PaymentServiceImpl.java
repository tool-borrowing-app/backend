package com.toolborrow.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.toolborrow.backend.exception.TBAException;
import com.toolborrow.backend.model.dto.CreateCheckoutSessionDto;
import com.toolborrow.backend.model.dto.CreateReservationDto;
import com.toolborrow.backend.model.entity.Payment;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.model.entity.enums.PaymentStatus;
import com.toolborrow.backend.repository.*;
import com.toolborrow.backend.utils.JwtUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Validated
public class PaymentServiceImpl implements PaymentService {

    final public UserRepository userRepository;
    final public ToolRepository toolRepository;
    final public ReservationRepository reservationRepository;
    final public PaymentRepository paymentRepository;

    final public ReservationService reservationService;

    @Value("${reservation.max-per-user-per-tool}")
    private int maxReservationsPerUserPerTool;

    @Value("${stripe.api-key}")
    private String stripApikey;

    @Value("${frontend.url}/kolcsonzeseim")
    private String successUrl;

    @Value("${frontend.url}")
    private String cancelUrl;

    public Session createCheckoutSession(CreateCheckoutSessionDto dto) throws StripeException {
        Stripe.apiKey = stripApikey;
        verifyIfCheckoutSessionParametersIsValid(dto);

        User borrower = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());
        Tool tool = toolRepository.findById(dto.getToolId())
                .orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found with id " + dto.getToolId()));

        Payment payment = Payment.builder()
                .tool(tool)
                .borrower(borrower)
                .dateFrom(dto.getDateFrom())
                .dateTo(dto.getDateTo())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        long amountHuf = calculateAmountHuf(dto) * 100L;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(String.valueOf(payment.getId()))
                .putMetadata("paymentId", String.valueOf(payment.getId()))
                .putMetadata("toolId", String.valueOf(dto.getToolId()))
                .putMetadata("borrowerUserId", String.valueOf(borrower.getId()))
                .putMetadata("dateFrom", dto.getDateFrom().toString())
                .putMetadata("dateTo", dto.getDateTo().toString())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("huf")
                                                .setUnitAmount(amountHuf)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Tool Reservation")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        payment.setCheckoutSessionId(session.getId());
        paymentRepository.save(payment);

        return session;
    }


    public String stripeWebhook(Event event) {
        EventDataObjectDeserializer deser = event.getDataObjectDeserializer();
        StripeObject stripeObject = deser.getObject().orElse(null);
        if (stripeObject == null) {
            System.out.println("Deserialization failed");
            return "";
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> {
                Session session = (Session) stripeObject;

                String paymentIdStr = session.getClientReferenceId();
                if (paymentIdStr == null) {
                    paymentIdStr = session.getMetadata().get("paymentId");
                }
                if (paymentIdStr == null) {
                    System.out.println("Missing paymentId/clientReferenceId on session " + session.getId());
                    return "";
                }

                long paymentId = Long.parseLong(paymentIdStr);

                Payment payment = paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new TBAException(NOT_FOUND, "Payment not found: " + paymentId));

                // idempotency
                if (payment.getStatus() == PaymentStatus.FINISHED) return "";

                payment.setPaymentIntentId(session.getPaymentIntent());
                payment.setStatus(PaymentStatus.FINISHED);
                paymentRepository.save(payment);

                // create reservation from metadata
                var metadata = session.getMetadata();

                CreateReservationDto createReservationDto = CreateReservationDto.builder()
                        .toolId(Long.parseLong(metadata.get("toolId")))
                        .borrowerUserId(Long.parseLong(metadata.get("borrowerUserId")))
                        .dateFrom(LocalDate.parse(metadata.get("dateFrom")))
                        .dateTo(LocalDate.parse(metadata.get("dateTo")))
                        .build();

                reservationService.createReservation(createReservationDto);
            }
            default -> System.out.println("Unhandled event type: " + event.getType());
        }
        return "";
    }

    // this logic is copied from the reservation service, maybe refactor this later?
    public void verifyIfCheckoutSessionParametersIsValid(CreateCheckoutSessionDto createCheckoutSessionDto) throws TBAException {
        final @NonNull User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        final @NonNull Tool tool = toolRepository.findById(createCheckoutSessionDto.getToolId()).orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found with id " + createCheckoutSessionDto.getToolId()));

        if (user.equals(tool.getUser())) {
            throw new TBAException(BAD_REQUEST, "User cannot reserve their own tool");
        }

        // ---- limit ellenőrzés ----
        final long existingReservations = reservationRepository.countActiveReservationsByUserAndTool(user.getId(), tool.getId());
        if (existingReservations >= maxReservationsPerUserPerTool) {
            throw new TBAException(BAD_REQUEST, "User cannot have more than " + maxReservationsPerUserPerTool + " active reservations for this tool!");
        }

        final @NonNull LocalDate from = createCheckoutSessionDto.getDateFrom();
        final @NonNull LocalDate to = createCheckoutSessionDto.getDateTo();
        final @NonNull LocalDate today = LocalDate.now();

        if (from.isBefore(today)) {
            throw new TBAException(BAD_REQUEST, "Reservation date cannot be in the past");
        }

        if (from.isAfter(to)) {
            throw new TBAException(BAD_REQUEST, "Invalid date range: dateFrom must be before or equal to dateTo");
        }

        final boolean overlapExists = reservationRepository.existsOverlappingActiveReservation(tool.getId(), from, to);

        if (overlapExists) {
            throw new TBAException(BAD_REQUEST, "Tool already reserved in the requested date range");
        }
    }

    public int calculateAmountHuf(CreateCheckoutSessionDto createCheckoutSessionDto) {
        Tool tool = toolRepository.findById(createCheckoutSessionDto.getToolId()).orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found with id: " + createCheckoutSessionDto.getToolId()));

        long days = ChronoUnit.DAYS.between(createCheckoutSessionDto.getDateFrom(), createCheckoutSessionDto.getDateTo()) + 1;

        return (int) (tool.getRentalPrice() * days + tool.getDepositPrice());
    }
}
