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

public class YGAspectRatioTest {

    private final YogaMeasureFunction MEASURE_FUNCTION = 
        (node, width, widthMode, height, heightMode) -> {
            int measuredWidth = widthMode == YogaMeasureMode.EXACTLY ? 
                    (int) width : 50;
            int measuredHeight = heightMode == YogaMeasureMode.EXACTLY ? 
                    (int) height : 50;
            return new YogaSize(measuredWidth, measuredHeight);
        };

    @Test
    public void test_aspect_ratio_cross_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_main_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_both_dimensions_defined_row() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(100);
        child.setHeight(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_both_dimensions_defined_column() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(100);
        child.setHeight(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_align_stretch() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_flex_grow() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setFlexGrow(1);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_flex_shrink() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(150);
        child.setFlexShrink(1);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_flex_shrink_2() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child0 = new YogaNode();
        child0.setHeightPercent(100);
        child0.setFlexShrink(1);
        child0.setAspectRatio(1);
        root.addChildAt(child0, 0);

        YogaNode child1 = new YogaNode();
        child1.setHeightPercent(100);
        child1.setFlexShrink(1);
        child1.setAspectRatio(1);
        root.addChildAt(child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child0.getLayoutX(), 0);
        assertEquals(0, child0.getLayoutY(), 0);
        assertEquals(50, child0.getLayoutWidth(), 0);
        assertEquals(50, child0.getLayoutHeight(), 0);

        assertEquals(0, child1.getLayoutX(), 0);
        assertEquals(50, child1.getLayoutY(), 0);
        assertEquals(50, child1.getLayoutWidth(), 0);
        assertEquals(50, child1.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_basis() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setFlexBasis(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_absolute_layout_width_defined() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setPositionType(YogaPositionType.ABSOLUTE);
        child.setPosition(YogaEdge.LEFT, 0);
        child.setPosition(YogaEdge.TOP, 0);
        child.setWidth(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_absolute_layout_height_defined() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setPositionType(YogaPositionType.ABSOLUTE);
        child.setPosition(YogaEdge.LEFT, 0);
        child.setPosition(YogaEdge.TOP, 0);
        child.setHeight(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_with_max_cross_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setMaxWidth(40);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(40, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_with_max_main_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setMaxHeight(40);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(40, child.getLayoutWidth(), 0);
        assertEquals(40, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_with_min_cross_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(30);
        child.setMinWidth(40);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(40, child.getLayoutWidth(), 0);
        assertEquals(30, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_with_min_main_defined() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(30);
        child.setMinHeight(40);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(40, child.getLayoutWidth(), 0);
        assertEquals(40, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_double_cross() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setAspectRatio(2);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_half_cross() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(100);
        child.setAspectRatio(0.5f);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_double_main() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setAspectRatio(0.5f);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_half_main() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(100);
        child.setAspectRatio(2);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_with_measure_func() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setMeasureFunction(MEASURE_FUNCTION);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_width_height_flex_grow_row() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(200);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setHeight(50);
        child.setFlexGrow(1);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_width_height_flex_grow_column() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(200);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setHeight(50);
        child.setFlexGrow(1);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_height_as_flex_basis() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(200);
        root.setHeight(200);

        YogaNode child0 = new YogaNode();
        child0.setHeight(50);
        child0.setFlexGrow(1);
        child0.setAspectRatio(1);
        root.addChildAt(child0, 0);

        YogaNode child1 = new YogaNode();
        child1.setHeight(100);
        child1.setFlexGrow(1);
        child1.setAspectRatio(1);
        root.addChildAt(child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child0.getLayoutX(), 0);
        assertEquals(0, child0.getLayoutY(), 0);
        assertEquals(75, child0.getLayoutWidth(), 0);
        assertEquals(75, child0.getLayoutHeight(), 0);

        assertEquals(75, child1.getLayoutX(), 0);
        assertEquals(0, child1.getLayoutY(), 0);
        assertEquals(125, child1.getLayoutWidth(), 0);
        assertEquals(125, child1.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_width_as_flex_basis() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(200);
        root.setHeight(200);

        YogaNode child0 = new YogaNode();
        child0.setWidth(50);
        child0.setFlexGrow(1);
        child0.setAspectRatio(1);
        root.addChildAt(child0, 0);

        YogaNode child1 = new YogaNode();
        child1.setWidth(100);
        child1.setFlexGrow(1);
        child1.setAspectRatio(1);
        root.addChildAt(child1, 1);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child0.getLayoutX(), 0);
        assertEquals(0, child0.getLayoutY(), 0);
        assertEquals(75, child0.getLayoutWidth(), 0);
        assertEquals(75, child0.getLayoutHeight(), 0);

        assertEquals(0, child1.getLayoutX(), 0);
        assertEquals(75, child1.getLayoutY(), 0);
        assertEquals(125, child1.getLayoutWidth(), 0);
        assertEquals(125, child1.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_overrides_flex_grow_row() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setFlexGrow(1);
        child.setAspectRatio(0.5f);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(100, child.getLayoutWidth(), 0);
        assertEquals(200, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_overrides_flex_grow_column() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setFlexGrow(1);
        child.setAspectRatio(2);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(200, child.getLayoutWidth(), 0);
        assertEquals(100, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_left_right_absolute() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setPositionType(YogaPositionType.ABSOLUTE);
        child.setPosition(YogaEdge.LEFT, 10);
        child.setPosition(YogaEdge.TOP, 10);
        child.setPosition(YogaEdge.RIGHT, 10);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(10, child.getLayoutX(), 0);
        assertEquals(10, child.getLayoutY(), 0);
        assertEquals(80, child.getLayoutWidth(), 0);
        assertEquals(80, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_top_bottom_absolute() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setPositionType(YogaPositionType.ABSOLUTE);
        child.setPosition(YogaEdge.LEFT, 10);
        child.setPosition(YogaEdge.TOP, 10);
        child.setPosition(YogaEdge.BOTTOM, 10);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(10, child.getLayoutX(), 0);
        assertEquals(10, child.getLayoutY(), 0);
        assertEquals(80, child.getLayoutWidth(), 0);
        assertEquals(80, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_width_overrides_align_stretch_row() {
        YogaNode root = new YogaNode();
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_height_overrides_align_stretch_column() {
        YogaNode root = new YogaNode();
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setAspectRatio(1);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, child.getLayoutX(), 0);
        assertEquals(0, child.getLayoutY(), 0);
        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_allow_child_overflow_parent_size() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.FLEX_START);
        root.setWidth(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setAspectRatio(4);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(50, root.getLayoutHeight(), 0);

        assertEquals(200, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }

    @Test
    public void test_aspect_ratio_defined_main_with_margin() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.CENTER);
        root.setJustifyContent(YogaJustify.CENTER);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setHeight(50);
        child.setAspectRatio(1);
        child.setMargin(YogaEdge.LEFT, 10);
        child.setMargin(YogaEdge.RIGHT, 10);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(100, root.getLayoutHeight(), 0);

        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }
    
    @Test
    public void test_aspect_ratio_defined_cross_with_margin() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.CENTER);
        root.setJustifyContent(YogaJustify.CENTER);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setAspectRatio(1);
        child.setMargin(YogaEdge.LEFT, 10);
        child.setMargin(YogaEdge.RIGHT, 10);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(100, root.getLayoutHeight(), 0);

        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }
    
    @Test
    public void test_aspect_ratio_defined_cross_with_main_margin() {
        YogaNode root = new YogaNode();
        root.setAlignItems(YogaAlign.CENTER);
        root.setJustifyContent(YogaJustify.CENTER);
        root.setWidth(100);
        root.setHeight(100);

        YogaNode child = new YogaNode();
        child.setWidth(50);
        child.setAspectRatio(1);
        child.setMargin(YogaEdge.TOP, 10);
        child.setMargin(YogaEdge.BOTTOM, 10);
        root.addChildAt(child, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(100, root.getLayoutHeight(), 0);

        assertEquals(50, child.getLayoutWidth(), 0);
        assertEquals(50, child.getLayoutHeight(), 0);
    }
    
    @Test
    public void test_aspect_ratio_should_prefer_explicit_height() {
        MutableYogaConfig config = YogaConfig.create();
        config.setUseWebDefaults(true);

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.COLUMN);

        YogaNode child0 = new YogaNode(config);
        child0.setFlexDirection(YogaFlexDirection.COLUMN);
        root.addChildAt(child0, 0);

        YogaNode child0Child0 = new YogaNode(config);
        child0Child0.setFlexDirection(YogaFlexDirection.COLUMN);
        child0Child0.setHeight(100);
        child0Child0.setAspectRatio(2);
        child0.addChildAt(child0Child0, 0);

        root.calculateLayout(100, 200, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(200, root.getLayoutHeight(), 0);

        assertEquals(100, child0.getLayoutWidth(), 0);
        assertEquals(100, child0.getLayoutHeight(), 0);

        assertEquals(200, child0Child0.getLayoutWidth(), 0);
        assertEquals(100, child0Child0.getLayoutHeight(), 0);
    }
    
    @Test
    public void test_aspect_ratio_should_prefer_explicit_width() {
        MutableYogaConfig config = YogaConfig.create();
        config.setUseWebDefaults(true);

        YogaNode root = new YogaNode(config);
        root.setFlexDirection(YogaFlexDirection.ROW);

        YogaNode child0 = new YogaNode(config);
        child0.setFlexDirection(YogaFlexDirection.ROW);
        root.addChildAt(child0, 0);

        YogaNode child0Child0 = new YogaNode(config);
        child0Child0.setFlexDirection(YogaFlexDirection.ROW);
        child0Child0.setWidth(100);
        child0Child0.setAspectRatio(0.5f);
        child0.addChildAt(child0Child0, 0);

        root.calculateLayout(200, 100, YogaDirection.LTR);

        assertEquals(200, root.getLayoutWidth(), 0);
        assertEquals(100, root.getLayoutHeight(), 0);

        assertEquals(100, child0.getLayoutWidth(), 0);
        assertEquals(100, child0.getLayoutHeight(), 0);

        assertEquals(100, child0Child0.getLayoutWidth(), 0);
        assertEquals(200, child0Child0.getLayoutHeight(), 0);
    }
    
    @Test
    public void test_aspect_ratio_should_prefer_flexed_dimension() {
        MutableYogaConfig config = YogaConfig.create();
        config.setUseWebDefaults(true);

        YogaNode root = new YogaNode(config);

        YogaNode child0 = new YogaNode(config);
        child0.setFlexDirection(YogaFlexDirection.COLUMN);
        child0.setAspectRatio(2);
        child0.setFlexGrow(1);
        root.addChildAt(child0, 0);

        YogaNode child0Child0 = new YogaNode(config);
        child0Child0.setAspectRatio(4);
        child0Child0.setFlexGrow(1);
        child0.addChildAt(child0Child0, 0);

        root.calculateLayout(100, 100, YogaDirection.LTR);

        assertEquals(100, root.getLayoutWidth(), 0);
        assertEquals(100, root.getLayoutHeight(), 0);

        assertEquals(100, child0.getLayoutWidth(), 0);
        assertEquals(50, child0.getLayoutHeight(), 0);

        assertEquals(200, child0Child0.getLayoutWidth(), 0);
        assertEquals(50, child0Child0.getLayoutHeight(), 0);
    }
}