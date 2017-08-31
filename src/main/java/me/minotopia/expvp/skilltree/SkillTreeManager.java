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

package me.minotopia.expvp.skilltree;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import li.l1t.common.util.inventory.ItemStackFactory;
import me.minotopia.expvp.api.i18n.DisplayNameService;
import me.minotopia.expvp.api.inject.DataFolder;
import me.minotopia.expvp.i18n.I18n;
import me.minotopia.expvp.skill.meta.SkillManager;
import me.minotopia.expvp.yaml.AbstractYamlManager;
import me.minotopia.expvp.yaml.YamlLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Optional;

/**
 * Manages in-memory skill trees and delegates loading.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-23
 */
@Singleton
public class SkillTreeManager extends AbstractYamlManager<SkillTree> {
    private final SkillManager skillManager;
    private final DisplayNameService names;

    @Inject
    SkillTreeManager(@DataFolder File dataFolder, SkillManager skillManager, DisplayNameService names) {
        super(new File(dataFolder, "skilltrees"));
        this.skillManager = skillManager;
        this.names = names;
        loadAll();
    }

    /**
     * {@inheritDoc} <p>Also {@link #populate() populates} all loaded trees with skill
     * metadata.</p>
     */
    @Override
    public void loadAll() {
        super.loadAll();
        populate();
    }

    @Override
    protected YamlLoader<SkillTree> createLoader() {
        return new SkillTreeLoader(this);
    }

    /**
     * Populates all loaded skill trees with skill metadata from the skill manager associated with
     * this skill tree manager.
     */
    public void populate() {
        getAll().forEach(tree ->
                tree.forEachNode(node ->
                        node.setValue(skillManager.get(node.getSkillId()) //null if not found
                        )
                ));
    }

    public ItemStack createIconFor(SkillTree tree, Player receiver) {
        ItemStack baseStack = Optional.ofNullable(tree)
                .map(SkillTree::getIconStack)
                .flatMap(Optional::ofNullable)
                .orElseGet(() -> new ItemStack(Material.BARRIER));
        return new ItemStackFactory(baseStack)
                .displayName(I18n.loc(receiver, names.displayName(tree)))
                .lore(I18n.loc(receiver, names.description(tree)))
                .produce();
    }
}
