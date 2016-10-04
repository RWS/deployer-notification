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
package com.sdl.deployer.notification.conditions;

import com.sdl.deployer.notification.NotifierConditionConfig;
import com.tridion.transport.transportpackage.Item;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class RootCondition implements Condition {
    private final Condition wrapped;
    private final Optional<NotifierConditionConfig> config;

    public RootCondition(Condition wrapped, NotifierConditionConfig config) {
        this.wrapped = wrapped;
        this.config = of(config);
    }

    public RootCondition(Condition wrapped) {
        this.wrapped = wrapped;
        this.config = empty();
    }

    public Optional<NotifierConditionConfig> getConfig() {
        return config;
    }

    @Override
    public boolean eval(Item item) {
        return wrapped.eval(item);
    }

    @Override
    public String toString() {
        return "RootCondition{" +
                "wrapped=" + wrapped +
                ", config=" + config +
                '}';
    }
}
