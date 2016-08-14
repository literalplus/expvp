/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.renderer;

import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.inventory.gui.holder.TemplateElementHolder;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.element.SimpleSkillElement;
import me.minotopia.expvp.skill.tree.ui.renderer.exception.RenderingException;

import java.util.function.Function;

/**
 * Attempts to somehow fit the structure of a skill tree in the area of a Minecraft inventory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class TreeStructureRenderer {
    public static final int MAX_HEIGHT = 3;
    public static final int MAX_WIDTH = 5;
    private final SkillTree tree;
    private final TemplateElementHolder template = new TemplateElementHolder();
    private Function<SimpleSkillTreeNode, MenuElement> elementSupplier = SimpleSkillElement::new;

    public TreeStructureRenderer(SkillTree tree) {
        this.tree = tree;
    }

    public void render() throws RenderingException {
        assureCouldTreeFitIntoGrid();
        new NodeStructureRenderer(this).render();
    }

    private void assureCouldTreeFitIntoGrid() throws RenderingException {
        if(tree.getHeight() > MAX_HEIGHT) {
            throw new RenderingException("Dieser Baum hat zu viele Äste! (drei erlaubt)");
        } else if(tree.getWidth() > MAX_WIDTH) {
            throw new RenderingException("Dieser Baum ist zu hoch! (fünf Skills in einer Zeile erlaubt)");
        }
    }

    public void applyStructureTo(ElementHolder holder) {
        template.apply(holder);
    }

    public TemplateElementHolder getTemplate() {
        return template;
    }

    public SkillTree getTree() {
        return tree;
    }

    public Function<SimpleSkillTreeNode, MenuElement> getElementSupplier() {
        return elementSupplier;
    }

    public void setElementSupplier(Function<SimpleSkillTreeNode, MenuElement> elementSupplier) {
        this.elementSupplier = elementSupplier;
    }

    MenuElement<?> createElement(SimpleSkillTreeNode node) {
        return elementSupplier.apply(node);
    }
}
