package org.appliedenergistics.yoga;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.Queue;

public class OrdinalsTest {

    @Test
    public void testIteration() {
        Queue<YogaEdge> expectedEdges = new LinkedList<>();
        
        expectedEdges.add(YogaEdge.LEFT);
        expectedEdges.add(YogaEdge.TOP);
        expectedEdges.add(YogaEdge.RIGHT);
        expectedEdges.add(YogaEdge.BOTTOM);
        expectedEdges.add(YogaEdge.START);
        expectedEdges.add(YogaEdge.END);
        expectedEdges.add(YogaEdge.HORIZONTAL);
        expectedEdges.add(YogaEdge.VERTICAL);
        expectedEdges.add(YogaEdge.ALL);

        // In Java, we can iterate over the enum values directly
        for (YogaEdge edge : YogaEdge.values()) {
            assertEquals(expectedEdges.poll(), edge);
        }

        assertTrue(expectedEdges.isEmpty());
    }
}