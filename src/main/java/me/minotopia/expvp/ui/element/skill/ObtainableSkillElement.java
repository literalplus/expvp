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

import li.l1t.common.i18n.Message;
import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.api.skill.SkillService;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.i18n.exception.I18nInternalException;
import me.minotopia.expvp.skill.meta.Skill;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.ui.menu.SkillTreeMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple menu element that represents a skill.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-07-22
 */
public class ObtainableSkillElement extends AbstractNodeElement<SkillTreeMenu> {
    private final ResearchService researchService;
    private final SkillService skills;
    private final DisplayNameService names;

    public ObtainableSkillElement(SkillTreeMenu menu, SimpleSkillTreeNode node, SkillService skills,
                                  ResearchService researchService, DisplayNameService names) {
        super(menu, node);
        this.researchService = researchService;
        this.skills = skills;
        this.names = names;
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return skills.createSkillIconFor(node, getMenu().getPlayer());
    }

    @Override
    public void handleMenuClick(InventoryClickEvent inventoryClickEvent, InventoryMenu menu) {
        Skill skill = node.getValue();
        if (skill == null) {
            throw new I18nInternalException("error!skill.missing-value");
        }
        Player player = menu.getPlayer();
        researchService.research(player, node);
        I18n.sendLoc(player, Message.of("core!research.success", names.displayName(skill)));
        getMenu().refresh();
    }

}
