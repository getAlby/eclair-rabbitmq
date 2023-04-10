package com.getalby.eclair.rabbitmq;

import fr.acinq.eclair.payment.PaymentReceived;

public final class Invoice {

    private final String r_hash;
    private final long amt_paid_sat;
    private final boolean settled;
    private final boolean is_key_send;
    private final int state;
    private final long settle_date;

    public Invoice(final PaymentReceived payment) {
        r_hash = payment.paymentHash().bytes().toBase64();
        amt_paid_sat = payment.amount().toLong() / 1000L;
        settled = true;
        is_key_send = false;
        state = 1;
        settle_date = payment.timestamp().toLong();
    }
}
