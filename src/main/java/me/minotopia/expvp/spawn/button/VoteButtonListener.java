/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.spawn.button;

import com.google.inject.Inject;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import me.minotopia.expvp.api.spawn.button.VoteButton;
import me.minotopia.expvp.api.spawn.button.VoteButtonService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Listens for button hits for map voting and linking/unlinking.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
public class VoteButtonListener implements Listener {
    private final VoteButtonService buttons;
    private final SpawnVoteService votes;
    private final DisplayNameService names;

    @Inject
    public VoteButtonListener(VoteButtonService buttons, SpawnVoteService votes, DisplayNameService names) {
        this.buttons = buttons;
        this.votes = votes;
        this.names = names;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onButtonHit(PlayerInteractEvent event) {
        if (isIrrelevant(event)) {
            return;
        }
        handleButtonHit(event, event.getPlayer(), event.getClickedBlock().getLocation());
    }

    private boolean isIrrelevant(PlayerInteractEvent event) {
        return (event.getAction() != Action.RIGHT_CLICK_BLOCK &&
                event.getAction() != Action.LEFT_CLICK_BLOCK) ||
                event.getClickedBlock() == null ||
                event.getClickedBlock().getType() != Material.STONE_BUTTON;
    }

    private void handleButtonHit(Cancellable event, Player player, Location location) {
        Optional<MapSpawn> linkingSession = buttons.getLinkingSession(player);
        if (linkingSession.isPresent()) {
            event.setCancelled(true);
            buttons.setButton(location, linkingSession.get());
            I18n.sendLoc(player, Format.success("admin!spawn.link.done"));
        } else {
            buttons.getButtonAt(location).ifPresent(castVote(event, player));
        }
    }

    private Consumer<VoteButton> castVote(Cancellable event, Player player) {
        return voteButton -> {
            event.setCancelled(true);
            MapSpawn spawn = voteButton.getSpawn();
            votes.castVoteFor(player.getUniqueId(), spawn);
            I18n.sendLoc(player, Format.success("spawn!vote.voted", names.displayName(spawn)));
        };
    }
}
