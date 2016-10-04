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

import com.tridion.configuration.Configurable;
import com.tridion.transport.transportpackage.Item;

import java.util.Optional;

/**
 * The notifier interface is responsible for sending notification messages to different targets. This is called
 * when all conditions specified in the configuration are met and this Notifier is configured to be triggered.
 */
public interface Notifier extends Configurable {
    /**
     * This method is called when the conditions are all met. It provides an optional notifier configuration and the
     * item that has met the conditions for notification.
     *
     * @param notifierConditionConfig The optional notifier configuration.
     * @param item The item that triggered the notification.
     */
    void sendNotification(Optional<NotifierConfig> notifierConditionConfig, Item item);

    /**
     * The identifier of the notifier, by default is the class name of the notifier.
     * @return The id of the notifier.
     */
    default String getNotifierId() {
        return getClass().getSimpleName();
    }
}
