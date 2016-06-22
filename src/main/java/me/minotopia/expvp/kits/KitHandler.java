/*
 * This file is part of ExPvP,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.kits;

import me.minotopia.expvp.EPPlugin;

import io.github.xxyy.common.games.kits.KitManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages kits for ExPvP.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-20
 */
public class KitHandler extends KitManager {
    private final EPPlugin plugin;

    public KitHandler(EPPlugin plugin) {
        super(new File(plugin.getDataFolder(), "kits"));
        this.plugin = plugin;
    }

    public Map<String, String> getObjectiveDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        return descriptions; //FIXME
    }
}
