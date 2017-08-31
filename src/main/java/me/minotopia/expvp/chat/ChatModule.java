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

package me.minotopia.expvp.chat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import li.l1t.common.chat.AdFilterService;
import li.l1t.common.chat.CapsFilterService;
import me.minotopia.expvp.api.chat.ChatDispatcher;
import me.minotopia.expvp.api.chat.ChatFormatService;
import me.minotopia.expvp.api.chat.GlobalMuteService;
import me.minotopia.expvp.api.chat.message.PMService;
import me.minotopia.expvp.api.chat.message.ReplyService;
import me.minotopia.expvp.chat.dispatch.SimpleChatDispatcher;
import me.minotopia.expvp.chat.format.I18nChatFormatService;
import me.minotopia.expvp.chat.glomu.SimpleGlobalMuteService;
import me.minotopia.expvp.chat.handler.*;
import me.minotopia.expvp.chat.message.I18nPMService;
import me.minotopia.expvp.chat.message.MapReplyService;
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
        bind(ReplyService.class).to(MapReplyService.class);
        bind(PMService.class).to(I18nPMService.class);
        bind(GlobalMuteService.class).to(SimpleGlobalMuteService.class);
    }

    @Provides
    @Singleton
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
    @Singleton
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
            RepeatedMessageHandler repeated, GlobalMuteHandler glomu
    ) {
        return new SimpleChatDispatcher(formatService, sessionProvider)
                .registerHandler(ads).registerHandler(caps)
                .registerHandler(color).registerHandler(lag)
                .registerHandler(repeated).registerHandler(glomu);
    }
}
