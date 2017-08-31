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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.misc.XyLocation;
import li.l1t.common.util.config.YamlHelper;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.api.spawn.MapSpawn;
import me.minotopia.expvp.api.spawn.SpawnService;
import me.minotopia.expvp.api.spawn.button.VoteButton;
import me.minotopia.expvp.api.spawn.button.VoteButtonService;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides access to vote button data stored in a YAML file.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-19
 */
@Singleton
public class YamlVoteButtonService implements VoteButtonService {
    private static final Logger LOGGER = LoggingManager.getLogger(YamlVoteButtonService.class);
    private final YamlConfiguration config;
    private final SpawnService spawnService;
    private final Map<Location, YamlVoteButton> buttons = new HashMap<>();
    private final Map<UUID, MapSpawn> linkingSessions = new HashMap<>();
    private final File configFile;

    @Inject
    public YamlVoteButtonService(@DataFolder File dataFolder, SpawnService spawnService) {
        this.spawnService = spawnService;
        configFile = new File(dataFolder, "vote-buttons.stor.yml");
        this.config = tryLoadConfig(dataFolder, configFile);
        loadConfig();
    }

    private YamlConfiguration tryLoadConfig(File dataFolder, File file) {
        try {
            return YamlHelper.load(file, false);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            LOGGER.warn("Unable to load vote buttons, making a backup.");
            try {
                Files.copy(file.toPath(), new File(dataFolder, "vote-buttons.stor.yml.bkp").toPath());
            } catch (IOException e1) {
                LOGGER.warn("Failed to make a backup.", e1);
            }
            return new YamlConfiguration();
        }
    }

    private void loadConfig() {
        config.getValues(false).entrySet().stream()
                .map(e -> new HashMap.SimpleEntry<>(
                        YamlVoteButton.deserializeLocation(e.getKey()), String.valueOf(e.getValue())
                )).forEach(e ->
                spawnService.getSpawnById(e.getValue())
                        .map(spawn -> new YamlVoteButton(e.getKey(), spawn))
                        .ifPresent(this::set)
        );
    }

    private void set(YamlVoteButton button) {
        buttons.put(button.getLocation(), button);
    }

    private void write(YamlVoteButton button) {
        button.saveTo(config);
    }

    private void saveConfig() {
        buttons.values().forEach(this::write);
        try {
            config.save(configFile);
        } catch (IOException e) {
            LOGGER.warn("Failed to save yaml buttons", e);
        }
    }

    @Override
    public void setButton(Location location, MapSpawn spawn) {
        Preconditions.checkNotNull(location, "location");
        if (spawn != null) {
            set(new YamlVoteButton(XyLocation.of(location), spawn));
        } else {
            removeButton(location);
        }
        saveConfig();
    }

    @Override
    public void removeButton(Location location) {
        Preconditions.checkNotNull(location, "location");
        XyLocation xyLoc = XyLocation.of(location);
        buttons.remove(xyLoc);
        config.set(YamlVoteButton.serializeLocation(location), null);
    }

    @Override
    public Optional<VoteButton> getButtonAt(Location location) {
        return Optional.ofNullable(buttons.get(XyLocation.of(location)));
    }

    @Override
    public void startLinkingSession(Player player, MapSpawn spawn) {
        Preconditions.checkNotNull(player, "player");
        linkingSessions.put(player.getUniqueId(), spawn);
    }

    @Override
    public Optional<MapSpawn> getLinkingSession(Player player) {
        Preconditions.checkNotNull(player, "player");
        return Optional.ofNullable(linkingSessions.remove(player.getUniqueId()));
    }
}
