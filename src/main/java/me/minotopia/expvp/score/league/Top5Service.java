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

package me.minotopia.expvp.score.league;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.logging.LoggingManager;
import me.minotopia.expvp.model.player.HibernatePlayerTopRepository;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Caches top five players for league changes.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-05
 */
@Singleton
public class Top5Service {
    private static final Logger LOGGER = LoggingManager.getLogger(Top5Service.class);
    private static final Duration expiryDuration = Duration.ofSeconds(30);
    private final HibernatePlayerTopRepository topRepository;
    private final TaskService tasks;
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);
    private Set<UUID> topFive = null;
    private Instant expiryTime = Instant.MIN;

    @Inject
    public Top5Service(HibernatePlayerTopRepository topRepository, TaskService tasks) {
        this.topRepository = topRepository;
        this.tasks = tasks;
        tasks.async(this::updateCache);
    }

    public boolean isInTopFive(PlayerData target) {
        if (topFive == null) {
            LOGGER.warn("No top five data yet for " + target.getUniqueId());
            return false;
        }
        scheduleUpdateIfNecessary();
        return topFive.contains(target.getUniqueId());
    }

    private void scheduleUpdateIfNecessary() {
        if (!updateScheduled.get() && expiryTime.isBefore(Instant.now())) {
            updateScheduled.set(true);
            tasks.async(this::updateCache);
        }
    }

    private void updateCache() {
        Set<UUID> entries = topRepository.findTopNByExp(5).stream()
                .map(PlayerData::getUniqueId)
                .collect(Collectors.toSet());
        topFive = Collections.unmodifiableSet(entries);
        expiryTime = Instant.now().plus(expiryDuration);
        updateScheduled.set(false);
    }
}
