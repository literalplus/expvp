/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
        return ResourceBundle.getBundle(baseName, locale, loader, new Utf8Control());
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
