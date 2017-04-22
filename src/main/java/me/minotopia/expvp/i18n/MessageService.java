/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.google.common.base.Preconditions;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.*;

/**
 * Handles retrieval of messages from the correct resource bundle.
 *
 * <p><b>Note:</b> To make a message service retrieve custom resource bundles from the file system, use {@link
 * #setDataFolder(File)}.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-03
 */
public class MessageService {
    private static final Logger LOGGER = LoggingManager.getLogger(MessageService.class);
    private final BundleCache bundleCache = new BundleCache();

    public String getMessage(Locale locale, Message message) {
        Preconditions.checkNotNull(locale, "locale");
        if (isDebugLocale(locale) || message.getKey() == null) {
            return message.toString();
        }
        MessagePath path = MessagePath.of(message.getKey());
        Optional<ResourceBundle> bundle = bundleCache.findBundleContaining(locale, path);
        if (bundle.isPresent()) {
            return doTranslation(locale, bundle.get(), path.key(), message.getArguments());
        } else {
            if (message.hasFallback()) {
                return getMessage(locale, message.getFallback());
            } else {
                LOGGER.warn("Missing {} for {}", path, locale);
                return message.toString();
            }
        }
    }

    private boolean isDebugLocale(Locale locale) {
        return "debug".equals(locale.getVariant());
    }

    private String doTranslation(Locale locale, ResourceBundle bundle, String key, Object... args) {
        String pattern = bundle.getString(key);
        return new MessageFormat(pattern, locale)
                .format(localizeMessageArguments(locale, args));
    }

    private Object[] localizeMessageArguments(Locale locale, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Message) {
                args[i] = getMessage(locale, (Message) args[i]);
            }
        }
        return args;
    }

    public String getMessage(Locale locale, String key, Object... args) {
        Preconditions.checkNotNull(locale, "locale");
        if (isDebugLocale(locale)) {
            return messageKeyString(key, args);
        }
        MessagePath path = MessagePath.of(key);
        Optional<ResourceBundle> bundle = bundleCache.findBundleContaining(locale, path);
        if (!bundle.isPresent()) {
            LOGGER.warn("Missing {} for {}", path, locale);
            return messageKeyString(key, args);
        } else {
            return doTranslation(locale, bundle.get(), path.key(), args);
        }
    }

    private String messageKeyString(String key, Object... args) {
        return key + Arrays.toString(args);
    }

    /**
     * @param dataFolder the data folder to use for retrieval of custom resource bundles
     * @throws NullPointerException if dataFolder is null
     */
    public void setDataFolder(File dataFolder) {
        Preconditions.checkNotNull(dataFolder, "dataFolder");
        try {
            bundleCache.setCustomLoader(folderClassLoader(dataFolder));
            File defaultsFolder = new File(dataFolder, "defaults_do_not_edit");
            copyDirectoryFromJarTo(defaultsFolder.toPath());
            bundleCache.setDefaultLoader(folderClassLoader(defaultsFolder));
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Invalid data folder of some sort: " + dataFolder.getAbsolutePath(), e);
            throw new AssertionError("I/O exception while in MessageService: " + dataFolder.getAbsolutePath(), e);
        }
    }

    private URLClassLoader folderClassLoader(File folder) throws IOException {
        Files.createDirectories(folder.toPath());
        return new URLClassLoader(new URL[]{folder.toURI().toURL()});
    }

    private void copyDirectoryFromJarTo(Path target) throws URISyntaxException, IOException {
        URI resource = getClass().getResource("/").toURI();
        FileSystem fileSystem = FileSystems.newFileSystem(
                resource, Collections.<String, String>emptyMap()
        );
        Path jarPath = fileSystem.getPath("me", "minotopia", "expvp");
        Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {
            private Path currentTarget;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                currentTarget = target.resolve(jarPath.relativize(dir).toString());
                Files.createDirectories(currentTarget);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void clearCache() {
        bundleCache.clear();
    }
}
