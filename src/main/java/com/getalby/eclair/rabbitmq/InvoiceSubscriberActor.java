package com.getalby.eclair.rabbitmq;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.acinq.eclair.payment.PaymentSent;

public class InvoiceSubscriberActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(InvoiceSubscriberActor.class, InvoiceSubscriberActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(message -> {
                    logger.info("FROM PLUGIN: {}", message.getClass());
                })
                .build();
    }

    @Override
    public void preStart() {
        getContext()
                .getSystem()
                .eventStream()
                .subscribe(getSelf(), PaymentSent.class);
    }

    @Override
    public void postStop() {
        getContext()
                .getSystem()
                .eventStream()
                .unsubscribe(getSelf());
    }
}
