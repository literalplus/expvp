/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.damage;

import me.minotopia.expvp.api.handler.SkillHandler;
import org.bukkit.entity.Player;

/**
 * A handler that gets called for damage events.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface DamageHandler extends SkillHandler {
    /**
     * Handles a player who has obtained the skill handled by this handler being hit by another player.
     *
     * @param victim  the player who was hit and has obtained this skill
     * @param culprit the player who dealt the hit
     */
    void handleVictim(Player victim, Player culprit);

    /**
     * Handles a player who has obtained the skill handled by this handler hitting another player.
     *
     * @param culprit the player who dealt the hit and has obtained this skill
     * @param victim  the player who was hit
     */
    void handleCulprit(Player culprit, Player victim);
}
