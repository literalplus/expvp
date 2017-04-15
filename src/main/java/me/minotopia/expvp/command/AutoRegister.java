/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classifies that a command class should be automatically registered when the plugin is loaded.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {
    /**
     * @return the command name to register the annotated class as
     */
    String value();

    String[] aliases() default {};
}
