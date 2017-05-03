/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.chat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import li.l1t.common.chat.AdFilterService;
import li.l1t.common.chat.CapsFilterService;
import me.minotopia.expvp.api.chat.ChatDispatcher;
import me.minotopia.expvp.api.chat.ChatFormatService;
import me.minotopia.expvp.chat.dispatch.SimpleChatDispatcher;
import me.minotopia.expvp.chat.format.I18nChatFormatService;
import me.minotopia.expvp.chat.handler.*;
import me.minotopia.expvp.util.SessionProvider;
import org.bukkit.Server;

/**
 * Provides the dependency wiring for the chat module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-03
 */
public class ChatModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ChatListener.class);
        bind(ChatFormatService.class).to(I18nChatFormatService.class);
        bind(ChatConfig.class);
        bind(ChatColorHandler.class);
        bind(LagMessageHandler.class);
        bind(RepeatedMessageHandler.class);
    }

    @Provides
    public AdFilterService adFilterService(ChatConfig config) {
        AdFilterService adFilterService = new AdFilterService();
        config.configure(adFilterService);
        return adFilterService;
    }

    @Provides
    @Singleton
    public AdFilterHandler adFilterHandler(ChatConfig config, AdFilterService adFilterService, ChatFormatService chatFormatService, Server server) {
        return new AdFilterHandler(adFilterService, chatFormatService, server, config.getMockMessageCount());
    }

    @Provides
    public CapsFilterService capsFilterService(ChatConfig config) {
        CapsFilterService capsFilterService = new CapsFilterService();
        config.configure(capsFilterService);
        return capsFilterService;
    }

    @Provides
    @Singleton
    public CapsFilterHandler capsFilterHandler(CapsFilterService capsFilterService) {
        return new CapsFilterHandler(capsFilterService);
    }

    @Provides
    @Singleton
    public ChatDispatcher chatDispatcher(
            ChatFormatService formatService, SessionProvider sessionProvider,
            AdFilterHandler ads, CapsFilterHandler caps, ChatColorHandler color, LagMessageHandler lag,
            RepeatedMessageHandler repeated
    ) {
        return new SimpleChatDispatcher(formatService, sessionProvider)
                .registerHandler(ads).registerHandler(caps)
                .registerHandler(color).registerHandler(lag)
                .registerHandler(repeated);
    }
}
