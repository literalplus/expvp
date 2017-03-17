/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit.compilation;

/**
 * Baselines a kit compilation with some default items.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-17
 */
public interface KitBaseline {
    /**
     * Baselines given compilation with some default items.
     *
     * @param compilation the compilation to baseline
     */
    void baseline(KitCompilation compilation);
}
