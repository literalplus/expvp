/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        String strippedMessage = ChatColor.stripColor(coloredMessage);
        I18n.sendLoc(sender, "chat!pm.sender", receiver.getName(), coloredMessage);
        I18n.sendLoc(receiver, "chat!pm.receiver", sender.getName(), coloredMessage);
        server.getOnlinePlayers().stream()
                .filter(Permission.CHAT_SPY::has)
                .forEach(spy -> I18n.sendLoc(spy, "chat!pm.spy", sender.getName(), receiver.getName(), strippedMessage));
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
        if (recentMessages.stream().anyMatch(checkIsRepeated(message))) {
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
        return previous -> previous.equalsIgnoreCase(toCheck.getMessage()) ||
                StringUtils.getLevenshteinDistance(toCheck.getMessage(), previous) <= 2;
    }
}
