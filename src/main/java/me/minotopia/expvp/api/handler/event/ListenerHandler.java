/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.event;

import me.minotopia.expvp.api.handler.SkillHandler;

/**
 * A skill handler that applies the effects of a skill by listening for events using the Bukkit event system and
 * modifying their outcome.
 *
 * <p>This is not called EventHandler because that name is already taken by {@link org.bukkit.event.EventHandler}, and
 * that would lead to confusion and fully-qualified names everywhere in code.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-02-22
 */
public interface ListenerHandler extends SkillHandler {
}
