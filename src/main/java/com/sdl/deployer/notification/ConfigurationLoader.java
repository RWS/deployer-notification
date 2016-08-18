package com.sdl.deployer.notification;

import com.sdl.deployer.notification.conditions.Condition;
import com.sdl.deployer.notification.conditions.ConditionLoader;
import com.sdl.deployer.notification.conditions.ConditionLoaderFactory;
import com.sdl.deployer.notification.conditions.RootCondition;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigurationLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);

    public static List<RootCondition> getRootConditions(Configuration moduleConfig) throws ConfigurationException {
        Configuration conditionConfig = moduleConfig.getChild("Conditions");
        if(conditionConfig != null) {
            return loadRootConditions(conditionConfig);
        } else {
            LOG.error("No conditions configured, not sending notifications");
            return new ArrayList<>();
        }
    }

    public static List<RootCondition> loadRootConditions(Configuration conditionConfig) throws ConfigurationException {
        List<Configuration> childConfigs = conditionConfig.getChildren();

        List<RootCondition> rootConditions = new ArrayList<>();
        for(Configuration rootConfig: childConfigs) {
            Condition rootCondition = loadCondition(rootConfig);

            if(rootConfig.hasChild("Notifiers")) {
                NotifierConditionConfig notifierConditionConfig = loadNotifierConfig(rootConfig.getChild("Notifiers"));
                rootConditions.add(new RootCondition(rootCondition, notifierConditionConfig));
            } else {
                rootConditions.add(new RootCondition(rootCondition));
            }
        }
        return rootConditions;
    }

    public static List<Condition> loadChildConditions(Configuration parentConfig) throws ConfigurationException {
        List<Configuration> childConfigs = parentConfig.getChildren();

        List<Condition> conditions = new ArrayList<>();
        for(Configuration childConfig: childConfigs) {
            if(!childConfig.getName().equalsIgnoreCase("Notifiers")) {
                Condition condition = loadCondition(childConfig);
                conditions.add(condition);
            }
        }
        return conditions;
    }

    public static Condition loadCondition(Configuration conditionConfig) throws ConfigurationException {
        String conditionName = conditionConfig.getName();
        ConditionLoader loader = ConditionLoaderFactory.getConditionLoader(conditionName);
        if(loader != null) {
            return loader.loadCondition(conditionConfig);
        } else {
            throw new ConfigurationException("Condition: " + conditionName + " specified but not recognized");
        }
    }

    public static NotifierConditionConfig loadNotifierConfig(Configuration notifiersConfig) throws ConfigurationException {
        List<NotifierConfig> configs = new ArrayList<>();
        for(Configuration notifierConfig : notifiersConfig.getChildrenByName("Notifier")) {
            String notifierId = notifierConfig.getAttribute("Id");
            configs.add(new NotifierConfig(notifierId, toProperties(notifierConfig)));
        }

        return new NotifierConditionConfig(configs);
    }

    public static Map<String, String> toProperties(Configuration configuration) throws ConfigurationException {
        List<Configuration> properties = configuration.getChildrenByName("Property");
        Map<String, String> mappedProperties = new HashMap<>();
        for(Configuration property : properties) {
            mappedProperties.put(property.getAttribute("Name"), property.getAttribute("Value"));
        }
        return mappedProperties;
    }

    public static String getProperty(Optional<NotifierConfig> notifierConfig, Map<String, String> defaultProperties, String property) {
        if(notifierConfig.isPresent()) {
            NotifierConfig config = notifierConfig.get();
            if(config.hasProperty(property)) {
                return config.getProperty(property);
            }
        }

        return defaultProperties.get(property);
    }
}
