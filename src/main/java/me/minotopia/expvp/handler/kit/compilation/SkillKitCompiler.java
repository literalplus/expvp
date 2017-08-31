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

package me.minotopia.expvp.handler.kit.compilation;

import com.google.inject.Inject;
import me.minotopia.expvp.api.handler.HandlerService;
import me.minotopia.expvp.api.handler.kit.KitHandler;
import me.minotopia.expvp.api.handler.kit.compilation.KitBaseline;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompiler;
import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

/**
 * Compiles kits based on players' skill sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SkillKitCompiler implements KitCompiler {
    private final HandlerService handlerService;
    private final KitBaseline baseline;

    @Inject
    public SkillKitCompiler(HandlerService handlerService, KitBaseline baseline) {
        this.handlerService = handlerService;
        this.baseline = baseline;
    }

    @Override
    public KitCompilation compile(Player player, PlayerData data) {
        SkillKitCompilation compilation = startCompilation(player, data);
        handlerService.handlersOfTypeStream(KitHandler.class, data)
                .forEach(handler -> handler.handle(compilation));
        return compilation;
    }

    private SkillKitCompilation startCompilation(Player player, PlayerData data) {
        SkillKitCompilation compilation = new SkillKitCompilation(player, data);
        baseline.baseline(compilation);
        return compilation;
    }
}
