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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ConditionLoaderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ConditionLoaderFactory.class);

    private Map<String, ConditionLoader> conditionLoaders = new HashMap<>();

    private static final ConditionLoaderFactory INSTANCE = new ConditionLoaderFactory();

    private ConditionLoaderFactory() {
        ServiceLoader.load(ConditionLoader.class).forEach(cl -> {
            LOG.info("Loading condition support for: {}", cl.getConditionName());
            conditionLoaders.put(cl.getConditionName(), cl);
        });
    }

    private ConditionLoader getLoader(String condition) {
        return conditionLoaders.get(condition);
    }

    public static ConditionLoader getConditionLoader(String condition) {
        return INSTANCE.getLoader(condition);
    }
}
