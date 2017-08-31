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

package me.minotopia.expvp.api.chat;

/**
 * Represents phases of chat handling. Phases with lower ordinal (that appear earlier in this enum)
 * are executed earlier.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-08-21
 */
public enum ChatPhase {
    /**
     * Initialising handlers, such as these who fetch chat prefixes from external APIs.
     */
    INITIALISING,
    /**
     * Checking handlers, that listen for specific message patterns for use in other subsystems and
     * drop matching messages.
     */
    CHECKING,
    /**
     * Filtering handlers, that block messages based on content.
     */
    FILTERING,
    /**
     * Censoring handlers, that replace forbidden (parts of) messages.
     */
    CENSORING,
    /**
     * Decorating handlers, that change messages to look better or be funnier.
     */
    DECORATING,
    /**
     * Forwarding handlers, that drop some messages and forward them to another subsystem.
     */
    FORWARDING,
    /**
     * Blocking handlers, that block messages from being sent to global chat based on external conditions.
     */
    BLOCKING,
    /**
     * Monitoring handlers, that just listen to messages but don't change them.
     */
    MONITORING
}
