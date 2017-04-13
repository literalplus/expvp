/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.ui.element.skill;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.ui.menu.EPMenu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * Represents a skill in a menu. This kind of element can be clicked to edit it in an inventory
 * GUI.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-23
 */
public class EditableSkillElement extends AbstractNodeElement<EPMenu> {
    private final SkillService skills;
    private final BiConsumer<EPMenu, SimpleSkillTreeNode> clickHandler;

    public EditableSkillElement(EPMenu menu, SimpleSkillTreeNode node, SkillService skills,
                                BiConsumer<EPMenu, SimpleSkillTreeNode> clickHandler) {
        super(menu, node);
        this.skills = skills;
        this.clickHandler = clickHandler;
    }

    @Override
    public ItemStack draw(ElementHolder elementHolder) {
        Skill skill = getNode().getValue();
        ItemStackFactory factory = skills.createRawSkillIconFor(skill, true, getMenu().getPlayer());
        factory.lore(localize("admin!ui.skilledit.clicktoedit"));
        return factory.produce();
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        clickHandler.accept(getMenu(), getNode());
    }
}
