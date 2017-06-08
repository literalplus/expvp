/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.points;

import li.l1t.common.i18n.Message;

/**
 * Represents a type of Talent Point. Talent Points are categorised by how they are obtained.
 * <p><b>Note:</b> The ordinals of this enum are used for persistence because Hibernate has better support for that</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-31
 */
public enum TalentPointType {
    /**
     * Talent Points received based on the absolute amount of kills since the last reset.
     */
    COMBAT("score!tp.type.combat"),
    /**
     * Talent Points received based on the absolute amount of deaths since the last reset.
     * <p><b>Note:</b> This type shares their limit with {@link #RECENT_DAMAGE}. This means that if the combined
     * amount of Talent Points received from both types reaches {@link #getLimit()}, no further Talent Points are
     * to be granted in either type.</p>
     */
    DEATHS("score!tp.type.deaths"),
    /**
     * Talent Points received based on the amount of damage dealt to players recently.
     * <p><b>Note:</b> This type shares their limit with {@link #DEATHS}. This means that if the combined
     * amount of Talent Points received from both types reaches {@link #getLimit()}, no further Talent Points are
     * to be granted in either type.</p>
     */
    RECENT_DAMAGE("score!tp.type.recent_damage"),
    /**
     * Talent Points received based on the current day of week. This aims to provide a small amount of Talent Points to
     * all players, so that they may research basic skills.
     */
    DAY_ACTIVITY("score!tp.type.day_activity");

    private final Message description;

    TalentPointType(String descriptionKey) {
        this.description = Message.of(descriptionKey);
    }

    public int getLimit() {
        return 64;
    }

    public Message getDescription() {
        return description;
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
