/*
 * This file is part of Expvp,
 * Copyright (c) 2016-2016.
 *
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt.
 */

package me.minotopia.expvp.skill.tree.ui.renderer;

import li.l1t.common.inventory.SlotPosition;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.TemplateElementHolder;
import me.minotopia.expvp.skill.tree.SimpleSkillTreeNode;
import me.minotopia.expvp.skill.tree.SkillTree;
import me.minotopia.expvp.skill.tree.ui.renderer.exception.RenderingAlgorithmException;

/**
 * Renders nodes and their children into an inventory grid.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class NodeStructureRenderer {
    private final TreeStructureRenderer treeRenderer;
    private final SkillTree tree;
    private final TemplateElementHolder template;
    private SlotPosition currentPosition;
    private SimpleSkillTreeNode currentNode;

    public NodeStructureRenderer(TreeStructureRenderer treeRenderer) {
        this.treeRenderer = treeRenderer;
        this.tree = treeRenderer.getTree();
        this.template = treeRenderer.getTemplate();
    }

    public void render() {
        selectRootNode();
        renderCurrentNodeAndChildren();
    }

    private void renderCurrentNodeAndChildren() {
        renderCurrentNodeAtCurrentPosition();
        renderUpwardConnectorForCurrentNodeIfNecessary();
        if(currentNode.hasChildren()) {
            renderHorizontalChildConnectorOfCurrentNode();
            renderChildrenOfCurrentNodeAt(bestPositionForFirstNodeOfNextBranch());
        }
    }

    private void renderChildrenOfCurrentNodeAt(SlotPosition firstChildPosition) {
        SlotPosition nextChildPosition = firstChildPosition;
        for(SimpleSkillTreeNode currentChild : currentNode.getChildren()) {
            selectNode(currentChild);
            advanceTo(nextChildPosition);
            renderCurrentNodeAndChildren();
            nextChildPosition = nextChildPosition.add(0, 2);
        }
    }

    private void renderHorizontalChildConnectorOfCurrentNode() {
        renderConnectorRelativeToCurrentPosition(1, 0);
    }

    private void renderUpwardConnectorForCurrentNodeIfNecessary() {
        if(currentNodeHasUpwardConnection()) {
            renderConnectorRelativeToCurrentPosition(0, -1);
        }
    }

    private void renderConnectorRelativeToCurrentPosition(int relX, int relY) {
        SlotPosition targetPosition = currentPosition.add(relX, relY);
        renderConnectorAt(targetPosition);
    }

    private void renderConnectorAt(SlotPosition targetPosition) {
        assureCanDrawAt(targetPosition);
        template.addPlaceholder(targetPosition.toSlotId());
    }

    private void renderCurrentNodeAtCurrentPosition() {
        template.addElement(currentPosition.toSlotId(), createElementForCurrentNode());
    }

    private boolean currentNodeHasUpwardConnection() {
        return currentNode.getParent() != null && currentNode.getParent().getChildId(currentNode) != 0;
    }

    private void selectNode(SimpleSkillTreeNode currentChild) {
        currentNode = currentChild;
    }

    private void selectRootNode() {
        selectNode(tree);
        currentPosition = SlotPosition.ofXY(0, 3);
    }

    private SlotPosition bestPositionForFirstNodeOfNextBranch() {
        if(nextBranchIsLinear() || !canBranchUpward()) {
            return currentPosition.add(2, 0);
        } else {
            return currentPosition.add(2, -2);
        }
    }

    private boolean canBranchUpward() {
        return canAdvanceRelatively(2, -2);
    }

    private boolean nextBranchIsLinear() {
        return currentNode.getChildren().size() == 1;
    }

    private MenuElement createElementForCurrentNode() {
        return treeRenderer.createElement(currentNode);
    }

    private void advanceTo(SlotPosition targetPosition) {
        assureCanDrawAt(targetPosition);
        currentPosition = targetPosition;
    }

    private void assureCanDrawAt(SlotPosition targetPosition) {
        if (!canAdvanceTo(targetPosition)) {
            throw new RenderingAlgorithmException("Cannot advance to: " + targetPosition);
        }
    }

    private boolean canAdvanceRelatively(int relX, int relY) {
        SlotPosition targetPosition = currentPosition.add(relX, relY);
        return canAdvanceTo(targetPosition);
    }

    private boolean canAdvanceTo(SlotPosition targetPosition) {
        return targetPosition.isValidSlot() &&
                !isSlotOccupied(targetPosition);
    }

    private boolean isSlotOccupied(SlotPosition targetPosition) {
        return template.isOccupied(targetPosition.toSlotId());
    }
}
