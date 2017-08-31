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
import li.l1t.common.i18n.Utf8Control;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Caches a resource bundle for a specific base name from a specific class loader.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-03
 */
class CachedBundle {
    private final ResourceBundle germanBundle;
    private final ResourceBundle englishBundle;
    private final String baseName;
    private final ClassLoader loader;

    public CachedBundle(String baseName, ClassLoader loader) {
        this.baseName = baseName;
        this.loader = loader;
        germanBundle = loadBundle(Locale.GERMAN);
        englishBundle = loadBundle(Locale.ENGLISH);
    }

    private ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle(baseName, locale, loader, Utf8Control.NO_CACHE);
    }

    public ResourceBundle getGermanBundle() {
        return germanBundle;
    }

    public ResourceBundle getEnglishBundle() {
        return englishBundle;
    }

    public ResourceBundle getBundleFor(Locale locale) {
        Preconditions.checkNotNull(locale, "locale");
        if (locale.getISO3Language().equals(Locale.GERMAN.getISO3Language())) {
            return getGermanBundle();
        } else {
            return getEnglishBundle();
        }
    }
}
