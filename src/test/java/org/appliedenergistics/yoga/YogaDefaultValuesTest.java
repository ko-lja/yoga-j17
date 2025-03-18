package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YogaDefaultValuesTest {

    @Test
    public void assertDefaultValues() {
        YogaNode root = new YogaNode();

        assertEquals(0, root.getChildCount());

        assertEquals(YogaDirection.INHERIT, root.getStyleDirection());
        assertEquals(YogaFlexDirection.COLUMN, root.getFlexDirection());
        assertEquals(YogaJustify.FLEX_START, root.getJustifyContent());
        assertEquals(YogaAlign.FLEX_START, root.getAlignContent());
        assertEquals(YogaAlign.STRETCH, root.getAlignItems());
        assertEquals(YogaAlign.AUTO, root.getAlignSelf());
        assertEquals(YogaPositionType.RELATIVE, root.getPositionType());
        assertEquals(YogaWrap.NO_WRAP, root.getWrap());
        assertEquals(YogaOverflow.VISIBLE, root.getOverflow());
        assertEquals(0f, root.getFlexGrow(), 0.0f);
        assertEquals(0f, root.getFlexShrink(), 0.0f);
        assertEquals(YogaUnit.AUTO, root.getFlexBasis().unit);

        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.LEFT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.TOP).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.RIGHT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.BOTTOM).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.START).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPosition(YogaEdge.END).unit);

        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.LEFT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.TOP).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.RIGHT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.BOTTOM).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.START).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMargin(YogaEdge.END).unit);

        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.LEFT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.TOP).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.RIGHT).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.BOTTOM).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.START).unit);
        assertEquals(YogaUnit.UNDEFINED, root.getPadding(YogaEdge.END).unit);

        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.LEFT)));
        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.TOP)));
        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.RIGHT)));
        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.BOTTOM)));
        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.START)));
        assertTrue(YogaConstants.isUndefined(root.getBorder(YogaEdge.END)));

        assertEquals(YogaUnit.AUTO, root.getWidth().unit);
        assertEquals(YogaUnit.AUTO, root.getHeight().unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMinWidth().unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMinHeight().unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMaxWidth().unit);
        assertEquals(YogaUnit.UNDEFINED, root.getMaxHeight().unit);

        assertEquals(0f, root.getLayoutX(), 0.0f);
        assertEquals(0f, root.getLayoutY(), 0.0f);
        assertEquals(0f, root.getLayoutRight(), 0.0f);
        assertEquals(0f, root.getLayoutBottom(), 0.0f);

        assertEquals(0f, root.getLayoutMargin(YogaEdge.LEFT), 0.0f);
        assertEquals(0f, root.getLayoutMargin(YogaEdge.TOP), 0.0f);
        assertEquals(0f, root.getLayoutMargin(YogaEdge.RIGHT), 0.0f);
        assertEquals(0f, root.getLayoutMargin(YogaEdge.BOTTOM), 0.0f);

        assertEquals(0f, root.getLayoutPadding(YogaEdge.LEFT), 0.0f);
        assertEquals(0f, root.getLayoutPadding(YogaEdge.TOP), 0.0f);
        assertEquals(0f, root.getLayoutPadding(YogaEdge.RIGHT), 0.0f);
        assertEquals(0f, root.getLayoutPadding(YogaEdge.BOTTOM), 0.0f);

        assertEquals(0f, root.getLayoutBorder(YogaEdge.LEFT), 0.0f);
        assertEquals(0f, root.getLayoutBorder(YogaEdge.TOP), 0.0f);
        assertEquals(0f, root.getLayoutBorder(YogaEdge.RIGHT), 0.0f);
        assertEquals(0f, root.getLayoutBorder(YogaEdge.BOTTOM), 0.0f);

        assertTrue(YogaConstants.isUndefined(root.getLayoutWidth()));
        assertTrue(YogaConstants.isUndefined(root.getLayoutHeight()));
        assertEquals(YogaDirection.INHERIT, root.getLayoutDirection());
    }

    @Test
    public void assertWebDefaultValues() {
        MutableYogaConfig config = YogaConfig.create();
        config.setUseWebDefaults(true);
        YogaNode root = new YogaNode(config);

        assertEquals(YogaFlexDirection.ROW, root.getFlexDirection());
        assertEquals(YogaAlign.STRETCH, root.getAlignContent());
        assertEquals(1.0f, root.getFlexShrink(), 0.0f);
    }

    @Test
    public void assertWebDefaultValuesReset() {
        MutableYogaConfig config = YogaConfig.create();
        config.setUseWebDefaults(true);
        YogaNode root = new YogaNode(config);
        root.reset();

        assertEquals(YogaFlexDirection.ROW, root.getFlexDirection());
        assertEquals(YogaAlign.STRETCH, root.getAlignContent());
        assertEquals(1.0f, root.getFlexShrink(), 0.0f);
    }

    @Test
    public void assertLegacyStretchBehaviour() {
        MutableYogaConfig config = YogaConfig.create();
        config.setErrata(YogaErrata.STRETCH_FLEX_BASIS);
        YogaNode root = new YogaNode(config);
        root.setWidth(500);
        root.setHeight(500);

        YogaNode rootChild0 = new YogaNode(config);
        rootChild0.setAlignItems(YogaAlign.FLEX_START);
        root.addChildAt(rootChild0, 0);

        YogaNode rootChild0Child0 = new YogaNode(config);
        rootChild0Child0.setFlexGrow(1);
        rootChild0Child0.setFlexShrink(1);
        rootChild0.addChildAt(rootChild0Child0, 0);

        YogaNode rootChild0Child0Child0 = new YogaNode(config);
        rootChild0Child0Child0.setFlexGrow(1);
        rootChild0Child0Child0.setFlexShrink(1);
        rootChild0Child0.addChildAt(rootChild0Child0Child0, 0);

        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);

        assertEquals(0, root.getLayoutX(), 0.0f);
        assertEquals(0, root.getLayoutY(), 0.0f);
        assertEquals(500, root.getLayoutWidth(), 0.0f);
        assertEquals(500, root.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0.getLayoutY(), 0.0f);
        assertEquals(500, rootChild0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0Child0.getLayoutHeight(), 0.0f);

        assertEquals(0, rootChild0Child0Child0.getLayoutX(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutY(), 0.0f);
        assertEquals(0, rootChild0Child0Child0.getLayoutWidth(), 0.0f);
        assertEquals(500, rootChild0Child0Child0.getLayoutHeight(), 0.0f);
    }

    @Test
    public void assertBoxSizingBorderBox() {
        MutableYogaConfig config = YogaConfig.create();
        YogaNode root = new YogaNode(config);

        assertEquals(YogaBoxSizing.BORDER_BOX, root.getBoxSizing());
    }
}