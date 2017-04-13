/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.i18n;

import li.l1t.common.intake.i18n.Message;
import li.l1t.common.util.CommandHelper;
import me.minotopia.expvp.logging.LoggingManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Single point of contact for localisation in Expvp.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-06
 */
public class I18n {
    private static final Logger LOGGER = LoggingManager.getLogger(I18n.class);
    private static MessageService messageService = new MessageService();
    private static Map<UUID, Locale> localeCache = new HashMap<>();

    private I18n() {

    }

    /**
     * @param dataFolder the data folder that contains custom resource bundles on the file system that may override the
     *                   defaults or provide custom data
     * @throws IOException if creation of given data folder fails
     */
    public static void setDataFolder(File dataFolder) throws IOException {
        Files.createDirectories(dataFolder.toPath());
        messageService.setDataFolder(dataFolder);
    }

    /**
     * Clears the resource bundle caches, forcing them to be reloaded when next used.
     *
     * <p><b>Note:</b> This does not clear the sender-&gt;locale cache, use {@link #clearLocaleOf(UUID)} for that.</p>
     */
    public static void clearCache() {
        messageService.clearCache();
    }

    /**
     * Returns the string value for given message key in given locale.
     *
     * @param locale the locale to query
     * @param key    the message key, according to {@link MessagePath} specifications.
     * @param args   the arguments for the message, which will be inserted using {@link java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(Locale locale, String key, Object... args) {
        if (messageService == null) {
            LOGGER.warn("Requested localisation for {} with {} before I18n was initialised",
                    key, Arrays.toString(args));
            return key + Arrays.toString(args);
        }
        return messageService.getMessage(locale, key, args);
    }

    /**
     * Returns the string value for given message key in given locale.
     *
     * @param senderId the unique id of the command sender whose locale to use from the cache
     * @param key      the message key, according to {@link MessagePath} specifications.
     * @param args     the arguments for the message, which will be inserted using {@link java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(UUID senderId, String key, Object... args) {
        return loc(getLocaleFor(senderId), key, args);
    }

    /**
     * Returns the string value for given message key in given locale.
     *
     * @param sender the command sender whose locale to use from the cache
     * @param key    the message key, according to {@link MessagePath} specifications.
     * @param args   the arguments for the message, which will be inserted using {@link java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(CommandSender sender, String key, Object... args) {
        return loc(CommandHelper.getSenderId(sender), key, args);
    }

    /**
     * Returns the string value for given message in given locale.
     *
     * @param locale  the locale to query
     * @param message the message object, with the key formatted according to {@link MessagePath} specifications, and
     *                the arguments being inserted back into the resolved message using {@link java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(Locale locale, Message message) {
        if (message.isStatic()) {
            return message.toString();
        }
        if (messageService == null) {
            LOGGER.warn("Requested localisation for {} before I18n was initialised", message);
            return message.toString();
        }
        return messageService.getMessage(locale, message);
    }

    /**
     * Returns the string value for given message in given locale.
     *
     * @param senderId the unique id of the command sender whose locale to use from the cache
     * @param message  the message object, with the key formatted according to {@link MessagePath} specifications, and
     *                 the arguments being inserted back into the resolved message using {@link
     *                 java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(UUID senderId, Message message) {
        return loc(getLocaleFor(senderId), message);
    }

    /**
     * Returns the string value for given message in given locale.
     *
     * @param sender  the command sender whose locale to use from the cache
     * @param message the message object, with the key formatted according to {@link MessagePath} specifications, and
     *                the arguments being inserted back into the resolved message using {@link java.text.MessageFormat}
     * @return requested string value, or a representation of the query if I18n hasn't been initialised yet
     */
    public static String loc(CommandSender sender, Message message) {
        return loc(CommandHelper.getSenderId(sender), message);
    }

    public static void setLocaleFor(UUID senderId, Locale locale) {
        localeCache.put(senderId, locale);
    }

    /**
     * @param senderId the unique id of the command sender to retrieve the locale for
     * @return the sender's preferred locale, or the system's default locale if none has been set
     */
    public static Locale getLocaleFor(UUID senderId) {
        return localeCache.getOrDefault(senderId, Locale.getDefault());
    }

    public static void clearLocaleOf(UUID senderId) {
        localeCache.remove(senderId);
    }

    /**
     * Sends given message to given sender, localised in the sender's locale.
     *
     * @param sender  the sender to send to in their locale
     * @param message the message to send
     */
    public static void sendLoc(CommandSender sender, Message message) {
        sender.sendMessage(loc(sender, message));
    }

    /**
     * Sends given message to given sender, localised in the sender's locale.
     *
     * @param sender    the sender to send to in their locale
     * @param key       the key of the message to send
     * @param arguments the arguments for the message
     */
    public static void sendLoc(CommandSender sender, String key, Object... arguments) {
        sendLoc(sender, Message.of(key, arguments));
    }
}
