package com.sdl.deployer.notification;

import com.tridion.configuration.Configurable;
import com.tridion.transport.transportpackage.Item;

import java.util.Optional;

public interface Notifier extends Configurable {
    void sendNotification(Optional<NotifierConfig> notifierConditionConfig, Item item);

    default String getNotifierId() {
        return getClass().getSimpleName();
    }
}
