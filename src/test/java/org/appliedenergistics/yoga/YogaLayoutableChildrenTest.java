package org.appliedenergistics.yoga;

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class YogaLayoutableChildrenTest {

    /**
     * Utility method to compare two lists by iterator instead of using size and get
     * @param expected The expected list
     * @param actual The actual list
     * @param <T> The type of elements in the lists
     * @return true if the lists are equal, false otherwise
     */
    private <T> boolean areListsEqual(Iterable<T> expected, Iterable<T> actual) {
        Iterator<T> expectedIterator = expected.iterator();
        Iterator<T> actualIterator = actual.iterator();

        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            T expectedItem = expectedIterator.next();
            T actualItem = actualIterator.next();

            if (!expectedItem.equals(actualItem)) {
                return false;
            }
        }

        return !expectedIterator.hasNext() && !actualIterator.hasNext();
    }

    /**
     * Asserts that two lists are equal using iterator comparison
     * @param expected The expected list
     * @param actual The actual list
     * @param <T> The type of elements in the lists
     */
    private <T> void assertListsEqual(Iterable<T> expected, Iterable<T> actual) {
        assertTrue(areListsEqual(expected, actual),
                "Lists are not equal. Expected: " + expected + ", Actual: " + actual);
    }

    @Test
    public void layoutable_children_single_contents_node() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        YogaNode rootGrandchild0 = new YogaNode();
        YogaNode rootGrandchild1 = new YogaNode();

        root.addChildAt(rootChild0, 0);
        root.addChildAt(rootChild1, 1);
        root.addChildAt(rootChild2, 2);

        rootChild1.addChildAt(rootGrandchild0, 0);
        rootChild1.addChildAt(rootGrandchild1, 1);

        rootChild1.setDisplay(YogaDisplay.CONTENTS);

        List<YogaNode> expectedOrder = new ArrayList<>();
        expectedOrder.add(rootChild0);
        expectedOrder.add(rootGrandchild0);
        expectedOrder.add(rootGrandchild1);
        expectedOrder.add(rootChild2);

        var layoutChildren = root.getLayoutChildren();

        // Verify the correct order and count of layoutable children
        assertListsEqual(expectedOrder, layoutChildren);
    }

    @Test
    public void layoutable_children_multiple_contents_nodes() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        YogaNode rootGrandchild0 = new YogaNode();
        YogaNode rootGrandchild1 = new YogaNode();
        YogaNode rootGrandchild2 = new YogaNode();
        YogaNode rootGrandchild3 = new YogaNode();
        YogaNode rootGrandchild4 = new YogaNode();
        YogaNode rootGrandchild5 = new YogaNode();

        root.addChildAt(rootChild0, 0);
        root.addChildAt(rootChild1, 1);
        root.addChildAt(rootChild2, 2);

        rootChild0.addChildAt(rootGrandchild0, 0);
        rootChild0.addChildAt(rootGrandchild1, 1);
        rootChild1.addChildAt(rootGrandchild2, 0);
        rootChild1.addChildAt(rootGrandchild3, 1);
        rootChild2.addChildAt(rootGrandchild4, 0);
        rootChild2.addChildAt(rootGrandchild5, 1);

        rootChild0.setDisplay(YogaDisplay.CONTENTS);
        rootChild1.setDisplay(YogaDisplay.CONTENTS);
        rootChild2.setDisplay(YogaDisplay.CONTENTS);

        List<YogaNode> expectedOrder = new ArrayList<>();
        expectedOrder.add(rootGrandchild0);
        expectedOrder.add(rootGrandchild1);
        expectedOrder.add(rootGrandchild2);
        expectedOrder.add(rootGrandchild3);
        expectedOrder.add(rootGrandchild4);
        expectedOrder.add(rootGrandchild5);

        var layoutChildren = root.getLayoutChildren();

        // Verify the correct order and count of layoutable children
        assertListsEqual(expectedOrder, layoutChildren);
    }

    @Test
    public void layoutable_children_nested_contents_nodes() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        YogaNode rootGrandchild0 = new YogaNode();
        YogaNode rootGrandchild1 = new YogaNode();

        YogaNode rootGreatGrandchild0 = new YogaNode();
        YogaNode rootGreatGrandchild1 = new YogaNode();

        root.addChildAt(rootChild0, 0);
        root.addChildAt(rootChild1, 1);
        root.addChildAt(rootChild2, 2);

        rootChild1.addChildAt(rootGrandchild0, 0);
        rootChild1.addChildAt(rootGrandchild1, 1);

        rootGrandchild1.addChildAt(rootGreatGrandchild0, 0);
        rootGrandchild1.addChildAt(rootGreatGrandchild1, 1);

        rootChild1.setDisplay(YogaDisplay.CONTENTS);
        rootGrandchild1.setDisplay(YogaDisplay.CONTENTS);

        List<YogaNode> expectedOrder = new ArrayList<>();
        expectedOrder.add(rootChild0);
        expectedOrder.add(rootGrandchild0);
        expectedOrder.add(rootGreatGrandchild0);
        expectedOrder.add(rootGreatGrandchild1);
        expectedOrder.add(rootChild2);

        var layoutChildren = root.getLayoutChildren();

        // Verify the correct order and count of layoutable children
        assertListsEqual(expectedOrder, layoutChildren);
    }

    @Test
    public void layoutable_children_contents_leaf_node() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        root.addChildAt(rootChild0, 0);
        root.addChildAt(rootChild1, 1);
        root.addChildAt(rootChild2, 2);

        rootChild1.setDisplay(YogaDisplay.CONTENTS);

        List<YogaNode> expectedOrder = new ArrayList<>();
        expectedOrder.add(rootChild0);
        expectedOrder.add(rootChild2);

        var layoutChildren = root.getLayoutChildren();

        // Verify the correct order and count of layoutable children
        assertListsEqual(expectedOrder, layoutChildren);
    }

    @Test
    public void layoutable_children_contents_root_node() {
        YogaNode root = new YogaNode();

        YogaNode rootChild0 = new YogaNode();
        YogaNode rootChild1 = new YogaNode();
        YogaNode rootChild2 = new YogaNode();

        root.addChildAt(rootChild0, 0);
        root.addChildAt(rootChild1, 1);
        root.addChildAt(rootChild2, 2);

        root.setDisplay(YogaDisplay.CONTENTS);

        List<YogaNode> expectedOrder = new ArrayList<>();
        expectedOrder.add(rootChild0);
        expectedOrder.add(rootChild1);
        expectedOrder.add(rootChild2);

        var layoutChildren = root.getLayoutChildren();

        // Verify the correct order and count of layoutable children
        assertListsEqual(expectedOrder, layoutChildren);
    }
}