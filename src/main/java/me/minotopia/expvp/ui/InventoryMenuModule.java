/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui;

import com.google.inject.AbstractModule;
import me.minotopia.expvp.ui.menu.EditNodeMenu;
import me.minotopia.expvp.ui.menu.SelectSkillMenu;
import me.minotopia.expvp.ui.menu.SelectTreeMenu;
import me.minotopia.expvp.ui.menu.SkillTreeMenu;

/**
 * Injector module for inventory menus.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-27
 */
public class InventoryMenuModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EditNodeMenu.Factory.class);
        bind(SelectSkillMenu.Factory.class);
        bind(SelectTreeMenu.Factory.class);
        bind(SkillTreeMenu.Factory.class);
    }
}
