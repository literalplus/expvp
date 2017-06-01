/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate.converter;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.score.TalentPointType;

import javax.persistence21.AttributeConverter;

/**
 * Converts talent point types to their ids.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-01
 */
public class TalentPointTypeConverter implements AttributeConverter<TalentPointType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TalentPointType talentPointType) {
        Preconditions.checkNotNull(talentPointType, "talentPointType");
        return talentPointType.ordinal();
    }

    @Override
    public TalentPointType convertToEntityAttribute(Integer typeId) {
        return TalentPointType.getById(typeId);
    }
}
