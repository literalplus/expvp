/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Stores the mutable state of an ongoing kit compilation.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-09-17
 */
public interface KitCompilation {
    /**
     * @return the player this compilation is for
     */
    Player getTargetPlayer();

    /**
     * @return the player data of the compilation target
     */
    PlayerData getTargetData();

    /**
     * @return the current state of the result, ready to be put into an inventory
     */
    Map<Integer, KitElement> getResult();

    /**
     * @param slotId the slot id to get the builder for
     * @param type   the expected material for the builder
     * @return the builder in given slot, or a new builder if there is a builder of a different type in given slot
     */
    KitElementBuilder slot(int slotId, Material type);

    KitElement snapshotOf(int slotId);
}
