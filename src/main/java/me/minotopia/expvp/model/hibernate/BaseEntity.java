/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.model.hibernate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence21.Column;
import javax.persistence21.MappedSuperclass;
import javax.persistence21.Version;
import java.io.Serializable;
import java.time.Instant;

/**
 * Provides a common base for entities that keep track of creation and last updated date.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant creationDate = Instant.now();

    @Version
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdated = Instant.now();

    public Instant getCreationDate() {
        return creationDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
