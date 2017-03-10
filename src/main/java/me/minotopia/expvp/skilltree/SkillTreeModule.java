/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skilltree;

import com.google.inject.AbstractModule;

/**
 * Provides the dependency wiring for the skill tree module.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-09
 */
public class SkillTreeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SkillTreeManager.class);
    }
}
