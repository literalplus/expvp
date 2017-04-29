/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.Sender;
import me.minotopia.expvp.api.spawn.SpawnService;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * A command that teleports to spawn.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
@AutoRegister("spawn")
public class CommandSpawn {
    private final SpawnService spawns;

    @Inject
    public CommandSpawn(SpawnService spawns) {
        this.spawns = spawns;
    }

    @Command(aliases = "",
            desc = "cmd!spawn.root.desc")
    public void newSkill(@Sender Player player)
            throws IOException {
        spawns.teleportToSpawnIfPossible(player);
    }
}
