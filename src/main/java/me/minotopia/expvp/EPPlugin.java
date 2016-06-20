/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp;

import io.github.xxyy.common.xyplugin.SqlXyGamePlugin;

/**
 * Plugin class for interfacing with the Bukkit API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-20
 */
public class EPPlugin extends SqlXyGamePlugin {
    @Override
    protected void initConfig() {
        //TODO
    }

    @Override
    public String getChatPrefix() {
        return null; //TODO
    }

    @Override
    public void setError(String s, String s1) {
        //TODO
    }

    @Override
    public void disable() {
        //TODO
    }

    @Override
    public void enable() {
        //TODO
        System.out.println("it twerks!");
    }
}
