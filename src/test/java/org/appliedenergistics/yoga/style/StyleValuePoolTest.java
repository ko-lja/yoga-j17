package org.appliedenergistics.yoga.style;

import org.appliedenergistics.yoga.numeric.FloatOptional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StyleValuePoolTest {

    @Test
    public void undefinedAtInit() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        assertTrue(handle.isUndefined());
        assertFalse(handle.isDefined());
        assertEquals(StyleLength.undefined(), pool.getLength(handle));
        assertEquals(FloatOptional.of(), pool.getNumber(handle));
    }

    @Test
    public void autoAtInit() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = StyleValueHandle.ofAuto();

        assertTrue(handle.isAuto());
        assertEquals(StyleLength.ofAuto(), pool.getLength(handle));
    }

    @Test
    public void storeSmallIntPoints() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.points(10));

        assertEquals(StyleLength.points(10), pool.getLength(handle));
    }

    @Test
    public void storeSmallNegativeIntPoints() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.points(-10));

        assertEquals(StyleLength.points(-10), pool.getLength(handle));
    }

    @Test
    public void storeSmallIntPercent() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.percent(10));

        assertEquals(StyleLength.percent(10), pool.getLength(handle));
    }

    @Test
    public void storeLargeIntPercent() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.percent(262144));

        assertEquals(StyleLength.percent(262144), pool.getLength(handle));
    }

    @Test
    public void storeLargeIntAfterSmallInt() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.percent(10));
        pool.store(handle, StyleLength.percent(262144));

        assertEquals(StyleLength.percent(262144), pool.getLength(handle));
    }

    @Test
    public void storeSmallIntAfterLargeInt() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.percent(262144));
        pool.store(handle, StyleLength.percent(10));

        assertEquals(StyleLength.percent(10), pool.getLength(handle));
    }

    @Test
    public void storeSmallIntNumber() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, FloatOptional.of(10.0f));

        assertEquals(FloatOptional.of(10.0f), pool.getNumber(handle));
    }

    @Test
    public void storeUndefined() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.undefined());

        assertTrue(handle.isUndefined());
        assertFalse(handle.isDefined());
        assertEquals(StyleLength.undefined(), pool.getLength(handle));
    }

    @Test
    public void storeUndefinedAfterSmallInt() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.points(10));
        pool.store(handle, StyleLength.undefined());

        assertTrue(handle.isUndefined());
        assertFalse(handle.isDefined());
        assertEquals(StyleLength.undefined(), pool.getLength(handle));
    }

    @Test
    public void storeUndefinedAfterLargeInt() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handle = new StyleValueHandle();

        pool.store(handle, StyleLength.points(262144));
        pool.store(handle, StyleLength.undefined());

        assertTrue(handle.isUndefined());
        assertFalse(handle.isDefined());
        assertEquals(StyleLength.undefined(), pool.getLength(handle));
    }

    @Test
    public void storeKeywords() {
        StyleValuePool pool = new StyleValuePool();
        StyleValueHandle handleMaxContent = new StyleValueHandle();
        StyleValueHandle handleFitContent = new StyleValueHandle();
        StyleValueHandle handleStretch = new StyleValueHandle();

        pool.store(handleMaxContent, StyleSizeLength.ofMaxContent());
        pool.store(handleFitContent, StyleSizeLength.ofFitContent());
        pool.store(handleStretch, StyleSizeLength.ofStretch());

        assertEquals(StyleSizeLength.ofMaxContent(), pool.getSize(handleMaxContent));
        assertEquals(StyleSizeLength.ofFitContent(), pool.getSize(handleFitContent));
        assertEquals(StyleSizeLength.ofStretch(), pool.getSize(handleStretch));
    }
}
