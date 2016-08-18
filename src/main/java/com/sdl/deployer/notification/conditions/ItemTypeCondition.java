package com.sdl.deployer.notification.conditions;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.Page;

public class ItemTypeCondition implements Condition, ConditionLoader {

    private final int itemType;

    public ItemTypeCondition(int itemType) {
        this.itemType = itemType;
    }

    public ItemTypeCondition() {
        this.itemType = 16;
    }

    @Override
    public boolean eval(Item item) {
        if(item instanceof Component && itemType == 16) {
            return true;
        } else if(item instanceof Page && itemType == 64) {
            return true;
        }
        return false;
    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        int itemType = configuration.getAttributeAsInt("ItemType");

        return new ItemTypeCondition(itemType);
    }

    @Override
    public String toString() {
        return "ItemTypeCondition{" +
                "itemType=" + itemType +
                '}';
    }
}
