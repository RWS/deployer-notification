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

import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableReplacer {
    private static final Logger LOG = LoggerFactory.getLogger(VariableReplacer.class);

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\$\\{(.*?)\\})");

    public static String replaceVars(String message, Item item) {
        if(item instanceof Page) {
            return replace(message, ((Page)item).getPageMetaFacade());
        } else if(item instanceof Component) {
            return replace(message, ((Component)item).getComponentMetaFacade());
        } else {
            return null;
        }
    }

    private static String replace(String input, com.tridion.meta.Item item) {
        Matcher matcher = VARIABLE_PATTERN.matcher(input);

        int n = 0;
        StringBuilder b = new StringBuilder();
        while(matcher.find()) {
            b.append(input.substring(n, matcher.start()));
            b.append(getProperty(matcher.group(2), item));

            n = matcher.end();
        }
        b.append(input.substring(n));

        return b.toString();
    }

    private static String getProperty(String property, com.tridion.meta.Item itemMeta) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(itemMeta.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(property)) {
                    return propertyDescriptor.getReadMethod().invoke(itemMeta).toString();
                }
            }

            LOG.info("Could not find property: {} on object: {}", property, itemMeta);
        } catch(IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            LOG.error("", e);
        }

        return null;
    }
}
