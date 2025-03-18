package org.appliedenergistics.yoga.style;

import org.appliedenergistics.yoga.YogaDirection;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaGutter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StyleTest {
    @Test
    public void initialBorderAndPaddingAreZero() {
        YogaStyle style = new YogaStyle();
        assertEquals(0f, style.computeInlineStartPaddingAndBorder(YogaFlexDirection.ROW, YogaDirection.LTR, Float.NaN));
    }

    @Test
    public void computedPaddingIsFloored() {
        YogaStyle style = new YogaStyle();
        style.setPadding(YogaEdge.ALL, StyleLength.points(-1.0f));
        float paddingStart = style.computeInlineStartPadding(
                YogaFlexDirection.ROW, YogaDirection.LTR, 0.0f /*widthSize*/);
        assertEquals(0.0f, paddingStart);
    }

    @Test
    public void computedBorderIsFloored() {
        YogaStyle style = new YogaStyle();
        style.setBorder(YogaEdge.ALL, StyleLength.points(-1.0f));
        float borderStart = style.computeInlineStartBorder(YogaFlexDirection.ROW, YogaDirection.LTR);
        assertEquals(0.0f, borderStart);
    }

    @Test
    public void computedGapIsFloored() {
        YogaStyle style = new YogaStyle();
        style.setGap(YogaGutter.COLUMN, StyleLength.points(-1.0f));
        float gapBetweenColumns = style.computeGapForAxis(YogaFlexDirection.ROW, 0.0f);
        assertEquals(0.0f, gapBetweenColumns);
    }

    @Test
    public void computedMarginIsNotFloored() {
        YogaStyle style = new YogaStyle();
        style.setMargin(YogaEdge.ALL, StyleLength.points(-1.0f));
        float marginStart = style.computeInlineStartMargin(
                YogaFlexDirection.ROW, YogaDirection.LTR, 0.0f /*widthSize*/);
        assertEquals(-1.0f, marginStart);
    }
}