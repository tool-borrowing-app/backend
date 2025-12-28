package com.toolborrow.backend.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface PaymentService {

    public Session createCheckoutSession() throws StripeException;
}
