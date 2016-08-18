package com.sdl.deployer.notification.conditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ConditionLoaderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ConditionLoaderFactory.class);

    private Map<String, ConditionLoader> conditionLoaders = new HashMap<>();

    private static final ConditionLoaderFactory INSTANCE = new ConditionLoaderFactory();

    private ConditionLoaderFactory() {
        ServiceLoader.load(ConditionLoader.class).forEach(cl -> {
            LOG.info("Loading condition support for: {}", cl.getConditionName());
            conditionLoaders.put(cl.getConditionName(), cl);
        });
    }

    private ConditionLoader getLoader(String condition) {
        return conditionLoaders.get(condition);
    }

    public static ConditionLoader getConditionLoader(String condition) {
        return INSTANCE.getLoader(condition);
    }
}
