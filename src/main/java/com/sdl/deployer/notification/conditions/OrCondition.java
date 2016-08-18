package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Item;

import java.util.ArrayList;
import java.util.List;

import static com.sdl.deployer.notification.ConfigurationLoader.loadChildConditions;

public class OrCondition implements Condition, ConditionLoader {
    private final List<Condition> conditions;

    public OrCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public OrCondition() {
        conditions = new ArrayList<>();
    }

    @Override
    public boolean eval(Item item) {
        return conditions.stream().anyMatch(c -> c.eval(item));
    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        return new OrCondition(loadChildConditions(configuration));
    }

    @Override
    public String toString() {
        return "OrCondition{" +
                "conditions=" + conditions +
                '}';
    }
}
