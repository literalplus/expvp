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

package me.minotopia.expvp.spawn;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.chat.ComponentSender;
import li.l1t.common.chat.XyComponentBuilder;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.SpawnVoteService;
import me.minotopia.expvp.i18n.Format;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.Plurals;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keeps track of players' spawn votes using a map.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-15
 */
@Singleton
public class MapSpawnVoteService implements SpawnVoteService {
    private final Map<UUID, MapSpawn> currentVotes = new HashMap<>();
    private final SpawnService spawns;
    private final DisplayNameService names;

    @Inject
    public MapSpawnVoteService(SpawnService spawns, DisplayNameService names) {
        this.spawns = spawns;
        this.names = names;
    }

    @Override
    public Optional<MapSpawn> findCurrentlyWinningSpawn() {
        Map<MapSpawn, Long> spawnToVotes = currentVotes.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue, Collectors.counting()
                ));
        long maxVotes = spawnToVotes.values().stream().max(Comparator.naturalOrder()).orElse(0L);
        List<MapSpawn> bestMaps = spawnToVotes.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (bestMaps.size() == 1) {
            return bestMaps.stream().findFirst();
        } else if (!bestMaps.isEmpty()) {
            return Optional.of(randomSpawnFrom(bestMaps));
        }
        List<MapSpawn> allSpawns = this.spawns.getSpawns();
        if (allSpawns.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(randomSpawnFrom(allSpawns));
    }

    @Override
    public long findVoteCount(MapSpawn spawn) {
        return currentVotes.values().stream()
                .filter(spawn::equals)
                .count();
    }

    private MapSpawn randomSpawnFrom(List<MapSpawn> input) {
        int choice = RandomUtils.nextInt(input.size());
        return input.get(choice);
    }

    @Override
    public void castVoteFor(UUID playerId, MapSpawn spawn) {
        Preconditions.checkNotNull(spawn, "spawn");
        Preconditions.checkNotNull(playerId, "playerId");
        currentVotes.put(playerId, spawn);
    }

    @Override
    public Optional<MapSpawn> findVote(UUID playerId) {
        return Optional.ofNullable(currentVotes.get(playerId));
    }

    @Override
    public void retractVote(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        currentVotes.remove(playerId);
    }

    @Override
    public void resetAllVotes() {
        currentVotes.clear();
    }

    @Override
    public Map<UUID, MapSpawn> getCurrentVotes() {
        return ImmutableMap.copyOf(currentVotes);
    }

    @Override
    public void showCurrentVotesTo(CommandSender sender, boolean showVoteButton) {
        List<MapSpawn> spawns = this.spawns.getSpawns();
        if (spawns.isEmpty()) {
            I18n.sendLoc(sender, Format.internalError("spawn!vote.no-spawns"));
        } else if (currentVotes.isEmpty() && !showVoteButton) {
            I18n.sendLoc(sender, Format.userError("spawn!vote.no-votes"));
        } else {
            showVotesWithHeader(sender, showVoteButton, spawns);
        }
    }

    private void showVotesWithHeader(CommandSender sender, boolean showVoteButton, List<MapSpawn> spawns) {
        I18n.sendLoc(sender, Format.listHeader("spawn!vote.header"));
        spawns.forEach(spawn -> sendSpawnItem(sender, spawn, showVoteButton));
        I18n.sendLoc(sender, Format.result("spawn!vote.see-at-spawn"));
    }

    private void sendSpawnItem(CommandSender sender, MapSpawn spawn, boolean showVoteButton) {
        long voteCount = findVoteCount(spawn);
        if (voteCount == 0 && !showVoteButton) {
            return;
        }
        BaseComponent[] nameComponents = TextComponent.fromLegacyText(
                I18n.loc(sender, Format.listItem("spawn!vote.spawn-item",
                        names.displayName(spawn), Plurals.plural("spawn!vote.vote", voteCount))) + " "
        );
        if (showVoteButton) {
            BaseComponent[] buttonComponents = new XyComponentBuilder(I18n.loc(sender, "spawn!vote.spawn-button"))
                    .color(ChatColor.GREEN)
                    .command("/mv vote " + spawn.getId())
                    .create();
            ComponentSender.sendTo(sender, nameComponents, buttonComponents);
        } else {
            ComponentSender.sendTo(sender, nameComponents);
        }
    }
}
