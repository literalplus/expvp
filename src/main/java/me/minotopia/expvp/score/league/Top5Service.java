/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.score.league;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.task.TaskService;
import me.minotopia.expvp.api.model.PlayerData;
import me.minotopia.expvp.model.player.HibernatePlayerTopRepository;

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
    private static final Duration expiryDuration = Duration.ofSeconds(30);
    private final HibernatePlayerTopRepository topRepository;
    private final TaskService tasks;
    private final AtomicBoolean updateScheduled = new AtomicBoolean();
    private Set<UUID> topFive = null;
    private Instant expiryTime = Instant.MIN;

    @Inject
    public Top5Service(HibernatePlayerTopRepository topRepository, TaskService tasks) {
        this.topRepository = topRepository;
        this.tasks = tasks;
        scheduleUpdateIfNecessary();
    }

    public boolean isInTopFive(PlayerData target) {
        if (topFive == null) {
            return false;
        }
        scheduleUpdateIfNecessary();
        return topFive.contains(target.getUniqueId());
    }

    private void scheduleUpdateIfNecessary() {
        if (!updateScheduled.get() && expiryTime.isBefore(Instant.now())) {
            if (updateScheduled.compareAndSet(false, true)) {
                tasks.async(this::updateCache);
            }
        }
    }

    private void updateCache() {
        Set<UUID> entries = topRepository.findTopNByExp(5).stream()
                .map(PlayerData::getUniqueId)
                .collect(Collectors.toSet());
        topFive = Collections.unmodifiableSet(entries);
        expiryTime = Instant.now().plus(expiryDuration);
    }
}
