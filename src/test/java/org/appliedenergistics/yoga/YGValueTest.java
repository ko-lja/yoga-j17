package org.appliedenergistics.yoga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGValueTest {

    @Test
    public void testSupportsEquality() {
        YogaValue percent12_5 = new YogaValue(12.5f, YogaUnit.PERCENT);
        YogaValue percent12_5_clone = new YogaValue(12.5f, YogaUnit.PERCENT);
        YogaValue percent56_7 = new YogaValue(56.7f, YogaUnit.PERCENT);
        YogaValue point12_5 = new YogaValue(12.5f, YogaUnit.POINT);
        YogaValue auto12_5 = new YogaValue(12.5f, YogaUnit.AUTO);
        YogaValue undefined12_5 = new YogaValue(12.5f, YogaUnit.UNDEFINED);
        
        assertEquals(percent12_5, percent12_5_clone);
        assertNotEquals(percent12_5, percent56_7);
        assertNotEquals(percent12_5, point12_5);
        assertNotEquals(percent12_5, auto12_5);
        assertNotEquals(percent12_5, undefined12_5);

        YogaValue undefined_value = new YogaValue(12.5f, YogaUnit.UNDEFINED);
        YogaValue undefined_const = new YogaValue(YogaConstants.UNDEFINED, YogaUnit.UNDEFINED);
        assertEquals(undefined_value, undefined_const);
        
        YogaValue auto_0 = new YogaValue(0, YogaUnit.AUTO);
        YogaValue auto_negative1 = new YogaValue(-1, YogaUnit.AUTO);
        assertEquals(auto_0, auto_negative1);
    }
}