/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.renderer.exception;

/**
 * Thrown if the rendering algorithm encounters an unexpected error and cannot continue.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-22
 */
public class RenderingAlgorithmException extends RuntimeException {
    public RenderingAlgorithmException(String message) {
        super(message);
    }

    public RenderingAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
