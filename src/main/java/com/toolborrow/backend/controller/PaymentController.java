package com.toolborrow.backend.controller;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.ApiResource;
import com.toolborrow.backend.model.dto.CreateCheckoutSessionDto;
import com.toolborrow.backend.service.PaymentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final @NonNull PaymentService paymentService;

    @PostMapping
    public String createCheckoutSession(final @RequestBody @NonNull CreateCheckoutSessionDto createCheckoutSessionDto) throws StripeException {
        return paymentService.createCheckoutSession(createCheckoutSessionDto).getUrl();
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> stripeWebhook(@RequestBody String payload) {
        Event event;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(400).body("");
        }

        System.out.println("event received in stripe webhook!");

        return ResponseEntity.ok(paymentService.stripeWebhook(event));
    }
}
