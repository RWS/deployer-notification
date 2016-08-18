package com.sdl.deployer.notification;

import java.util.List;

public class NotifierConditionConfig {
    private final List<NotifierConfig> notifierConfigs;

    public NotifierConditionConfig(List<NotifierConfig> notifierConfigs) {
        this.notifierConfigs = notifierConfigs;
    }

    public List<NotifierConfig> getNotifierConfigs() {
        return notifierConfigs;
    }
}
