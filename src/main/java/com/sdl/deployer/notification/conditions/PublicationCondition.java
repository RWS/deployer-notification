package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.Page;

public class PublicationCondition implements Condition, ConditionLoader {
    private final int publicationId;

    public PublicationCondition(int publicationId) {
        this.publicationId = publicationId;
    }

    public PublicationCondition() {
        this.publicationId = 0;
    }

    @Override
    public boolean eval(Item item) {
        if(item instanceof Component) {
            return ((Component)item).getId().getPublicationId() == publicationId;
        } else if(item instanceof Page) {
            return ((Page)item).getId().getPublicationId() == publicationId;
        }
        return false;
    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        return new PublicationCondition(configuration.getAttributeAsInt("PublicationId"));
    }

    @Override
    public String toString() {
        return "PublicationCondition{" +
                "publicationId=" + publicationId +
                '}';
    }
}
