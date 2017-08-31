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

package me.minotopia.expvp.model.hibernate.converter;

import com.google.common.base.Preconditions;
import me.minotopia.expvp.api.score.points.TalentPointType;

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
