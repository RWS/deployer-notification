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
package com.sdl.deployer.notification.hipchat;

import com.sdl.deployer.notification.Notifier;
import com.sdl.deployer.notification.NotifierConfig;
import com.sdl.deployer.notification.VariableReplacer;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Item;
import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.sdl.deployer.notification.ConfigurationLoader.getProperty;
import static com.sdl.deployer.notification.ConfigurationLoader.toProperties;

public class HipchatNotifier implements Notifier {
    private static final Logger LOG = LoggerFactory.getLogger(HipchatNotifier.class);

    private Map<String, String> defaultProperties;

    @Override
    public void configure(Configuration configuration) throws ConfigurationException {
        this.defaultProperties = toProperties(configuration);
    }

    @Override
    public void sendNotification(Optional<NotifierConfig> notifierConditionConfig, Item item) {
        String token = getProperty(notifierConditionConfig, defaultProperties, "token");
        String room = getProperty(notifierConditionConfig, defaultProperties, "room");
        String template = getProperty(notifierConditionConfig, defaultProperties, "template");

        if(token != null && room != null) {
            HipChatClient client = new HipChatClient(token);

            String message = VariableReplacer.replaceVars(template, item);
            SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(room, message);
            Future<NoContent> future = builder.setColor(MessageColor.GREEN).setNotify(true).build().execute();
            try {
                NoContent noContent = future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Could not send hipchat notification", e);
            }
        } else {
            LOG.warn("Could not send hipchat notification, no token or room configured");
        }
    }


}
