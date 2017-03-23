/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.factory;

import me.minotopia.expvp.handler.factory.damage.CulpritEffectHandlerFactory;
import me.minotopia.expvp.handler.factory.damage.PoisonousArmorHandlerFactory;
import me.minotopia.expvp.handler.factory.damage.VictimEffectHandlerFactory;
import me.minotopia.expvp.handler.factory.damage.VictimHealHandlerFactory;
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
    public void wire(EPHandlerFactoryGraph graph) {
        graph.kits().addChild(new SimpleKitHandlerFactory("simple"));
        graph.kits().addChild(new EnchantKitHandlerFactory("ench"));
        graph.kits().addChild(new PotionKitHandlerFactory("potion"));
        graph.kits().addChild(new NotchAppleKitHandlerFactory("notch"));
        graph.damages().addChild(new CulpritEffectHandlerFactory("culprit"));
        graph.damages().addChild(new VictimEffectHandlerFactory("victim"));
        graph.damages().addChild(new VictimHealHandlerFactory("victim-heal"));
        graph.damages().addChild(new PoisonousArmorHandlerFactory("poison-armor"));
    }
}
