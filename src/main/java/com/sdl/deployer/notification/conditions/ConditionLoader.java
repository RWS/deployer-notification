package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

public interface ConditionLoader {
    default String getConditionName() {
        return getClass().getSimpleName();
    }

    Condition loadCondition(Configuration configuration) throws ConfigurationException;
}
