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
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.api.skill.SkillService;
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

    public ObtainableSkillElement(SkillTreeMenu menu, SimpleSkillTreeNode node, SkillService skills,
                                  ResearchService researchService) {
        super(menu, node);
        this.researchService = researchService;
        this.skills = skills;
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
        getMenu().refresh();
    }

}
