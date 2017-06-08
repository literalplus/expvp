/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points.objective;

import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.score.points.TalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointType;

/**
 * A simple Talent Point objective with arbitrary message.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class SimpleObjective implements TalentPointObjective {
    protected final TalentPointType type;
    protected final Message description;

    public SimpleObjective(Message description, TalentPointType type) {
        this.description = description;
        this.type = type;
    }

    @Override
    public TalentPointType getType() {
        return type;
    }

    @Override
    public Message getDescription() {
        return description;
    }
}
