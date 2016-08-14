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
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
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
    public void render__root_only() throws Exception {
        //given
        MockHelper.mockServer();
        SkillTree tree = givenASkillTree();
        NodeStructureRenderer renderer = givenANodeRenderer(tree);
        //when
        renderer.render();
        //then
        thenTheNodeAtIs(SlotPosition.ofXY(0, 3), tree, renderer);
    }

    public NodeStructureRenderer givenANodeRenderer(SkillTree tree) {
        TreeStructureRenderer treeRenderer = new TreeStructureRenderer(tree);
        return new NodeStructureRenderer(treeRenderer);
    }

    public SkillTree givenASkillTree() {
        SkillTree tree = new SkillTree("test-tree");
        tree.setIconStack(MELON_STACK);
        return tree;
    }

    @Test
    public void render__single_child() throws Exception {
        //given
        MockHelper.mockServer();
        SkillTree tree = givenASkillTree();
        SimpleSkillTreeNode child = tree.createChild("some-skill");
        NodeStructureRenderer renderer = givenANodeRenderer(tree);
        //when
        renderer.render();
        //then
        thenTheNodeAtIs(SlotPosition.ofXY(0, 3), tree, renderer);
        thenTheNodeAtIs(SlotPosition.ofXY(2, 3), child, renderer);
        thenThereIsAPlaceholderAt(SlotPosition.ofXY(1, 3), renderer);
    }

    @Test
    public void render__three_children() throws Exception {
        //given
        MockHelper.mockServer();
        SkillTree tree = givenASkillTree();
        SimpleSkillTreeNode child1 = tree.createChild("first-skill");
        SimpleSkillTreeNode child2 = tree.createChild("second-skill");
        SimpleSkillTreeNode child3 = tree.createChild("third-skill");
        NodeStructureRenderer renderer = givenANodeRenderer(tree);
        //when
        renderer.render();
        //then
        thenTheNodeAtIs(SlotPosition.ofXY(0, 3), tree, renderer);
        thenTheNodeAtIs(SlotPosition.ofXY(2, 1), child1, renderer);
        thenTheNodeAtIs(SlotPosition.ofXY(2, 3), child2, renderer);
        thenTheNodeAtIs(SlotPosition.ofXY(2, 5), child3, renderer);
        thenThereArePlaceholdersVerticallyAtBetween(1, 1, 5, renderer);
    }

    private void thenTheNodeAtIs(SlotPosition position, SkillTreeNode<?> expected,
                                 NodeStructureRenderer renderer) {
        TemplateElementHolder template = renderer.getTreeRenderer().getTemplate();
        SimpleSkillElement element = (SimpleSkillElement) template.getElement(position.toSlotId());
        assertThat("wrong element at pos " + position, element.getNode(), is(expected));
    }

    private void thenThereIsAPlaceholderAt(SlotPosition position, NodeStructureRenderer renderer) {
        TemplateElementHolder template = renderer.getTreeRenderer().getTemplate();
        assertThat("expected placeholder at " + position,
                template.hasPlaceholderAt(position.toSlotId()), is(true));
    }

    private void thenThereArePlaceholdersVerticallyAtBetween(int x, int startY, int endY,
                                                             NodeStructureRenderer renderer) {
        for(int y = startY; y <= endY; y++) {
            thenThereIsAPlaceholderAt(SlotPosition.ofXY(x, y), renderer);
        }
    }
}
