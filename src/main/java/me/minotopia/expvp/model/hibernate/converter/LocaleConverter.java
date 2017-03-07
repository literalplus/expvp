/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.converter;

import javax.persistence21.AttributeConverter;
import java.util.Locale;

/**
 * Converts language tag strings to locale objects.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-07
 */
public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return locale == null ? null : locale.toLanguageTag();
    }

    @Override
    public Locale convertToEntityAttribute(String languageTag) {
        if (languageTag == null) {
            return null;
        } else if (languageTag.isEmpty()) {
            return Locale.getDefault();
        } else {
            return Locale.forLanguageTag(languageTag);
        }
    }
}
