/**
 * Copyright (c) 2016 All Rights Reserved by the SDL Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdl.deployer.notification;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.locks.ReentrantLock;

public class NotifierFactory {
    private static final Logger LOG = LoggerFactory.getLogger(NotifierFactory.class);

    private Map<String, Notifier> notifierMap = new HashMap<>();

    private static NotifierFactory INSTANCE = null;

    private static final ReentrantLock LOCK = new ReentrantLock();

    public NotifierFactory(Configuration configuration) {
        ServiceLoader.load(Notifier.class).forEach(n -> {
            try {
                Configuration notifierConfig = getConfiguration(configuration, n.getNotifierId());
                if(notifierConfig != null) {
                    LOG.info("Loading notifier: {}", n.getNotifierId());
                    n.configure(notifierConfig);
                    notifierMap.put(n.getNotifierId(), n);
                } else {
                    LOG.warn("Could not find configuration for notifier: {}", n.getNotifierId());
                }
            } catch (ConfigurationException e) {
                LOG.warn("Could not configure the notifier: {} for reason: {}", n, e.getMessage());
            }
        });
    }

    private Configuration getConfiguration(Configuration notifierConfigs, String notifierId)
            throws ConfigurationException {
        for(Configuration configuration : notifierConfigs.getChildren()) {
            if(configuration.getAttribute("Id").equalsIgnoreCase(notifierId)) {
                return configuration;
            }
        }
        return null;
    }

    public static NotifierFactory getInstance(Configuration configuration) throws ConfigurationException {
        if(INSTANCE == null) {
            LOCK.lock();
            try {
                INSTANCE = new NotifierFactory(configuration);
            } finally {
                LOCK.unlock();
            }
        }
        return INSTANCE;
    }

    public List<Notifier> getNotifiers() {
        return new ArrayList<>(notifierMap.values());
    }

    public Notifier getNotifier(String notifierId) {
        return notifierMap.get(notifierId);
    }
}
