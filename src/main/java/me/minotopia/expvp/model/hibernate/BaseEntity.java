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
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-22
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created")
    private Instant creationDate = Instant.now();

    @Version
    @UpdateTimestamp
    @Column(nullable = false, name = "updated")
    private Instant lastUpdated = Instant.now();

    public Instant getCreationDate() {
        return creationDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
