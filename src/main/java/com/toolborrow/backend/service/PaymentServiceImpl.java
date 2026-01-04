package com.toolborrow.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.toolborrow.backend.exception.TBAException;
import com.toolborrow.backend.model.dto.CreateCheckoutSessionDto;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ReservationRepository;
import com.toolborrow.backend.repository.ToolRepository;
import com.toolborrow.backend.repository.UserRepository;
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
    final public LookupRepository lookupRepository;

    @Value("${reservation.max-per-user-per-tool}")
    private int maxReservationsPerUserPerTool;

    @Value("${stripe.api-key}")
    private String stripApikey;

    @Value("${frontend.url}/success")
    private String successUrl;

    @Value("${frontend.url}/kolcsonzes")
    private String cancelUrl;

    public Session createCheckoutSession(CreateCheckoutSessionDto createCheckoutSessionDto) throws StripeException, TBAException {
        Stripe.apiKey = stripApikey;

        verifyIfCheckoutSessionParametersIsValid(createCheckoutSessionDto);

        long amountHuf = calculateAmountHuf(createCheckoutSessionDto) * 100;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("huf")
                                                .setUnitAmount(amountHuf)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Tool Reservation Test")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return Session.create(params);
    }

    public String stripeWebhook(Event event) {

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                System.out.println("event: checkout.session.completed");
                break;
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
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
