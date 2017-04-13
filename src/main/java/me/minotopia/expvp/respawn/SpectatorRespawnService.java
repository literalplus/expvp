/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.respawn;

import com.google.inject.Inject;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.respawn.RespawnService;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Creates special effects on respawn using packets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public class SpectatorRespawnService implements RespawnService {
    private final List<UUID> pendingLeagueChanges = new ArrayList<>();
    private final TaskService tasks;
    private final SelectTreeMenu.Factory treeMenuFactory;
    private final KitService kitService;
    private final TalentPointService talentPoints;
    private final SpawnService spawns;

    @Inject
    public SpectatorRespawnService(PlayerInitService initService, TaskService tasks,
                                   SelectTreeMenu.Factory treeMenuFactory,
                                   KitService kitService, TalentPointService talentPoints,
                                   SpawnService spawns) {
        this.tasks = tasks;
        this.treeMenuFactory = treeMenuFactory;
        this.kitService = kitService;
        this.talentPoints = talentPoints;
        this.spawns = spawns;
        initService.registerDeInitHandler(this::purgePlayerLeagueChangeCache);
    }

    @Override
    public void startRespawnDelay(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        I18n.sendLoc(player, Format.result(Message.of("core!respawn.delay-start")));
        tasks.delayed(
                () -> {
                    I18n.sendLoc(player, Format.success(Message.of("core!respawn.delay-end")));
                    startRespawn(player);
                },
                Duration.ofSeconds(5)
        );
    }

    @Override
    public void startRespawn(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        spawns.teleportToSpawnIfPossible(player);
        if (talentPoints.getCurrentTalentPointCount(player) > 0) {
            treeMenuFactory.openForResearch(player)
                    .addCloseHandler(ignored -> startPostRespawn(player));
        } else {
            startPostRespawn(player);
        }
    }

    @Override
    public void startPostRespawn(Player player) {
        kitService.applyKit(player);
        if (pendingLeagueChanges.remove(player.getUniqueId())) {
            player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
            tasks.delayed(
                    () -> kitService.applyKit(player), Duration.ofSeconds(5)
            );
        }
    }

    @Override
    public void queueLeagueChange(UUID playerId) {
        pendingLeagueChanges.add(playerId);
    }

    private void purgePlayerLeagueChangeCache(Player player) {
        pendingLeagueChanges.remove(player.getUniqueId());
    }
}
