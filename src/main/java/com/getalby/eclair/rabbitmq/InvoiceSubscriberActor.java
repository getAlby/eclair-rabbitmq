package com.getalby.eclair.rabbitmq;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import fr.acinq.eclair.payment.PaymentReceived;
import fr.acinq.eclair.payment.PaymentSent;

public class InvoiceSubscriberActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private Channel channel;

    private InvoiceSubscriberActor(Channel channel) {
        this.channel = channel;
    }

    public static Props props(final Channel channel) {
        return Props.create(InvoiceSubscriberActor.class, channel);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(event -> {
                    final Invoice invoice = new Invoice((PaymentReceived) event);
                    final String payload = gson.toJson(invoice);

                    try {
                        this.channel.basicPublish(
                                "lnd_invoice",
                                "invoice.incoming.settled",
                                new AMQP.BasicProperties.Builder()
                                        .contentType("application/json")
                                        .build(),
                                payload.getBytes()
                        );
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                })
                .build();
    }

    @Override
    public void preStart() {
        getContext()
                .getSystem()
                .eventStream()
                .subscribe(getSelf(), PaymentReceived.class);
    }

    @Override
    public void postStop() {
        getContext()
                .getSystem()
                .eventStream()
                .unsubscribe(getSelf());
    }
}
