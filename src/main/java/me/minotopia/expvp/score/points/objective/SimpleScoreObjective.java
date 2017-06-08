/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.points.objective;

import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.score.points.ScoreTalentPointObjective;
import me.minotopia.expvp.api.score.points.TalentPointType;

import java.util.function.Function;

/**
 * A Talent Point objective solely based on a numerical score.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public class SimpleScoreObjective extends SimpleObjective implements ScoreTalentPointObjective {
    private final long missingAmount;

    public SimpleScoreObjective(TalentPointType type, long missingAmount, Function<Long, Message> pluralFunction) {
        super(Message.of("score!tp.obj.score", pluralFunction.apply(missingAmount)), type);
        this.missingAmount = missingAmount;
    }

    @Override
    public long getMissingAmount() {
        return missingAmount;
    }
}
