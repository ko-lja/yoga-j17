package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.config.YogaConfig;
import org.appliedenergistics.yoga.event.LayoutData;
import org.appliedenergistics.yoga.event.YogaEvent;
import org.appliedenergistics.yoga.event.YogaEventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.appliedenergistics.yoga.config.MutableYogaConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: The Java Yoga API handles events differently than the C++ API.
 * In this implementation, we're approximating the behavior using the Java version
 * of event subscriptions. The implementation may differ from the original C++ tests.
 */
public class EventsTest {
    /**
     * This class represents an event with its arguments.
     */
    private static class EventArgs {
        final YogaNode node;
        final YogaEventType type;
        Object data;

        EventArgs(YogaNode node, YogaEventType type, Object data) {
            this.node = node;
            this.type = type;
            this.data = data;
        }
    }

    // List to store events
    private static List<EventArgs> events = new ArrayList<>();
    
    // Event listener implementation
    private final YogaEvent.Subscriber eventListener = (node, type, data) -> events.add(new EventArgs(node, type, data));
    
    @BeforeEach
    public void setUp() {
        // Register global event listener
        YogaEvent.subscribe(eventListener);
        events.clear();
    }
    
    @AfterEach
    public void tearDown() {
        events.clear();
        // Unregister the event listener to clean up
        YogaEvent.reset();
    }
    
    /**
     * Helper method to get the last event
     */
    private EventArgs lastEvent() {
        if (events.isEmpty()) {
            return null;
        }
        return events.get(events.size() - 1);
    }

    @Test
    public void testLayoutEvents() {
        YogaNode root = new YogaNode();
        YogaNode child = new YogaNode();
        root.addChildAt(child, 0);
        
        // Clear events from node creation
        events.clear();
        
        root.calculateLayout(123, 456, YogaDirection.LTR);
        
        // Check for the layout pass start event
        assertTrue(events.size() >= 1);
        assertEquals(root, events.get(0).node);
        assertEquals(YogaEventType.LAYOUT_PASS_START, events.get(0).type);
        
        // Check for node layout events
        // The exact number and order may vary in Java implementation
        boolean foundChildLayout = false;
        boolean foundRootLayout = false;
        
        for (int i = 1; i < events.size(); i++) {
            if (events.get(i).type == YogaEventType.NODE_LAYOUT) {
                if (events.get(i).node == child) {
                    foundChildLayout = true;
                } else if (events.get(i).node == root) {
                    foundRootLayout = true;
                }
            }
        }
        
        assertTrue(foundChildLayout, "Should have a layout event for child node");
        assertTrue(foundRootLayout, "Should have a layout event for root node");
        
        // Check for layout pass end event
        assertEquals(YogaEventType.LAYOUT_PASS_END, lastEvent().type);
        assertEquals(root, lastEvent().node);
    }
    
    @Test
    public void testLayoutEventsSingleNode() {
        YogaNode root = new YogaNode();
        
        // Clear events from node creation
        events.clear();
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        
        assertEquals(YogaEventType.LAYOUT_PASS_START, events.get(0).type);
        assertEquals(root, events.get(0).node);
        
        assertEquals(YogaEventType.NODE_LAYOUT, events.get(1).type);
        assertEquals(root, events.get(1).node);
        
        assertEquals(YogaEventType.LAYOUT_PASS_END, events.get(2).type);
        assertEquals(root, events.get(2).node);
        
        // Check layout data
        LayoutData layoutData = ((YogaEvent.LayoutPassEndData) lastEvent().data).layoutData;
        assertNotNull(layoutData);
        
        assertEquals(1, layoutData.layouts);
        assertEquals(0, layoutData.measures);
        assertEquals(1, layoutData.maxMeasureCache);
    }
    
    @Test
    public void testMeasureFunctionsGetWrapped() {
        YogaNode root = new YogaNode();
        root.setMeasureFunction(new YogaMeasureFunction() {
            @Override
            public YogaSize measure(
                    YogaNode node,
                    float width,
                    YogaMeasureMode widthMode,
                    float height,
                    YogaMeasureMode heightMode) {
                return new YogaSize(0, 0);
            }
        });
        
        // Clear events from node creation
        events.clear();
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        
        // Find measure callback start event
        boolean foundMeasureStart = false;
        for (EventArgs event : events) {
            if (event.type == YogaEventType.MEASURE_CALLBACK_START && event.node == root) {
                foundMeasureStart = true;
                break;
            }
        }
        
        assertTrue(foundMeasureStart, "Should have a measure callback start event");
        assertEquals(YogaEventType.LAYOUT_PASS_END, lastEvent().type);
    }
    
    @Test
    public void testBaselineFunctionsGetWrapped() {
        YogaNode root = new YogaNode();
        YogaNode child = new YogaNode();
        root.addChildAt(child, 0);
        
        child.setBaselineFunction(new YogaBaselineFunction() {
            @Override
            public float baseline(YogaNode node, float width, float height) {
                return 0.0f;
            }
        });
        
        root.setFlexDirection(YogaFlexDirection.ROW);
        root.setAlignItems(YogaAlign.BASELINE);
        
        // Clear events from node creation
        events.clear();
        
        root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaDirection.LTR);
        
        // Find baseline start event
        boolean foundBaselineStart = false;
        for (EventArgs event : events) {
            if (event.type == YogaEventType.NODE_BASELINE_START && event.node == child) {
                foundBaselineStart = true;
                break;
            }
        }
        
        assertTrue(foundBaselineStart, "Should have a baseline start event");
        assertEquals(YogaEventType.LAYOUT_PASS_END, lastEvent().type);
    }
}

/**
 * Factory class since YogaConfigFactory doesn't exist in the Java API
 */
class YogaConfigFactory {
    public static YogaConfig create() {
        return YogaConfig.create();
    }
}