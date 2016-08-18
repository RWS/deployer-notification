package com.sdl.deployer.notification.conditions;

import com.sdl.deployer.notification.NotifierConditionConfig;
import com.tridion.transport.transportpackage.Item;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class RootCondition implements Condition {
    private final Condition wrapped;
    private final Optional<NotifierConditionConfig> config;

    public RootCondition(Condition wrapped, NotifierConditionConfig config) {
        this.wrapped = wrapped;
        this.config = of(config);
    }

    public RootCondition(Condition wrapped) {
        this.wrapped = wrapped;
        this.config = empty();
    }

    public Optional<NotifierConditionConfig> getConfig() {
        return config;
    }

    @Override
    public boolean eval(Item item) {
        return wrapped.eval(item);
    }

    @Override
    public String toString() {
        return "RootCondition{" +
                "wrapped=" + wrapped +
                ", config=" + config +
                '}';
    }
}
