/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Caches resource bundles, forwarding to a loader for cache misses.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-03
 */
class BundleCache {
    private static final Logger LOGGER = LoggingManager.getLogger(BundleCache.class);
    private final ClassLoader fileLoader;
    private final Map<String, Optional<CachedBundle>> fileBundles = new HashMap<>();
    private final Map<String, Optional<CachedBundle>> defaultBundles = new HashMap<>();

    BundleCache(ClassLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public Optional<ResourceBundle> findBundleContaining(Locale locale, MessagePath path) {
        return findFileBundleContaining(locale, path)
                .map(Optional::of)
                .orElseGet(() -> findDefaultsBundleContaining(locale, path));
    }

    private Optional<ResourceBundle> findFileBundleContaining(Locale locale, MessagePath path) {
        return getFromFile(path.bundle())
                .map(bundle -> bundle.getBundleFor(locale))
                .filter(bundle -> bundle.containsKey(path.key()));
    }

    private Optional<ResourceBundle> findDefaultsBundleContaining(Locale locale, MessagePath path) {
        return getFromDefaults(path.bundle())
                .map(bundle -> bundle.getBundleFor(locale))
                .filter(bundle -> bundle.containsKey(path.key()));
    }

    private Optional<CachedBundle> getFromFile(String baseName) {
        return fileBundles.computeIfAbsent(baseName, this::fileBundleFor);
    }

    private Optional<CachedBundle> fileBundleFor(String baseName) {
        return bundleFor(baseName, fileLoader, "custom file");
    }

    private Optional<CachedBundle> bundleFor(String baseName, ClassLoader classLoader, String bundleTypeDesc) {
        try {
            return Optional.of(new CachedBundle(baseName, classLoader));
        } catch (MissingResourceException e) {
            LOGGER.debug(String.format("No %s language bundle for I18n key %s", bundleTypeDesc, baseName), e);
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.warn(String.format("Unable to load %s language bundle for I18n key %s", bundleTypeDesc, baseName), e);
            return Optional.empty();
        }
    }

    private Optional<CachedBundle> getFromDefaults(String baseName) {
        return defaultBundles.computeIfAbsent(baseName, this::defaultsBundleFor);
    }

    private Optional<CachedBundle> defaultsBundleFor(String baseName) {
        return bundleFor("me.minotopia.expvp." + baseName, getClass().getClassLoader(), "default");
    }
}
