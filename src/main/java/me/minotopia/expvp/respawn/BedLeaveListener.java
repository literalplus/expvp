/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.respawn;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import li.l1t.common.intake.i18n.Message;
import me.minotopia.expvp.api.respawn.RespawnService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Listens for bed leave actions and consults the respawn service on what to do next.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
class BedLeaveListener extends PacketAdapter {
    private final RespawnService respawnService;
    private final BedPacketService packetService;

    public BedLeaveListener(Plugin plugin, RespawnService respawnService, BedPacketService packetService) {
        super(plugin, PacketType.Play.Client.ENTITY_ACTION);
        this.respawnService = respawnService;
        this.packetService = packetService;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        EnumWrappers.PlayerAction action = event.getPacket().getPlayerActions().read(0);
        if (action == EnumWrappers.PlayerAction.STOP_SLEEPING) {
            Entity entity = event.getPacket().getEntityModifier(event).read(0);
            if (entity instanceof Player) {
                handleBedLeave((Player) entity, event);
            }
        }
    }

    private void handleBedLeave(Player player, PacketEvent event) {
        if (respawnService.hasDelayPassed(player)) {
            //respawnService.startRespawn(player);
            if (RandomUtils.nextInt(0, 2) == 1) {
                player.closeInventory();
                player.sendMessage("cInv");
            } else {
                packetService.forceBedExit(player);
            }
        } else {
            I18n.sendLoc(player, Format.userError(Message.of("core!respawn.delay-wait")));
            event.setCancelled(true);
        }
    }
}
