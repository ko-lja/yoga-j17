package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.appliedenergistics.yoga.numeric.FloatOptional;

public class FloatOptionalTest {

    private static final FloatOptional empty = FloatOptional.of();
    private static final FloatOptional zero = FloatOptional.of(0.0f);
    private static final FloatOptional one = FloatOptional.of(1.0f);
    private static final FloatOptional positive = FloatOptional.of(1234.5f);
    private static final FloatOptional negative = FloatOptional.of(-9876.5f);

    @Test
    public void testValue() {
        assertTrue(YogaConstants.isUndefined(empty.unwrap()));
        assertEquals(0.0f, zero.unwrap(), 0.0f);
        assertEquals(1.0f, one.unwrap(), 0.0f);
        assertEquals(1234.5f, positive.unwrap(), 0.0f);
        assertEquals(-9876.5f, negative.unwrap(), 0.0f);

        assertTrue(empty.isUndefined());
        assertFalse(zero.isUndefined());
        assertFalse(one.isUndefined());
        assertFalse(positive.isUndefined());
        assertFalse(negative.isUndefined());
    }

    @Test
    public void testEquality() {
        assertTrue(empty.equals(empty));
        assertTrue(empty.equals(FloatOptional.of())); // Empty constructor creates undefined
        assertFalse(empty.equals(zero));
        assertFalse(empty.equals(negative));
        assertFalse(empty.equals(FloatOptional.of(12.3f)));

        assertTrue(zero.equals(zero));
        assertTrue(zero.equals(FloatOptional.of(0.0f)));
        assertFalse(zero.equals(positive));
        assertFalse(zero.equals(FloatOptional.of(-5555.5f)));

        assertTrue(one.equals(one));
        assertTrue(one.equals(FloatOptional.of(1.0f)));
        assertFalse(one.equals(positive));

        assertTrue(positive.equals(positive));
        assertTrue(positive.equals(FloatOptional.of(positive.unwrap())));
        assertFalse(positive.equals(one));

        assertTrue(negative.equals(negative));
        assertTrue(negative.equals(FloatOptional.of(negative.unwrap())));
        assertFalse(negative.equals(zero));
    }

    @Test
    public void testInequality() {
        assertFalse(!empty.equals(empty));
        assertFalse(!empty.equals(FloatOptional.of()));
        assertTrue(!empty.equals(zero));
        assertTrue(!empty.equals(negative));
        assertTrue(!empty.equals(FloatOptional.of(12.3f)));

        assertFalse(!zero.equals(zero));
        assertFalse(!zero.equals(FloatOptional.of(0.0f)));
        assertTrue(!zero.equals(positive));
        assertTrue(!zero.equals(FloatOptional.of(-5555.5f)));

        assertFalse(!one.equals(one));
        assertFalse(!one.equals(FloatOptional.of(1.0f)));
        assertTrue(!one.equals(positive));

        assertFalse(!positive.equals(positive));
        assertFalse(!positive.equals(FloatOptional.of(positive.unwrap())));
        assertTrue(!positive.equals(one));

        assertFalse(!negative.equals(negative));
        assertFalse(!negative.equals(FloatOptional.of(negative.unwrap())));
        assertTrue(!negative.equals(zero));
    }

    @Test
    public void testGreaterThanWithUndefined() {
        assertFalse(empty.isGreaterThan(empty));
        assertFalse(empty.isGreaterThan(zero));
        assertFalse(empty.isGreaterThan(one));
        assertFalse(empty.isGreaterThan(positive));
        assertFalse(empty.isGreaterThan(negative));
        assertFalse(zero.isGreaterThan(empty));
        assertFalse(one.isGreaterThan(empty));
        assertFalse(positive.isGreaterThan(empty));
        assertFalse(negative.isGreaterThan(empty));
    }

    @Test
    public void testGreaterThan() {
        assertTrue(zero.isGreaterThan(negative));
        assertFalse(zero.isGreaterThan(zero));
        assertFalse(zero.isGreaterThan(positive));
        assertFalse(zero.isGreaterThan(one));

        assertTrue(one.isGreaterThan(negative));
        assertTrue(one.isGreaterThan(zero));
        assertFalse(one.isGreaterThan(positive));

        assertTrue(negative.isGreaterThan(FloatOptional.of(Float.NEGATIVE_INFINITY)));
    }

    @Test
    public void testLessThanWithUndefined() {
        assertFalse(empty.isLessThan(empty));
        assertFalse(zero.isLessThan(empty));
        assertFalse(one.isLessThan(empty));
        assertFalse(positive.isLessThan(empty));
        assertFalse(negative.isLessThan(empty));
        assertFalse(empty.isLessThan(zero));
        assertFalse(empty.isLessThan(one));
        assertFalse(empty.isLessThan(positive));
        assertFalse(empty.isLessThan(negative));
    }

    @Test
    public void testLessThan() {
        assertTrue(negative.isLessThan(zero));
        assertFalse(zero.isLessThan(zero));
        assertFalse(positive.isLessThan(zero));
        assertFalse(one.isLessThan(zero));

        assertTrue(negative.isLessThan(one));
        assertTrue(zero.isLessThan(one));
        assertFalse(positive.isLessThan(one));

        assertTrue(FloatOptional.of(Float.NEGATIVE_INFINITY).isLessThan(negative));
    }

    @Test
    public void testGreaterThanEqualsWithUndefined() {
        assertTrue(empty.isGreaterThanOrEqual(empty));
        assertFalse(empty.isGreaterThanOrEqual(zero));
        assertFalse(empty.isGreaterThanOrEqual(one));
        assertFalse(empty.isGreaterThanOrEqual(positive));
        assertFalse(empty.isGreaterThanOrEqual(negative));
        assertFalse(zero.isGreaterThanOrEqual(empty));
        assertFalse(one.isGreaterThanOrEqual(empty));
        assertFalse(positive.isGreaterThanOrEqual(empty));
        assertFalse(negative.isGreaterThanOrEqual(empty));
    }

    @Test
    public void testGreaterThanEquals() {
        assertTrue(zero.isGreaterThanOrEqual(negative));
        assertTrue(zero.isGreaterThanOrEqual(zero));
        assertFalse(zero.isGreaterThanOrEqual(positive));
        assertFalse(zero.isGreaterThanOrEqual(one));

        assertTrue(one.isGreaterThanOrEqual(negative));
        assertTrue(one.isGreaterThanOrEqual(zero));
        assertFalse(one.isGreaterThanOrEqual(positive));

        assertTrue(negative.isGreaterThanOrEqual(FloatOptional.of(Float.NEGATIVE_INFINITY)));
    }

    @Test
    public void testLessThanEqualsWithUndefined() {
        assertTrue(empty.isLessThanOrEqual(empty));
        assertFalse(zero.isLessThanOrEqual(empty));
        assertFalse(one.isLessThanOrEqual(empty));
        assertFalse(positive.isLessThanOrEqual(empty));
        assertFalse(negative.isLessThanOrEqual(empty));
        assertFalse(empty.isLessThanOrEqual(zero));
        assertFalse(empty.isLessThanOrEqual(one));
        assertFalse(empty.isLessThanOrEqual(positive));
        assertFalse(empty.isLessThanOrEqual(negative));
    }

    @Test
    public void testLessThanEquals() {
        assertTrue(negative.isLessThanOrEqual(zero));
        assertTrue(zero.isLessThanOrEqual(zero));
        assertFalse(positive.isLessThanOrEqual(zero));
        assertFalse(one.isLessThanOrEqual(zero));

        assertTrue(negative.isLessThanOrEqual(one));
        assertTrue(zero.isLessThanOrEqual(one));
        assertFalse(positive.isLessThanOrEqual(one));

        assertTrue(FloatOptional.of(Float.NEGATIVE_INFINITY).isLessThanOrEqual(negative));
    }

    @Test
    public void testAddition() {
        float n = negative.unwrap();
        float p = positive.unwrap();

        assertEquals(one, zero.add(one));
        assertEquals(FloatOptional.of(n + p), negative.add(positive));
        assertEquals(empty, empty.add(zero));
        assertEquals(empty, empty.add(empty));
        assertEquals(empty, negative.add(empty));
    }

    @Test
    public void testMaxOrDefined() {
        assertEquals(empty, empty.maxOrDefined(empty));
        assertEquals(positive, empty.maxOrDefined(positive));
        assertEquals(negative, negative.maxOrDefined(empty));
        assertEquals(negative, negative.maxOrDefined(FloatOptional.of(Float.NEGATIVE_INFINITY)));
        assertEquals(FloatOptional.of(1.125f), FloatOptional.of(1.0f).maxOrDefined(FloatOptional.of(1.125f)));
    }

    @Test
    public void testUnwrap() {
        assertTrue(YogaConstants.isUndefined(empty.unwrap()));
        assertEquals(0.0f, zero.unwrap(), 0.0f);
        assertEquals(123456.78f, FloatOptional.of(123456.78f).unwrap(), 0.0f);
    }
}