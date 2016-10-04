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

import com.tridion.transport.transportpackage.Page;
import com.tridion.transport.transportpackage.VersionInfo;
import com.tridion.util.TCMURI;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VariableReplacerTest {
    @Test
    public void testParseMessage() {
        Page page =new Page();
        page.setId(new TCMURI(1, 1, 16, 0));
        page.setTitle("test");

        String input = "Item with title: '${title}' with id ${id} was published";
        String formatted = VariableReplacer.replaceVars(input, page);
        assertThat(formatted, is("Item with title: 'test' with id 1 was published"));
    }

    @Test
    public void testFormatDate() {
        Page page =new Page();
        page.setId(new TCMURI(1, 1, 16, 0));
        page.setTitle("test");

        VersionInfo v = new VersionInfo();
        Calendar c = Calendar.getInstance();
        c.set(2016, 7, 16, 10, 0, 0);
        Date date = c.getTime();
        v.setVersionDate(date);
        page.setVersionInfo(v);

        String input = "Item with title: '${title}' with id ${id} was published on: ${modificationDate}";
        String formatted = VariableReplacer.replaceVars(input, page);
        assertThat(formatted, is("Item with title: 'test' with id 1 was published on: Tue Aug 16 10:00:00 CEST 2016"));
    }
}
