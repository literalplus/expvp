/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.renderer.exception;

/**
 * Thrown if the rendering algorithm is not able to fit a tree into the inventory grid.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class RenderingException extends Exception {
    public RenderingException(String message) {
        super(message);
    }

    public RenderingException(String message, Throwable cause) {
        super(message, cause);
    }
}