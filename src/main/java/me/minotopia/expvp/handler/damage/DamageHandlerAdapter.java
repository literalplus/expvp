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

package me.minotopia.expvp.handler.damage;

import me.minotopia.expvp.api.handler.damage.DamageHandler;
import me.minotopia.expvp.handler.HandlerAdapter;
import me.minotopia.expvp.skill.meta.Skill;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Adapter for damage handlers with no-op implementations. Provides a mechanism for probability checking through {@link
 * #isChanceMet()}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public abstract class DamageHandlerAdapter extends HandlerAdapter implements DamageHandler {
    protected static final Random RANDOM = new Random();
    private final int probabilityPerCent;

    public DamageHandlerAdapter(Skill skill, int probabilityPerCent) {
        super(skill);
        this.probabilityPerCent = probabilityPerCent;
    }

    @Override
    public void handleVictim(Player victim, Player culprit) {

    }

    @Override
    public void handleCulprit(Player culprit, Player victim) {

    }

    protected boolean isChanceMet() {
        return RANDOM.nextInt(100) > probabilityPerCent;
    }
}
