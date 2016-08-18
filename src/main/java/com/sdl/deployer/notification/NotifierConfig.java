package com.sdl.deployer.notification;

import java.util.Map;

public class NotifierConfig {
    private final String notifierId;
    private final Map<String, String> properties;

    public NotifierConfig(String notifierId, Map<String, String> properties) {
        this.notifierId = notifierId;
        this.properties = properties;
    }

    public String getNotifierId() {
        return notifierId;
    }

    public String getProperty(String property) {
        return this.properties.get(property);
    }

    public boolean hasProperty(String property) {
        return this.properties.containsKey(property);
    }
}
