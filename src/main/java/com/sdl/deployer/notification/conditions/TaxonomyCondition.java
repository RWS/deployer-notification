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

import com.google.common.collect.Iterators;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Category;
import com.tridion.transport.transportpackage.Component;
import com.tridion.transport.transportpackage.Item;
import com.tridion.transport.transportpackage.Page;
import com.tridion.util.TCMURI;

import java.util.Iterator;
import java.util.Map;

public class TaxonomyCondition implements Condition, ConditionLoader {

    private final int taxonomyId;

    public TaxonomyCondition(int taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public TaxonomyCondition() {
        this.taxonomyId = 0;
    }

    @Override
    public boolean eval(Item item) {
        if(item instanceof Component) {
            return evalCategories(((Component)item).getCategories());
        } else if(item instanceof Page) {
            return evalCategories(((Page)item).getCategories());
        }

        return false;
    }

    private boolean evalCategories(Iterator<Map.Entry<String, Category>> categories) {
        return Iterators.any(categories, categoryEntry -> {
            TCMURI categoryUri = categoryEntry.getValue().getCategoryURI();

            return categoryUri.getItemId() == taxonomyId;
        });

    }

    @Override
    public Condition loadCondition(Configuration configuration) throws ConfigurationException {
        int taxonomyId = configuration.getAttributeAsInt("TaxonomyId");

        return new TaxonomyCondition(taxonomyId);
    }

    @Override
    public String toString() {
        return "TaxonomyCondition{" +
                "taxonomyId=" + taxonomyId +
                '}';
    }
}
