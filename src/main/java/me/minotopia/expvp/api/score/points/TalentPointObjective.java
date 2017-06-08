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
 * A specific objective a specific player needs to complete before they get another Talent Point of a specific type.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public interface TalentPointObjective {
    TalentPointType getType();

    Message getDescription();
}
