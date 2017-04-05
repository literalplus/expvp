/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.score;

import me.minotopia.expvp.i18n.Plurals;
import me.minotopia.expvp.i18n.exception.I18nUserException;

/**
 * Thrown if a player has an insufficient amount of Talent Points for an operation.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class InsufficientTalentPointsException extends I18nUserException {
    public InsufficientTalentPointsException(int requiredPoints, int currentPoints) {
        super("error!tp.insufficient-tp", Plurals.talentPointPlural(requiredPoints), Plurals.talentPointPlural(currentPoints));
    }
}
