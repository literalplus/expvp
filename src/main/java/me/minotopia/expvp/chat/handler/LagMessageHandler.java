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

package me.minotopia.expvp.chat.handler;


import com.google.inject.Inject;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.api.chat.ChatPhase;

import java.util.regex.Pattern;

/**
 * Blocks messages claiming there's server lag.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-21
 */
public class LagMessageHandler extends AbstractChatHandler {
    private static final Pattern LAG_PATTERN = Pattern.compile("\\b([lL]+[aA4]+[gG]+)\\b");

    @Inject
    public LagMessageHandler() {
        super(ChatPhase.FILTERING);
    }

    @Override
    public void handle(ChatMessageEvent evt) {
        if (!containsLagMessage(evt)) {
            return;
        }

        evt.tryDenyMessage(Message.of("chat!lag.warn"), this);
    }

    private boolean containsLagMessage(ChatMessageEvent evt) {
        return LAG_PATTERN.matcher(evt.getMessage()).find();
    }
}
