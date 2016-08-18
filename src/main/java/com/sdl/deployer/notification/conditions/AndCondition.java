package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Item;

import java.util.ArrayList;
import java.util.List;

import static com.sdl.deployer.notification.ConfigurationLoader.loadChildConditions;

public class AndCondition implements Condition, ConditionLoader {
    private final List<Condition> conditions;

    public AndCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public AndCondition() {
        conditions = new ArrayList<>();
    }

    @Override
    public boolean eval(Item item) {
        return conditions.stream().allMatch(c -> c.eval(item));
    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        return new AndCondition(loadChildConditions(configuration));
    }

    @Override
    public String toString() {
        return "AndCondition{" +
                "conditions=" + conditions +
                '}';
    }
}
