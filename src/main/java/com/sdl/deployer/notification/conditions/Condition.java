package com.sdl.deployer.notification.conditions;

import com.tridion.transport.transportpackage.Item;

public interface Condition {
    boolean eval(Item item);
}
