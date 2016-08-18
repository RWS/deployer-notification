package com.sdl.deployer.notification;

import com.sdl.deployer.notification.conditions.RootCondition;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.deployer.Module;
import com.tridion.deployer.ProcessingException;
import com.tridion.deployer.Processor;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.MetaData;
import com.tridion.transport.transportpackage.MetaDataFile;
import com.tridion.transport.transportpackage.ProcessorInstructions;
import com.tridion.transport.transportpackage.TransportPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static com.tridion.transport.TransportConstants.COMPONENT_TYPE;
import static com.tridion.transport.TransportConstants.PAGE_TYPE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class NotificationModule extends Module {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationModule.class);

    private List<RootCondition> rootConditions;
    private NotifierFactory notifierFactory;

    public NotificationModule(Configuration configuration, Processor processor) throws ConfigurationException {
        super(configuration, processor);
    }

    @Override
    public void configure(Configuration configuration) throws ConfigurationException {
        super.configure(configuration);
        rootConditions = ConfigurationLoader.getRootConditions(configuration);
        notifierFactory = NotifierFactory.getInstance(configuration.getChild("Notifiers"));
        rootConditions.forEach(c -> LOG.info("Following notifier conditions set: {}", c));
    }

    @Override
    public void process(TransportPackage data) throws ProcessingException {
        if(!rootConditions.isEmpty()) {
            processItemType(data, PAGE_TYPE);
            processItemType(data, COMPONENT_TYPE);
        }
    }

    private void processItemType(TransportPackage data, String itemType) {
        ProcessorInstructions instructions = data.getProcessorInstructions();

        MetaData metaInfo = instructions.getMetaData(itemType);
        MetaDataFile metaFile = data.getMetaData(itemType, metaInfo.getName());
        Iterator iterator = metaFile.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            rootConditions.forEach(c -> {
                if(c.eval(item)) {
                    runNotifiers(c, item);
                }
            });
        }
    }

    private void runNotifiers(RootCondition c, Item item) {
        if(c.getConfig().isPresent()) {
            NotifierConditionConfig config = c.getConfig().get();
            config.getNotifierConfigs().forEach(n -> {
                Notifier notifier = notifierFactory.getNotifier(n.getNotifierId());
                if(notifier != null) {
                    notifier.sendNotification(of(n), item);
                }
            });
        } else {
            //if nothing configured we run all
            notifierFactory.getNotifiers().forEach(n -> {
                LOG.info("Item: {} was published sending message using notifier: {}", item, n);
                n.sendNotification(empty(), item);
            });
        }
    }
}
