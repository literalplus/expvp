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
