package com.getalby.eclair.rabbitmq;

import fr.acinq.eclair.Kit;
import fr.acinq.eclair.Plugin;
import fr.acinq.eclair.PluginParams;
import fr.acinq.eclair.Setup;

public class RabbitMQPlugin implements Plugin {

    @Override
    public PluginParams params() {
        return () -> "RabbitMQPlugin";
    }

    @Override
    public void onSetup(Setup setup) {
        System.out.println("YEEET");
    }

    @Override
    public void onKit(Kit kit) {
        System.out.println("YEEET");
    }
}
