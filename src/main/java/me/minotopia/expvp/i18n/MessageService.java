/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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

    public String getMessage(Locale locale, String key, Object... args) {
        Preconditions.checkNotNull(locale, "locale");
        if ("debug".equals(locale.getVariant())) {
            return messageKeyString(key, args);
        }
        MessagePath path = MessagePath.of(key);
        Optional<ResourceBundle> bundle = bundleCache.findBundleContaining(locale, path);
        if (!bundle.isPresent()) {
            LOGGER.warn("Missing {} for {}", path, locale);
            return messageKeyString(key, args);
        } else {
            String pattern = bundle.get().getString(path.key());
            return String.format(pattern, args);
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
            bundleCache.setFileLoader(new URLClassLoader(new URL[]{dataFolder.toURI().toURL()}));
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid data folder of some sort: " + dataFolder.getAbsolutePath(), e);
            throw new AssertionError("Invalid URL while in MessageService: " + dataFolder.getAbsolutePath(), e);
        }
    }

    public void clearCache() {
        bundleCache.clear();
    }
}
