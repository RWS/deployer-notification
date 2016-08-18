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
