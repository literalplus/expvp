/*
 * Expvp Minecraft game mode
 * Copyright (C) 2016-2017 Philipp Nowak (https://github.com/xxyy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
