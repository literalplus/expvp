/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2017.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.api.service.ResearchService;
import me.minotopia.expvp.skilltree.SkillTreeNode;
import me.minotopia.expvp.yaml.AbstractYamlManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Manages skill metadata instances, keeping a list of all known ones by id and delegating loading
 * and saving.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
@Singleton
public class SkillManager extends AbstractYamlManager<Skill> {
    private final ResearchService obtainmentService;

    @Inject
    public SkillManager(@DataFolder File dataFolder, ResearchService obtainmentService) {
        super(new File(dataFolder, "skills"));
        this.obtainmentService = Preconditions.checkNotNull(obtainmentService, "obtainmentService");
        loadAll();
    }

    @Override
    protected SkillLoader createLoader() {
        return new SkillLoader(this);
    }

    public ItemStack createSkillIconFor(SkillTreeNode<?> node, Player player) {
        Skill skill = node.getValue();
        if (skill == null) {
            return new ItemStack(Material.BARRIER);
        }
        boolean obtained = obtainmentService.has(player.getUniqueId(), skill);
        ItemStackFactory icon = createRawSkillIconFor(skill, obtained);
        if (obtained) {
            icon.glow().lore("\n§aDu hast diesen Skill\n§abereits erforscht.");
        } else if (!doesParentPermitObtainment(node, player)) {
            icon.lore("\n§cDu kannst diesen Skill\n§cnoch nicht erforschen!");
        } else {
            icon.lore("\n§eDu kannst diesen Skill\n§efür " + skill.getBookCost() + " Skillpunkte erforschen.");
        }
        return icon.produce();
    }

    public ItemStackFactory createRawSkillIconFor(Skill skill, boolean obtained) {
        if (skill == null) {
            return new ItemStackFactory(Material.BARRIER);
        }
        ItemStackFactory icon = new ItemStackFactory(skill.getDisplayStack());
        icon.displayName(findColoredDisplayNameFor(skill, obtained));
        icon.amount(skill.getBookCost());
        return icon;
    }

    private boolean doesParentPermitObtainment(SkillTreeNode<?> node, Player player) {
        SkillTreeNode<?> parent = node.getParent();
        return parent == null ||
                (parent.getValue() != null &&
                        obtainmentService.has(player.getUniqueId(), parent.getValue()));
    }

    private String findColoredDisplayNameFor(Skill skill, boolean obtained) {
        return (obtained ? "§a" : "§c") + skill.getDisplayName();
    }
}
