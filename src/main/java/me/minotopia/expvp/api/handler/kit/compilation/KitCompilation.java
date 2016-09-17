/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

import me.minotopia.expvp.api.model.PlayerData;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Stores the mutable state of an ongoing kit compilation.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
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
    List<KitElement> getResult(); //TODO: clone elements; this is not the place i know but ill forget

    /**
     * @return the element the compilation is currently building, or null for none
     */
    KitElementBuilder getCurrentElement();

    /**
     * @return the element the compilation is currently building
     * @throws IllegalStateException if there is no current element
     */
    KitElementBuilder current();

    /**
     * Sets given element as the current element of this compilation.
     *
     * @param nextElement the next element
     */
    void next(KitElementBuilder nextElement);

    /**
     * Returns whether this compilation is finished. Finished compilations may no longer be
     * modified.
     *
     * @return whether this compilation is finished
     */
    boolean isFinished();
}
