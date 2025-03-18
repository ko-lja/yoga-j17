package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class YGTreeMutationTest {

    private List<YogaNode> getChildren(YogaNode node) {
        final int count = node.getChildCount();
        List<YogaNode> children = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            children.add(node.getChild(i));
        }
        return children;
    }

    @Test
    public void testSetChildrenAddsChildrenToParent() {
        YogaNode root = new YogaNode();
        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();

        YogaNode[] children = {rootChild0, rootChild1};
        root.setChildren(Arrays.asList(children));

        List<YogaNode> expectedChildren = Arrays.asList(rootChild0, rootChild1);
        assertEquals(expectedChildren, getChildren(root));

        List<YogaNode> owners = Arrays.asList(rootChild0.getOwner(), rootChild1.getOwner());
        List<YogaNode> expectedOwners = Arrays.asList(root, root);
        assertEquals(expectedOwners, owners);
    }

    @Test
    public void testSetChildrenToEmptyRemovesOldChildren() {
        YogaNode root = new YogaNode();
        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();

        YogaNode[] children = {rootChild0, rootChild1};
        root.setChildren(Arrays.asList(children));
        root.setChildren(new ArrayList<>()); // Empty list

        List<YogaNode> expectedChildren = new ArrayList<>(); // Empty
        assertEquals(expectedChildren, getChildren(root));

        List<YogaNode> owners = Arrays.asList(rootChild0.getOwner(), rootChild1.getOwner());
        List<YogaNode> expectedOwners = Arrays.asList(null, null);
        assertEquals(expectedOwners, owners);
    }

    @Test
    public void testSetChildrenReplacesNonCommonChildren() {
        YogaNode root = new YogaNode();
        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();

        YogaNode[] children1 = {rootChild0, rootChild1};
        root.setChildren(Arrays.asList(children1));

        YogaNode rootChild2 = new YogaNode();
        YogaNode rootChild3 = new YogaNode();

        YogaNode[] children2 = {rootChild2, rootChild3};
        root.setChildren(Arrays.asList(children2));

        List<YogaNode> expectedChildren = Arrays.asList(rootChild2, rootChild3);
        assertEquals(expectedChildren, getChildren(root));

        List<YogaNode> owners = Arrays.asList(rootChild0.getOwner(), rootChild1.getOwner());
        List<YogaNode> expectedOwners = Arrays.asList(null, null);
        assertEquals(expectedOwners, owners);
    }

    @Test
    public void testSetChildrenKeepsAndReordersCommonChildren() {
        YogaNode root = new YogaNode();
        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        YogaNode[] children1 = {rootChild0, rootChild1, rootChild2};
        root.setChildren(Arrays.asList(children1));

        YogaNode rootChild3 = new YogaNode();

        YogaNode[] children2 = {rootChild2, rootChild1, rootChild3};
        root.setChildren(Arrays.asList(children2));

        List<YogaNode> expectedChildren = Arrays.asList(rootChild2, rootChild1, rootChild3);
        assertEquals(expectedChildren, getChildren(root));

        List<YogaNode> owners = Arrays.asList(
            rootChild0.getOwner(),
            rootChild1.getOwner(),
            rootChild2.getOwner(),
            rootChild3.getOwner());
        List<YogaNode> expectedOwners = Arrays.asList(null, root, root, root);
        assertEquals(expectedOwners, owners);
    }
}