package com.sdl.deployer.notification;

import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.Page;
import com.tridion.util.TCMURI;

public class NotificationUtil {
    public static TCMURI getId(Item item) {
        if(item instanceof Page) {
            return ((Page)item).getId();
        } else if(item instanceof Component) {
            return ((Component)item).getId();
        } else {
            return new TCMURI(0, 0, 0, 0);
        }
    }
}
