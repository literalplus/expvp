/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
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
