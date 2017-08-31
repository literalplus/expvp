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

package me.minotopia.expvp.api.score.points;

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
