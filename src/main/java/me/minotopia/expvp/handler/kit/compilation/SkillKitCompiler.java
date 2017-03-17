/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
        SkillKitCompilation compilation = new SkillKitCompilation(player, data);
        baseline.baseline(compilation);
        handlerService.handlersOfTypeStream(KitHandler.class, data)
                .forEach(handler -> handler.handle(compilation));
        return compilation;
    }
}
