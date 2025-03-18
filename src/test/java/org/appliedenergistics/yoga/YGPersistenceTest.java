package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGPersistenceTest {

    @Test
    public void testCloningSharedRoot() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setFlexGrow(1);
        rootChild0.setFlexBasis(50);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setFlexGrow(1);
        root.addChildAt(rootChild1, 1);
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(100, root.getLayoutWidth(), 0.0f);
        assertEquals(100, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(100, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(75, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild1.getLayoutX(), 0.0f);
        assertEquals(75, rootChild1.getLayoutY(), 0.0f);
        assertEquals(100, rootChild1.getLayoutWidth(), 0.0f);
        assertEquals(25, rootChild1.getLayoutHeight(), 0.0f);

        YogaNode root2 = root.cloneWithChildren();
        root2.setWidth(100);

        assertEquals(2, root2.getChildCount());
        // The children should have referential equality at this point.
        assertSame(rootChild0, root2.getChild(0));
        assertSame(rootChild1, root2.getChild(1));

        root2.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(2, root2.getChildCount());
        // Relayout with no changed input should result in referential equality.
        assertSame(rootChild0, root2.getChild(0));
        assertSame(rootChild1, root2.getChild(1));

        root2.setWidth(150);
        root2.setHeight(200);
        root2.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(2, root2.getChildCount());
        // Relayout with changed input should result in cloned children.
        YogaNode root2Child0 = root2.getChild(0);
        YogaNode root2Child1 = root2.getChild(1);
        assertNotSame(rootChild0, root2Child0);
        assertNotSame(rootChild1, root2Child1);

        // Everything in the root should remain unchanged.
        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(100, root.getLayoutWidth(), 0.0f);
        assertEquals(100, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(100, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(75, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild1.getLayoutX(), 0.0f);
        assertEquals(75, rootChild1.getLayoutY(), 0.0f);
        assertEquals(100, rootChild1.getLayoutWidth(), 0.0f);
        assertEquals(25, rootChild1.getLayoutHeight(), 0.0f);

        // The new root now has new layout.
        assertEquals(0, root2.getLayoutX(), 0.0f);
        assertEquals(0, root2.getLayoutY(), 0.0f);
        assertEquals(150, root2.getLayoutWidth(), 0.0f);
        assertEquals(200, root2.getLayoutHeight(), 0.0f);

        assertEquals(0, root2Child0.getLayoutX(), 0.0f);
        assertEquals(0, root2Child0.getLayoutY(), 0.0f);
        assertEquals(150, root2Child0.getLayoutWidth(), 0.0f);
        assertEquals(125, root2Child0.getLayoutHeight(), 0.0f);

        assertEquals(0, root2Child1.getLayoutX(), 0.0f);
        assertEquals(125, root2Child1.getLayoutY(), 0.0f);
        assertEquals(150, root2Child1.getLayoutWidth(), 0.0f);
        assertEquals(75, root2Child1.getLayoutHeight(), 0.0f);
    }

    @Test
    public void testMutatingChildrenOfACloneClonesOnlyAfterLayout() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        assertEquals(0, root.getChildCount());

        YogaNode root2 = root.cloneWithChildren();
        assertEquals(0, root2.getChildCount());

        YogaNode root2Child0 = new YogaNode(config);
        root2.addChildAt(root2Child0, 0);

        assertEquals(0, root.getChildCount());
        assertEquals(1, root2.getChildCount());

        YogaNode root3 = root2.cloneWithChildren();
        assertEquals(1, root2.getChildCount());
        assertEquals(1, root3.getChildCount());
        assertSame(root2.getChild(0), root3.getChild(0));

        YogaNode root3Child1 = new YogaNode(config);
        root3.addChildAt(root3Child1, 1);
        assertEquals(1, root2.getChildCount());
        assertEquals(2, root3.getChildCount());
        assertSame(root3Child1, root3.getChild(1));
        assertSame(root2.getChild(0), root3.getChild(0));

        YogaNode root4 = root3.cloneWithChildren();
        assertSame(root3Child1, root4.getChild(1));

        root4.removeChildAndInvalidate(root3Child1);
        assertEquals(2, root3.getChildCount());
        assertEquals(1, root4.getChildCount());
        assertSame(root3.getChild(0), root4.getChild(0));

        root4.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertNotSame(root3.getChild(0), root4.getChild(0));
        
        root3.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        assertNotSame(root2.getChild(0), root3.getChild(0));
    }

    @Test
    public void testCloningTwoLevels() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setFlexGrow(1);
        rootChild0.setFlexBasis(15);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setFlexGrow(1);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild10 = new YogaNode(config);
        rootChild10.setFlexBasis(10);
        rootChild10.setFlexGrow(1);
        rootChild1.addChildAt(rootChild10, 0);

        YogaNode rootChild11 = new YogaNode(config);
        rootChild11.setFlexBasis(25);
        rootChild1.addChildAt(rootChild11, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(40, rootChild0.getLayoutHeight(), 0.0f);
        assertEquals(60, rootChild1.getLayoutHeight(), 0.0f);
        assertEquals(35, rootChild10.getLayoutHeight(), 0.0f);
        assertEquals(25, rootChild11.getLayoutHeight(), 0.0f);

        YogaNode root2Child0 = rootChild0.cloneWithChildren();
        YogaNode root2Child1 = rootChild1.cloneWithChildren();
        YogaNode root2 = root.cloneWithChildren();

        root2Child0.setFlexGrow(0);
        root2Child0.setFlexBasis(40);

        root2.clearChildren();
        root2.addChildAt(root2Child0, 0);
        root2.addChildAt(root2Child1, 1);
        assertEquals(2, root2.getChildCount());

        root2.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        // Original root is unchanged
        assertEquals(40, rootChild0.getLayoutHeight(), 0.0f);
        assertEquals(60, rootChild1.getLayoutHeight(), 0.0f);
        assertEquals(35, rootChild10.getLayoutHeight(), 0.0f);
        assertEquals(25, rootChild11.getLayoutHeight(), 0.0f);

        // New root has new layout at the top
        assertEquals(40, root2Child0.getLayoutHeight(), 0.0f);
        assertEquals(60, root2Child1.getLayoutHeight(), 0.0f);

        // The deeper children are untouched.
        assertSame(root2Child1.getChild(0), rootChild10);
        assertSame(root2Child1.getChild(1), rootChild11);
    }

    @Test
    public void testCloningAndFreeing() {
        try {
            TestUtil.startCountingNodes();

            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = new YogaNode(config);
            root.setWidth(100);
            root.setHeight(100);
            YogaNode rootChild0 = new YogaNode(config);
            root.addChildAt(rootChild0, 0);
            YogaNode rootChild1 = new YogaNode(config);
            root.addChildAt(rootChild1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            YogaNode root2 = root.cloneWithChildren();

            // In Java, we don't need to free the root separately as memory management is handled by GC

            root2.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            // Again, we don't need to free the objects separately
            
            assertEquals(0, TestUtil.stopCountingNodes());
        } catch (Exception e) {
            throw new RuntimeException("Test failed: " + e.getMessage());
        }
    }
}