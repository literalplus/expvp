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

import java.util.*;

/**
 * Caches resource bundles, forwarding to a loader for cache misses.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-03
 */
class BundleCache {
    private static final Logger LOGGER = LoggingManager.getLogger(BundleCache.class);
    private final Map<String, Optional<CachedBundle>> customBundles = new HashMap<>();
    private final Map<String, Optional<CachedBundle>> defaultBundles = new HashMap<>();
    private ClassLoader defaultLoader;
    private ClassLoader customLoader;

    public void setDefaultLoader(ClassLoader defaultLoader) {
        Preconditions.checkNotNull(defaultLoader, "defaultLoader");
        this.defaultLoader = defaultLoader;
    }

    public void setCustomLoader(ClassLoader customLoader) {
        Preconditions.checkNotNull(customLoader, "customLoader");
        this.customLoader = customLoader;
    }

    public Optional<ResourceBundle> findBundleContaining(Locale locale, MessagePath path) {
        return findCustomBundleContaining(locale, path)
                .map(Optional::of)
                .orElseGet(() -> findDefaultsBundleContaining(locale, path));
    }

    private Optional<ResourceBundle> findCustomBundleContaining(Locale locale, MessagePath path) {
        return getCustom(path.bundle())
                .map(bundle -> bundle.getBundleFor(locale))
                .filter(bundle -> bundle.containsKey(path.key()));
    }

    private Optional<ResourceBundle> findDefaultsBundleContaining(Locale locale, MessagePath path) {
        return getFromDefaults(path.bundle())
                .map(bundle -> bundle.getBundleFor(locale))
                .filter(bundle -> bundle.containsKey(path.key()));
    }

    private Optional<CachedBundle> getCustom(String baseName) {
        if (customLoader == null) {
            return Optional.empty();
        }
        return customBundles.computeIfAbsent(baseName, this::customBundleFor);
    }

    private Optional<CachedBundle> customBundleFor(String baseName) {
        return bundleFor(baseName, customLoader, "custom file");
    }

    private Optional<CachedBundle> bundleFor(String baseName, ClassLoader classLoader, String bundleTypeDesc) {
        try {
            return Optional.of(new CachedBundle(baseName, classLoader));
        } catch (MissingResourceException e) {
            LOGGER.debug(String.format("No %s language bundle for I18n key %s <> %s %s",
                    bundleTypeDesc, baseName, e.getClass().getSimpleName(), e.getMessage()));
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
        return bundleFor(baseName, defaultLoader, "default");
    }

    public void clear() {
        customBundles.clear();
        defaultBundles.clear();
    }
}
