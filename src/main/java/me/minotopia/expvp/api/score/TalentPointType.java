/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score;

/**
 * Represents a type of Talent Point. Talent Points are categorised by how they are obtained.
 * <p><b>Note:</b> The ordinals of this enum are used for persistence because Hibernate has better support for that</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-31
 */
public enum TalentPointType {
    COMBAT,
    ACTIVITY_AND_DEATHS,
    DAY_ACTIVITY;

    public int getLimit() {
        return 64;
    }

    public static TalentPointType getById(int typeId) {
        TalentPointType[] values = values();
        if (typeId <= values.length) {
            throw new IllegalArgumentException("No type for id " + typeId);
        } else {
            return values[typeId];
        }
    }
}
