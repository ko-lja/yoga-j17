package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGCloneNodeTest {

    private void recursivelyAssertProperNodeOwnership(YogaNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            YogaNode child = node.getChild(i);
            assertEquals(node, child.getOwner());
            recursivelyAssertProperNodeOwnership(child);
        }
    }

    @Test
    public void testAbsoluteNodeClonedWithStaticParent() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setPositionType(YogaPositionType.STATIC);
        rootChild0.setWidth(10);
        rootChild0.setHeight(10);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child0.setWidthPercent(1);
        rootChild0Child0.setHeight(1);
        rootChild0.addChildAt(rootChild0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        YogaNode clonedRoot = root.cloneWithChildren();
        clonedRoot.setWidth(110);
        clonedRoot.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        recursivelyAssertProperNodeOwnership(clonedRoot);
    }

    @Test
    public void testAbsoluteNodeClonedWithStaticAncestors() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode();
        rootChild0.setPositionType(YogaPositionType.STATIC);
        rootChild0.setWidth(50);
        rootChild0.setHeight(50);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode();
        rootChild0Child0.setPositionType(YogaPositionType.STATIC);
        rootChild0Child0.setWidth(40);
        rootChild0Child0.setHeight(40);
        rootChild0.addChildAt(rootChild0Child0, 0);

        YogaNode rootChild0Child0Child0 = new YogaNode();
        rootChild0Child0Child0.setPositionType(YogaPositionType.STATIC);
        rootChild0Child0Child0.setWidth(30);
        rootChild0Child0Child0.setHeight(30);
        rootChild0Child0.addChildAt(rootChild0Child0Child0, 0);

        YogaNode rootChild0Child0Child0Child0 = new YogaNode();
        rootChild0Child0Child0Child0.setPositionType(YogaPositionType.ABSOLUTE);
        rootChild0Child0Child0Child0.setWidthPercent(1);
        rootChild0Child0Child0Child0.setHeight(1);
        rootChild0Child0Child0.addChildAt(rootChild0Child0Child0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        YogaNode clonedRoot = root.cloneWithChildren();
        clonedRoot.setWidth(110);
        clonedRoot.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        recursivelyAssertProperNodeOwnership(clonedRoot);
    }
}