package com.toolborrow.backend.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.toolborrow.backend.model.dto.CreateCheckoutSessionDto;

public interface PaymentService {

    public Session createCheckoutSession(CreateCheckoutSessionDto createCheckoutSessionDto) throws StripeException;

    public String stripeWebhook(Event event);
}
