/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.api.handler.kit;

import me.minotopia.expvp.api.handler.SkillHandler;
import me.minotopia.expvp.api.handler.kit.compilation.KitCompilation;

/**
 * A skill handler which mutates a player's kit during compilation.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-17
 */
public interface KitHandler extends SkillHandler {
    /**
     * Handles the compilation of a kit element and mutates the state of the current element
     * according to this handler.
     *
     * @param compilation the compilation to handle
     * @throws IllegalStateException if the compilation does not have a current element
     * @see KitCompilation#current() for getting the current element with null check
     */
    void handle(KitCompilation compilation);
}
