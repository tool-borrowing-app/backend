package com.toolborrow.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api-key}")
    private String stripApikey;

    @Value("${frontend.url}/success")
    private String successUrl;

    @Value("${frontend.url}/kolcsonzes")
    private String cancelUrl;

    public Session createCheckoutSession() throws StripeException {
        Stripe.apiKey = stripApikey;

        long amountHuf = 1000L * 100; // fixed test amount, TODO: get this from params

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
}
