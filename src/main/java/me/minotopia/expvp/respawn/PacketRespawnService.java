/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.respawn;

import com.comphenix.protocol.ProtocolManager;
import com.google.inject.Inject;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.handler.kit.KitService;
import me.minotopia.expvp.api.misc.PlayerInitService;
import me.minotopia.expvp.api.respawn.RespawnService;
import me.minotopia.expvp.api.score.TalentPointService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Creates special effects on respawn using packets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public class PacketRespawnService implements RespawnService {
    private final List<UUID> pendingLeagueChanges = new ArrayList<>();
    private final Map<UUID, Instant> respawnDelayTimeouts = new HashMap<>();
    private final BedPacketService packetService;
    private final TaskService tasks;
    private final SelectTreeMenu.Factory treeMenuFactory;
    private final KitService kitService;
    private final TalentPointService talentPoints;

    @Inject
    public PacketRespawnService(PlayerInitService initService, BedPacketService packetService, Plugin plugin,
                                ProtocolManager protocolManager, TaskService tasks, SelectTreeMenu.Factory treeMenuFactory,
                                KitService kitService, TalentPointService talentPoints) {
        this.packetService = packetService;
        this.tasks = tasks;
        this.treeMenuFactory = treeMenuFactory;
        this.kitService = kitService;
        this.talentPoints = talentPoints;
        initService.registerDeInitHandler(this::purgePlayerLeagueChangeCache);
        protocolManager.addPacketListener(new BedLeaveListener(plugin, this));
    }

    @Override
    public void startPreRespawn(Player player) {
        respawnDelayTimeouts.put(player.getUniqueId(), Instant.now().plusSeconds(5));
        I18n.sendLoc(player, Format.result(Message.of("core!respawn.delay-start")));
        packetService.sendIntoBed(player);
        tasks.delayed(
                () -> I18n.sendLoc(player, Format.resultSuccess(Message.of("core!respawn-end"))),
                Duration.ofSeconds(5)
        );
    }

    @Override
    public boolean hasDelayPassed(Player player) {
        Optional<Instant> timeout = Optional.ofNullable(respawnDelayTimeouts.get(player.getUniqueId()));
        return timeout.map(instant -> Instant.now().isAfter(instant)).orElse(true);
    }

    @Override
    public void startRespawn(Player player) {
        if (talentPoints.getCurrentTalentPointCount(player) > 0) {
            treeMenuFactory.openForResearch(player)
                    .addCloseHandler(ignored -> startPostRespawn(player));
        } else {
            startPostRespawn(player);
        }
    }

    @Override
    public void startPostRespawn(Player player) {
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
