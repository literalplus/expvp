/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler;

import me.minotopia.expvp.EPPlugin;

/**
 * Takes care of applying the effects of a skill to a player.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public interface SkillHandler extends HandlerSpecNode {
    /**
     * Enables this handler, preparing everything necessary for it to work and be called.
     *
     * @param plugin the plugin enabling the handler
     */
    void enable(EPPlugin plugin);

    /**
     * Disables this handler, cleaning up the mess it made.
     *
     * @param plugin the plugin disabling the handler
     */
    void disable(EPPlugin plugin);
}
