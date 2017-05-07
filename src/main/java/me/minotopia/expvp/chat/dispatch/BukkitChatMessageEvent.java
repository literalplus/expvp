/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat.dispatch;

import com.google.common.base.Preconditions;
import li.l1t.common.i18n.Message;
import me.minotopia.expvp.api.chat.ChatHandler;
import me.minotopia.expvp.api.chat.ChatMessageEvent;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * A chat message event that proxies a Bukkit event.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public class BukkitChatMessageEvent implements ChatMessageEvent {
    private final AsyncPlayerChatEvent bukkitEvent;
    private String message;
    private boolean cancelled;

    public BukkitChatMessageEvent(AsyncPlayerChatEvent bukkitEvent) {
        this.bukkitEvent = Preconditions.checkNotNull(bukkitEvent, "bukkitEvent");
        this.message = bukkitEvent.getMessage();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean tryDenyMessage(Message errorMessage, ChatHandler handler) {
        if (mayBypassFilters()) {
            notifyBypassOf(handler);
            return false;
        }
        respond(errorMessage);
        setCancelled(true);
        return true;
    }

    private void notifyBypassOf(ChatHandler handler) {
        if (handler != null) {
            respond(Format.success("core!chat.bypass-of", handler.getClass().getSimpleName()));
        }
    }

    @Override
    public void respond(Message message) {
        I18n.sendLoc(getPlayer(), message);
    }

    @Override
    public boolean mayBypassFilters() {
        return getPlayer().hasPermission("mtc.ignore");
    }

    @Override
    public void dropMessage() {
        setCancelled(true);
    }

    @Override
    public Player getPlayer() {
        return bukkitEvent.getPlayer();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getInitialMessage() {
        return bukkitEvent.getMessage();
    }

    @Override
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    @Override
    public boolean shouldContinueHandling() {
        return !cancelled;
    }
}
