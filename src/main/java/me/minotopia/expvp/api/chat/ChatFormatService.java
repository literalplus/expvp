/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.chat;

import li.l1t.common.i18n.Message;
import org.bukkit.entity.Player;

/**
 * Takes care of distributing chat messages.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-03
 */
public interface ChatFormatService {
    void sendToAll(Player player, String message);

    void sendToAll(Player player, Message message);
}
