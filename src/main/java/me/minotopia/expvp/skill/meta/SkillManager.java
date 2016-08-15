/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.meta;

import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.player.PlayerDataManager;
import me.minotopia.expvp.skill.tree.SkillTreeNode;
import me.minotopia.expvp.yaml.AbstractYamlManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Manages skill metadata instances, keeping a list of all known ones by id and delegating
 * loading and saving.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class SkillManager extends AbstractYamlManager<Skill> {
    private final PlayerDataManager playerDataManager;

    /**
     * Creates a new skill manager.
     *
     * @param skillDirectory    the directory to load skills from
     * @param playerDataManager the manager to get skill obtainment state from
     */
    public SkillManager(File skillDirectory, PlayerDataManager playerDataManager) {
        super(skillDirectory);
        this.playerDataManager = playerDataManager;
        loadAll();
    }

    @Override
    protected SkillLoader createLoader() {
        return new SkillLoader(this);
    }

    public ItemStack createSkillIconFor(SkillTreeNode<?> node, Player player) {
        Skill skill = node.getValue();
        if(skill == null) {
            return new ItemStack(Material.BARRIER);
        }
        boolean obtained = playerDataManager.hasObtainedSkill(player.getUniqueId(), skill);
        ItemStackFactory icon = new ItemStackFactory(skill.getIconStack());
        icon.displayName(findColoredDisplayNameFor(skill, obtained));
        if(!doesParentPermitObtainment(node, player)) {
            icon.lore("\n§cDu kannst diesen Skill\n§cnoch nicht erforschen!");
        } else if (obtained) {
            icon.glow().lore("\n§aDu hast diesen Skill\n§abereits erforscht.");
        } else {
            icon.lore("\n§eDu kannst diesen Skill\n§efür " + skill.getBookCost() + " Skillpunkte erforschen.");
        }
        return icon.produce();
    }

    private boolean doesParentPermitObtainment(SkillTreeNode<?> node, Player player) {
        SkillTreeNode<?> parent = node.getParent();
        return parent == null ||
                (parent.getValue() != null &&
                        playerDataManager.hasObtainedSkill(player.getUniqueId(), parent.getValue()));
    }

    private String findColoredDisplayNameFor(Skill skill, boolean obtained) {
        return (obtained ? "§a" : "§c") + skill.getDisplayName();
    }
}
