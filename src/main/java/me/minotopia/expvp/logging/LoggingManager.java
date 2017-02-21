/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.logging;

import com.google.common.base.Preconditions;
import li.l1t.common.log.Log4JContextInitialiser;
import me.minotopia.expvp.EPPlugin;
import org.apache.logging.log4j.Logger;

/**
 * Takes care of initialising a custom Log4J2 context for Expvp logging. All members are static to
 * allow static construction of {@code LOGGER} fields.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-20
 */
public class LoggingManager {
    private static EPPlugin plugin;
    private static Log4JContextInitialiser initialiser = new Log4JContextInitialiser("exp", "logs");

    private LoggingManager() {

    }

    /**
     * Obtains a logger in the Expvp context for a specific class.
     *
     * @param clazz the class to use
     * @return the obtained logger
     * @see Log4JContextInitialiser#getLogger(Class)
     */
    public static Logger getLogger(Class<?> clazz) {
        return initialiser.getLogger(clazz);
    }

    /**
     * Sets the plugin used by the log manager. Once set, it cannot be re-set.
     *
     * @param newPlugin the plugin to use
     */
    public static void setPlugin(EPPlugin newPlugin) {
        Preconditions.checkState(plugin == null || newPlugin == null || newPlugin == plugin,
                "Cannot re-set singleton plugin!");
        plugin = newPlugin;
        initialiser.setPlugin(newPlugin);
    }

    /**
     * @return the initialiser used to initialise the Log4J2 context
     */
    public static Log4JContextInitialiser getInitialiser() {
        return initialiser;
    }
}
