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
