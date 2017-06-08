/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score.points;

/**
 * A Talent Point objective based on a numerical score.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-06-08
 */
public interface ScoreTalentPointObjective extends TalentPointObjective {
    /**
     * @return the amount of this objective's score that was missing for the next Talent Point at creation time of this
     * object
     */
    long getMissingAmount();
}
