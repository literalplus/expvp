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

package me.minotopia.expvp.handler.factory;

import com.google.inject.Inject;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.handler.factory.damage.*;
import me.minotopia.expvp.handler.factory.kit.EnchantKitHandlerFactory;
import me.minotopia.expvp.handler.factory.kit.NotchAppleKitHandlerFactory;
import me.minotopia.expvp.handler.factory.kit.PotionKitHandlerFactory;
import me.minotopia.expvp.handler.factory.kit.SimpleKitHandlerFactory;

/**
 * Wires handler factories into the handler factory graph.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-13
 */
public class HandlerFactoryWiring {
    private final PlayerInitService initService;

    @Inject
    public HandlerFactoryWiring(PlayerInitService initService) {
        this.initService = initService;
    }

    public void wire(EPHandlerFactoryGraph graph) {
        graph.kits().addChild(new SimpleKitHandlerFactory("simple"));
        graph.kits().addChild(new EnchantKitHandlerFactory("ench"));
        graph.kits().addChild(new PotionKitHandlerFactory("potion"));
        graph.kits().addChild(new NotchAppleKitHandlerFactory("notch"));
        graph.damages().addChild(new CulpritEffectHandlerFactory("culprit"));
        graph.damages().addChild(new EvilCulpritEffectHandlerFactory("evil-culprit"));
        graph.damages().addChild(new VictimEffectHandlerFactory("victim"));
        graph.damages().addChild(new VictimHealHandlerFactory("victim-heal"));
        graph.damages().addChild(new PoisonousArmorHandlerFactory("poison-armor"));
        graph.damages().addChild(new NotTodayHandlerFactory("nottoday", initService));
    }
}
