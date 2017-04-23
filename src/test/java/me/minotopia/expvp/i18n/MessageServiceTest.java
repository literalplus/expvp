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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageServiceTest {
    @Test
    public void testSetDataFolder() throws IOException {
        //given
        MessageService messageService = new MessageService();
        File dataFolder = new File("target/i18n-test");
        deleteDirectoryRecursively(dataFolder);
        Files.createDirectories(dataFolder.toPath());
        //when
        messageService.setDataFolder(dataFolder);
        //then
        assertThat("core exists",
                Files.exists(dataFolder.toPath().resolve("defaults_do_not_edit").resolve("core.properties")),
                is(true));
    }

    private void deleteDirectoryRecursively(File dataFolder) throws IOException {
        Files.walkFileTree(dataFolder.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
