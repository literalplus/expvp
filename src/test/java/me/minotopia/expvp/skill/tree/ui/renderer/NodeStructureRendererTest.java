/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.renderer;

import li.l1t.common.inventory.SlotPosition;
import li.l1t.common.inventory.gui.holder.TemplateElementHolder;
import li.l1t.common.test.util.MockHelper;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.SkillTreeNode;
import me.minotopia.expvp.skill.tree.ui.element.SimpleSkillElement;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the node structure renderer.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class NodeStructureRendererTest {
    private static final ItemStack MELON_STACK = new ItemStack(Material.MELON_BLOCK);

    @Test
    public void render__simple_root_only() throws Exception {
        //given
        MockHelper.mockServer();
        SkillTree tree = new SkillTree("test-tree");
        tree.setIconStack(MELON_STACK);
        TreeStructureRenderer treeRenderer = new TreeStructureRenderer(tree);
        NodeStructureRenderer nodeRenderer = new NodeStructureRenderer(treeRenderer);
        //when
        nodeRenderer.render();
        //then
        assertNodeAtPositionIs(tree, SlotPosition.ofXY(0, 3), treeRenderer);
    }

    private void assertNodeAtPositionIs(SkillTreeNode<?> expected, SlotPosition position,
                                       TreeStructureRenderer treeRenderer) {
        TemplateElementHolder template = treeRenderer.getTemplate();
        SimpleSkillElement element = (SimpleSkillElement) template.getElement(position.toSlotId());
        assertThat("wrong element at pos " + position, element.getNode(), is(expected));
    }

}
