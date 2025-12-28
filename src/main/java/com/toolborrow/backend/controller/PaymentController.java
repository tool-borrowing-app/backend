package com.toolborrow.backend.controller;

import com.stripe.exception.StripeException;
import com.toolborrow.backend.service.PaymentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final @NonNull PaymentService paymentService;

    @PostMapping
    public String createCheckoutSession() throws StripeException {
        return paymentService.createCheckoutSession().getUrl();
    }
}
