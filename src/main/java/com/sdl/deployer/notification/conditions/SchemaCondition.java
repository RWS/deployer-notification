package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;

public class SchemaCondition implements Condition, ConditionLoader {

    private final int schemaId;

    public SchemaCondition(int schemaId) {
        this.schemaId = schemaId;
    }

    public SchemaCondition() {
        this.schemaId = 0;
    }

    @Override
    public boolean eval(Item item) {
        return item instanceof Component && ((Component)item).getSchemaId().getItemId() == schemaId;
    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        return new SchemaCondition(configuration.getAttributeAsInt("SchemaId"));
    }

    @Override
    public String toString() {
        return "SchemaCondition{" +
                "schemaId=" + schemaId +
                '}';
    }
}
