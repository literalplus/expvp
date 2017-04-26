/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.friend;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.api.friend.FriendRequestService;
import me.minotopia.expvp.api.friend.FriendService;

/**
 * Provides bindings for the friend module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-21
 */
public class FriendModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FriendService.class).to(HibernateFriendService.class);
        bind(FriendRequestService.class).to(HibernateFriendRequestService.class);
    }
}
