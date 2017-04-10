/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.respawn;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.inject.Inject;
import li.l1t.common.exception.InternalException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides an API for sending bed-related packets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
class BedPacketService {
    private final ProtocolManager protocolManager;

    @Inject
    BedPacketService(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public void sendIntoBed(Player player) {
        sendIntoBed(player, player.getLocation());
    }

    @SuppressWarnings("deprecation")
    public void sendIntoBed(Player player, Location bedLocation) {
        player.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte) 0);
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BED);
        packet.getEntityModifier(player.getWorld()).write(0, player);
        packet.getBlockPositionModifier().write(0, new BlockPosition(bedLocation.toVector()));
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new InternalException("Error sending bed packet", e);
        }
    }

    public void forceBedExit(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packet.getEntityModifier(player.getWorld()).write(0, player);
        packet.getIntegers().write(0, 2);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new InternalException("Error sending bed exit packet", e);
        }
    }
}
