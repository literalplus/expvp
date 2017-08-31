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

package me.minotopia.expvp.ui.renderer;

import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.TemplateElementHolder;
import me.minotopia.expvp.skilltree.SimpleSkillTreeNode;
import me.minotopia.expvp.skilltree.SkillTree;
import me.minotopia.expvp.ui.renderer.exception.RenderingException;

import java.util.function.Function;

/**
 * Attempts to somehow fit the structure of a skill tree in the area of a Minecraft inventory.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-06-24
 */
public class TreeStructureRenderer {
    public static final int MAX_HEIGHT = 3;
    public static final int MAX_WIDTH = 5;
    private final SkillTree tree;
    private TemplateElementHolder template;
    private Function<SimpleSkillTreeNode, MenuElement> elementSupplier;
    private boolean rendered;

    public TreeStructureRenderer(SkillTree tree, Function<SimpleSkillTreeNode, MenuElement> elementSupplier) {
        this.tree = tree;
        this.elementSupplier = elementSupplier;
        reset();
    }

    public void reset() {
        rendered = false;
        template = new TemplateElementHolder();
    }

    public void render() throws RenderingException {
        assureCouldTreeFitIntoGrid();
        createNodeRenderer().render();
        rendered = true;
    }

    NodeStructureRenderer createNodeRenderer() {
        return new NodeStructureRenderer(tree, template, elementSupplier);
    }

    public boolean isRendered() {
        return rendered;
    }

    private void assureCouldTreeFitIntoGrid() throws RenderingException {
        if (tree.getHeight() > MAX_HEIGHT) {
            throw new RenderingException("Dieser Baum hat zu viele Äste! (drei erlaubt)");
        } else if (tree.getWidth() > MAX_WIDTH) {
            throw new RenderingException("Dieser Baum ist zu hoch! (fünf Skills in einer Zeile erlaubt)");
        }
    }

    public TemplateElementHolder getTemplate() {
        return template;
    }

    public SkillTree getTree() {
        return tree;
    }
}
