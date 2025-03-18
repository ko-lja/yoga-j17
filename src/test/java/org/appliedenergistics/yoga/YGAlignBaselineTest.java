package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.appliedenergistics.yoga.config.MutableYogaConfig;


public class YGAlignBaselineTest {

    private float baselineFunc(YogaNode node, float width, float height) {
        return height / 2;
    }

    private YogaSize measure1(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(42, 50);
    }

    private YogaSize measure2(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
        return new YogaSize(279, 126);
    }

    private static YogaNode createYogaNode(
            MutableYogaConfig config,
            YogaFlexDirection direction,
            int width,
            int height,
            boolean alignBaseline) {
        YogaNode node = new YogaNode(config);
        node.setFlexDirection(direction);
        if (alignBaseline) {
            node.setAlignItems(YogaAlign.BASELINE);
        }
        node.setWidth(width);
        node.setHeight(height);
        return node;
    }

    // Test case for bug in T32999822
    @Test
    public void align_baseline_parent_ht_not_specified() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignContent(YogaAlign.STRETCH);
        root.setAlignItems(YogaAlign.BASELINE);
        root.setWidth(340);
        root.setMaxHeight(170);
        root.setMinHeight(0);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setFlexGrow(0);
        rootChild0.setFlexShrink(1);
        rootChild0.setMeasureFunction(this::measure1);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setFlexGrow(0);
        rootChild1.setFlexShrink(1);
        rootChild1.setMeasureFunction(this::measure2);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0);
        assertEquals(0, root.getLayoutY(), 0);
        assertEquals(340, root.getLayoutWidth(), 0);
        assertEquals(126, root.getLayoutHeight(), 0);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(42, rootChild0.getLayoutWidth(), 0);
        assertEquals(50, rootChild0.getLayoutHeight(), 0);
        assertEquals(76, rootChild0.getLayoutY(), 0);

        assertEquals(42, rootChild1.getLayoutX(), 0);
        assertEquals(0, rootChild1.getLayoutY(), 0);
        assertEquals(279, rootChild1.getLayoutWidth(), 0);
        assertEquals(126, rootChild1.getLayoutHeight(), 0);
    }

    @Test
    public void align_baseline_with_no_parent_ht() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.BASELINE);
        root.setWidth(150);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setWidth(50);
        rootChild0.setHeight(50);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setWidth(50);
        rootChild1.setHeight(40);
        rootChild1.setBaselineFunction(this::baselineFunc);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0);
        assertEquals(0, root.getLayoutY(), 0);
        assertEquals(150, root.getLayoutWidth(), 0);
        assertEquals(70, root.getLayoutHeight(), 0);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);
        assertEquals(50, rootChild0.getLayoutWidth(), 0);
        assertEquals(50, rootChild0.getLayoutHeight(), 0);

        assertEquals(50, rootChild1.getLayoutX(), 0);
        assertEquals(30, rootChild1.getLayoutY(), 0);
        assertEquals(50, rootChild1.getLayoutWidth(), 0);
        assertEquals(40, rootChild1.getLayoutHeight(), 0);
    }

    @Test
    public void align_baseline_with_no_baseline_func_and_no_parent_ht() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.BASELINE);
        root.setWidth(150);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setWidth(50);
        rootChild0.setHeight(80);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = new YogaNode(config);
        rootChild1.setWidth(50);
        rootChild1.setHeight(50);
        root.addChildAt(rootChild1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0);
        assertEquals(0, root.getLayoutY(), 0);
        assertEquals(150, root.getLayoutWidth(), 0);
        assertEquals(80, root.getLayoutHeight(), 0);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);
        assertEquals(50, rootChild0.getLayoutWidth(), 0);
        assertEquals(80, rootChild0.getLayoutHeight(), 0);

        assertEquals(50, rootChild1.getLayoutX(), 0);
        assertEquals(30, rootChild1.getLayoutY(), 0);
        assertEquals(50, rootChild1.getLayoutWidth(), 0);
        assertEquals(50, rootChild1.getLayoutHeight(), 0);
    }

    @Test
    public void align_baseline_parent_using_child_in_column_as_reference() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

        YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
        rootChild1.addChildAt(rootChild1Child0, 0);

        YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
        rootChild1Child1.setBaselineFunction(this::baselineFunc);
        rootChild1Child1.setIsReferenceBaseline(true);
        rootChild1.addChildAt(rootChild1Child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);

        assertEquals(500, rootChild1.getLayoutX(), 0);
        assertEquals(100, rootChild1.getLayoutY(), 0);

        assertEquals(0, rootChild1Child0.getLayoutX(), 0);
        assertEquals(0, rootChild1Child0.getLayoutY(), 0);

        assertEquals(0, rootChild1Child1.getLayoutX(), 0);
        assertEquals(300, rootChild1Child1.getLayoutY(), 0);
    }

    @Test
    public void align_baseline_parent_using_child_with_padding_in_column_as_reference() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

        YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
        rootChild1.addChildAt(rootChild1Child0, 0);

        YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
        rootChild1Child1.setBaselineFunction(this::baselineFunc);
        rootChild1Child1.setIsReferenceBaseline(true);
        rootChild1Child1.setPadding(YogaEdge.LEFT, 100);
        rootChild1Child1.setPadding(YogaEdge.RIGHT, 100);
        rootChild1Child1.setPadding(YogaEdge.TOP, 100);
        rootChild1Child1.setPadding(YogaEdge.BOTTOM, 100);
        rootChild1.addChildAt(rootChild1Child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);

        assertEquals(500, rootChild1.getLayoutX(), 0);
        assertEquals(100, rootChild1.getLayoutY(), 0);

        assertEquals(0, rootChild1Child0.getLayoutX(), 0);
        assertEquals(0, rootChild1Child0.getLayoutY(), 0);

        assertEquals(0, rootChild1Child1.getLayoutX(), 0);
        assertEquals(300, rootChild1Child1.getLayoutY(), 0);
    }

    @Test
    public void align_baseline_parent_with_padding_using_child_in_column_as_reference() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

        YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
        rootChild1.setPadding(YogaEdge.LEFT, 100);
        rootChild1.setPadding(YogaEdge.RIGHT, 100);
        rootChild1.setPadding(YogaEdge.TOP, 100);
        rootChild1.setPadding(YogaEdge.BOTTOM, 100);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
        rootChild1.addChildAt(rootChild1Child0, 0);

        YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
        rootChild1Child1.setBaselineFunction(this::baselineFunc);
        rootChild1Child1.setIsReferenceBaseline(true);
        rootChild1.addChildAt(rootChild1Child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);

        assertEquals(500, rootChild1.getLayoutX(), 0);
        assertEquals(0, rootChild1.getLayoutY(), 0);

        assertEquals(100, rootChild1Child0.getLayoutX(), 0);
        assertEquals(100, rootChild1Child0.getLayoutY(), 0);

        assertEquals(100, rootChild1Child1.getLayoutX(), 0);
        assertEquals(400, rootChild1Child1.getLayoutY(), 0);
    }

    @Test
    public void align_baseline_parent_with_margin_using_child_in_column_as_reference() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

        YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
        rootChild1.setMargin(YogaEdge.LEFT, 100);
        rootChild1.setMargin(YogaEdge.RIGHT, 100);
        rootChild1.setMargin(YogaEdge.TOP, 100);
        rootChild1.setMargin(YogaEdge.BOTTOM, 100);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
        rootChild1.addChildAt(rootChild1Child0, 0);

        YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
        rootChild1Child1.setBaselineFunction(this::baselineFunc);
        rootChild1Child1.setIsReferenceBaseline(true);
        rootChild1.addChildAt(rootChild1Child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);

        assertEquals(600, rootChild1.getLayoutX(), 0);
        assertEquals(100, rootChild1.getLayoutY(), 0);

        assertEquals(0, rootChild1Child0.getLayoutX(), 0);
        assertEquals(0, rootChild1Child0.getLayoutY(), 0);

        assertEquals(0, rootChild1Child1.getLayoutX(), 0);
        assertEquals(300, rootChild1Child1.getLayoutY(), 0);
    }

    @Test
    public void align_baseline_parent_using_child_with_margin_in_column_as_reference() {
        MutableYogaConfig config = YogaConfig.create();

        YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

        YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
        root.addChildAt(rootChild1, 1);

        YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
        rootChild1.addChildAt(rootChild1Child0, 0);

        YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
        rootChild1Child1.setBaselineFunction(this::baselineFunc);
        rootChild1Child1.setIsReferenceBaseline(true);
        rootChild1Child1.setMargin(YogaEdge.LEFT, 100);
        rootChild1Child1.setMargin(YogaEdge.RIGHT, 100);
        rootChild1Child1.setMargin(YogaEdge.TOP, 100);
        rootChild1Child1.setMargin(YogaEdge.BOTTOM, 100);
        rootChild1.addChildAt(rootChild1Child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, rootChild0.getLayoutX(), 0);
        assertEquals(0, rootChild0.getLayoutY(), 0);

        assertEquals(500, rootChild1.getLayoutX(), 0);
        assertEquals(0, rootChild1.getLayoutY(), 0);

        assertEquals(0, rootChild1Child0.getLayoutX(), 0);
        assertEquals(0, rootChild1Child0.getLayoutY(), 0);

        assertEquals(100, rootChild1Child1.getLayoutX(), 0);
        assertEquals(400, rootChild1Child1.getLayoutY(), 0);
    }

        @Test
        public void align_baseline_parent_using_child_in_row_as_reference() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.ROW, 500, 800, true);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setBaselineFunction(this::baselineFunc);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(100, rootChild1.getLayoutY(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(500, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_with_padding_in_row_as_reference() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.ROW, 500, 800, true);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setBaselineFunction(this::baselineFunc);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1Child1.setPadding(YogaEdge.LEFT, 100);
            rootChild1Child1.setPadding(YogaEdge.RIGHT, 100);
            rootChild1Child1.setPadding(YogaEdge.TOP, 100);
            rootChild1Child1.setPadding(YogaEdge.BOTTOM, 100);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(100, rootChild1.getLayoutY(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(500, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_with_margin_in_row_as_reference() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.ROW, 500, 800, true);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setBaselineFunction(this::baselineFunc);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1Child1.setMargin(YogaEdge.LEFT, 100);
            rootChild1Child1.setMargin(YogaEdge.RIGHT, 100);
            rootChild1Child1.setMargin(YogaEdge.TOP, 100);
            rootChild1Child1.setMargin(YogaEdge.BOTTOM, 100);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(100, rootChild1.getLayoutY(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(600, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_column_as_reference_with_no_baseline_func() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 800, false);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(100, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(0, rootChild1.getLayoutY(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(0, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_row_as_reference_with_no_baseline_func() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = createYogaNode(config, YogaFlexDirection.ROW, 1000, 1000, true);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = createYogaNode(config, YogaFlexDirection.ROW, 500, 800, true);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(100, rootChild1.getLayoutY(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(500, rootChild1Child1.getLayoutX(), 0);
            assertEquals(100, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_column_as_reference_with_height_not_specified() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = new YogaNode(config);
            root.setFlexDirection(YogaFlexDirection.ROW);
            root.setAlignItems(YogaAlign.BASELINE);
            root.setWidth(1000);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = new YogaNode(config);
            rootChild1.setFlexDirection(YogaFlexDirection.COLUMN);
            rootChild1.setWidth(500);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setBaselineFunction(this::baselineFunc);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(800, root.getLayoutHeight(), 0);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(100, rootChild1.getLayoutY(), 0);
            assertEquals(700, rootChild1.getLayoutHeight(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(0, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_row_as_reference_with_height_not_specified() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = new YogaNode(config);
            root.setFlexDirection(YogaFlexDirection.ROW);
            root.setAlignItems(YogaAlign.BASELINE);
            root.setWidth(1000);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = new YogaNode(config);
            rootChild1.setFlexDirection(YogaFlexDirection.ROW);
            rootChild1.setWidth(500);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setBaselineFunction(this::baselineFunc);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(900, root.getLayoutHeight(), 0);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(400, rootChild1.getLayoutY(), 0);
            assertEquals(500, rootChild1.getLayoutHeight(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(500, rootChild1Child1.getLayoutX(), 0);
            assertEquals(0, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_column_as_reference_with_no_baseline_func_and_height_not_specified() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = new YogaNode(config);
            root.setFlexDirection(YogaFlexDirection.ROW);
            root.setAlignItems(YogaAlign.BASELINE);
            root.setWidth(1000);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = new YogaNode(config);
            rootChild1.setFlexDirection(YogaFlexDirection.COLUMN);
            rootChild1.setWidth(500);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 300, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(700, root.getLayoutHeight(), 0);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(100, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(0, rootChild1.getLayoutY(), 0);
            assertEquals(700, rootChild1.getLayoutHeight(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(0, rootChild1Child1.getLayoutX(), 0);
            assertEquals(300, rootChild1Child1.getLayoutY(), 0);
        }

        @Test
        public void align_baseline_parent_using_child_in_row_as_reference_with_no_baseline_func_and_height_not_specified() {
            MutableYogaConfig config = YogaConfig.create();

            YogaNode root = new YogaNode(config);
            root.setFlexDirection(YogaFlexDirection.ROW);
            root.setAlignItems(YogaAlign.BASELINE);
            root.setWidth(1000);

            YogaNode rootChild0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 600, false);
            root.addChildAt(rootChild0, 0);

            YogaNode rootChild1 = new YogaNode(config);
            rootChild1.setFlexDirection(YogaFlexDirection.ROW);
            rootChild1.setWidth(500);
            root.addChildAt(rootChild1, 1);

            YogaNode rootChild1Child0 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 500, false);
            rootChild1.addChildAt(rootChild1Child0, 0);

            YogaNode rootChild1Child1 = createYogaNode(config, YogaFlexDirection.COLUMN, 500, 400, false);
            rootChild1Child1.setIsReferenceBaseline(true);
            rootChild1.addChildAt(rootChild1Child1, 1);

            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

            assertEquals(700, root.getLayoutHeight(), 0);

            assertEquals(0, rootChild0.getLayoutX(), 0);
            assertEquals(0, rootChild0.getLayoutY(), 0);

            assertEquals(500, rootChild1.getLayoutX(), 0);
            assertEquals(200, rootChild1.getLayoutY(), 0);
            assertEquals(500, rootChild1.getLayoutHeight(), 0);

            assertEquals(0, rootChild1Child0.getLayoutX(), 0);
            assertEquals(0, rootChild1Child0.getLayoutY(), 0);

            assertEquals(500, rootChild1Child1.getLayoutX(), 0);
            assertEquals(0, rootChild1Child1.getLayoutY(), 0);
        }
    }