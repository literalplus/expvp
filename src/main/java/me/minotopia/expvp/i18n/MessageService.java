/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.minotopia.expvp.i18n;

import com.google.common.base.Preconditions;
import li.l1t.common.i18n.Message;
import li.l1t.common.util.FileHelper;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    private BundleCache bundleCache = new BundleCache();
    private File dataFolder;

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
        this.dataFolder = dataFolder;
        try {
            bundleCache.setCustomLoader(folderClassLoader(dataFolder));
            File defaultsFolder = new File(dataFolder, "defaults_do_not_edit");
            copyDirectoryFromJarTo(defaultsFolder);
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

    private void copyDirectoryFromJarTo(File target) throws URISyntaxException, IOException {
        copyResourcesRecursively(getClass().getResource("/lang/core.properties"), target);
    }

    private void copyResourcesRecursively(URL originUrl, File destination) throws IOException {
        URLConnection urlConnection = originUrl.openConnection();
        urlConnection.setUseCaches(false);
        if (urlConnection instanceof JarURLConnection) {
            copyJarResourcesRecursively(destination.toPath(), (JarURLConnection) urlConnection);
        } else if (urlConnection.getClass().getSimpleName().equals("FileURLConnection")) {
            FileHelper.copyFolder(new File(originUrl.getPath()), destination);
        } else {
            throw new IllegalArgumentException("URLConnection[" + urlConnection.getClass().getSimpleName() +
                    "] is not a recognized/implemented connection type.");
        }
    }

    private void copyJarResourcesRecursively(Path destination, JarURLConnection jarConnection) throws IOException {
        JarFile jarFile = jarConnection.getJarFile();
        enumerationAsStream(jarFile.entries())
                .filter(jarEntry -> jarEntry.getName().startsWith("lang"))
                .forEach(jarEntry -> copyJarEntryTo(destination, "lang/", jarFile, jarEntry));
    }

    private void copyJarEntryTo(Path destination, String prefix, JarFile jarFile, JarEntry jarEntry) {
        try {
            String fileName = StringUtils.removeStart(jarEntry.getName(), prefix);
            Path resolvedPath = destination.resolve(fileName);
            if (jarEntry.isDirectory()) {
                Files.createDirectories(resolvedPath);
            } else {
                try (InputStream entryInputStream = jarFile.getInputStream(jarEntry)) {
                    Files.copy(entryInputStream, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        /*
        ─────────▄▄───────────────────▄▄──
        ──────────▀█───────────────────▀█─
        ──────────▄█───────────────────▄█─
        ──█████████▀───────────█████████▀─
        ───▄██████▄─────────────▄██████▄──
        ─▄██▀────▀██▄─────────▄██▀────▀██▄
        ─██────────██─────────██────────██
        ─██───██───██─────────██───██───██
        ─██────────██─────────██────────██
        ──██▄────▄██───────────██▄────▄██─
        ───▀██████▀─────────────▀██████▀──
        ──────────────────────────────────
        ──────────────────────────────────
        ───────────█████████████──────────
        ──────────────────────────────────
        ──────────────────────────────────
         */
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }

    public void clearCache() {
        bundleCache = new BundleCache();
        if (dataFolder != null) {
            setDataFolder(dataFolder);
        }
    }
}
