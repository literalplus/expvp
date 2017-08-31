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

package me.minotopia.expvp.handler.factory.damage;

import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.handler.damage.NotTodayHandler;
import me.minotopia.expvp.handler.factory.HandlerArgs;
import me.minotopia.expvp.skill.meta.Skill;

/**
 * Creates not today handlers.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public class NotTodayHandlerFactory extends AbstractDamageHandlerFactory {
    private final PlayerInitService initService;

    public NotTodayHandlerFactory(String ownHandlerSpec, PlayerInitService initService) {
        super(ownHandlerSpec);
        this.initService = initService;
    }

    @Override
    public String getDescription() {
        return "poisonous armor: probability, duration_seconds";
    }

    @Override
    protected NotTodayHandler createHandler(Skill skill, HandlerArgs args) {
        return new NotTodayHandler(
                skill, probabilityPerCent(args), args.intArg(1), initService
        );
    }
}
