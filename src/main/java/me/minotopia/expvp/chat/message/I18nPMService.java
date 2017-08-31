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

package me.minotopia.expvp.chat.message;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.chat.AdFilterService;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.Permission;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.api.chat.message.PrivateMessage;
import me.minotopia.expvp.api.chat.message.ReplyService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nUserException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Takes care of dispatching private messages.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
@Singleton
public class I18nPMService implements PMService {
    private final ListMultimap<UUID, String> recentSentMessages = MultimapBuilder.hashKeys(50).arrayListValues(5).build();
    private final AdFilterService adFilterService;
    private final Server server;
    private final ReplyService replyService;

    @Inject
    public I18nPMService(AdFilterService adFilterService, Server server, ReplyService replyService) {
        this.adFilterService = adFilterService;
        this.server = server;
        this.replyService = replyService;
    }

    @Override
    public void sendMessage(CommandSender sender, CommandSender receiver, String message) {
        checkAd(message);
        PrivateMessage pm = createMessage(CommandHelper.getSenderId(sender), CommandHelper.getSenderId(receiver), message);
        checkSpam(pm);
        replyService.registerMessage(pm);
        String strippedMessage = ChatColor.stripColor(message);
        I18n.sendLoc(sender, "chat!pm.sender", receiver.getName(), message);
        I18n.sendLoc(receiver, "chat!pm.receiver", sender.getName(), message);
        server.getOnlinePlayers().stream()
                .filter(Permission.CHAT_SPY::has)
                .filter(isNotPartOfConversation(pm))
                .forEach(spy -> I18n.sendLoc(spy, "chat!pm.spy", sender.getName(), receiver.getName(), strippedMessage));
    }

    private Predicate<Player> isNotPartOfConversation(PrivateMessage message) {
        return player -> !message.getSenderId().equals(player.getUniqueId()) &&
                !message.getReceiverId().equals(player.getUniqueId());
    }

    private SimplePrivateMessage createMessage(UUID senderId, UUID receiverId, String message) {
        return new SimplePrivateMessage(senderId, receiverId, message);
    }

    private void checkAd(String message) {
        if (adFilterService.test(message)) {
            throw new I18nUserException("chat!pm.ad");
        }
    }

    private void checkSpam(PrivateMessage message) {
        List<String> recentMessages = recentSentMessages.get(message.getSenderId());
        if (recentMessages.stream().filter(checkIsRepeated(message)).count() >= 3) {
            throw new I18nUserException("chat!pm.spam");
        }
        if (recentMessages.size() >= 5) {
            for (int i = 4; i < recentMessages.size(); i++) {
                recentMessages.remove(i);
            }
        }
        recentMessages.add(message.getMessage());
    }

    private Predicate<String> checkIsRepeated(PrivateMessage toCheck) {
        return previous -> {
            String msg = toCheck.getMessage();
            return previous.equalsIgnoreCase(msg) ||
                    (msg.length() > 5 && StringUtils.getLevenshteinDistance(msg, previous) <= 2);
        };
    }
}
