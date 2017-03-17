/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.handler.kit.compilation;

import li.l1t.common.collections.Pair;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;
import me.minotopia.expvp.api.handler.kit.compilation.KitElement;
import me.minotopia.expvp.api.handler.kit.compilation.KitElementBuilder;
import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A kit compilation for kits based on players' skill sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public class SkillKitCompilation implements KitCompilation {
    private final Player player;
    private final PlayerData playerData;
    private final Map<Integer, KitElementBuilder> slotMap = new HashMap<>();

    public SkillKitCompilation(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
    }

    @Override
    public Player getTargetPlayer() {
        return player;
    }

    @Override
    public PlayerData getTargetData() {
        return playerData;
    }

    @Override
    public Map<Integer, KitElement> getResult() {
        return slotMap.entrySet().stream()
                .map(e -> Pair.pairOf(e.getKey(), e.getValue().build()))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Override
    public KitElementBuilder slot(int slotId, Material type) {
        return slotMap.computeIfAbsent(slotId, id -> new ItemKitElementBuilder(type, id));
    }

}
