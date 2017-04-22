/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageServiceTest {
    @Test
    public void testSetDataFolder() throws IOException {
        //given
        MessageService messageService = new MessageService();
        File dataFolder = new File("target/i18n-test");
        Files.createDirectories(dataFolder.toPath());
        //when
        messageService.setDataFolder(dataFolder);
        //then
        assertThat("core exists",
                Files.exists(dataFolder.toPath().resolve("defaults_do_not_edit").resolve("core.properties")),
                is(true));
    }
}
