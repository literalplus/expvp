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
