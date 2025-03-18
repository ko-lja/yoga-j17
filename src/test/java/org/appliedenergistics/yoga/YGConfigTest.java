package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YGConfigTest {
    
    private MutableYogaConfig config;
    private static YogaNode clonedNode = new YogaNode();
    
    @BeforeEach
    public void setUp() {
        config = YogaConfig.create();
    }
    
    @AfterEach
    public void tearDown() {
        config = null;
    }
    
    private static YogaNode cloneNode(YogaNode node, YogaNode owner, int childIndex) {
        return clonedNode;
    }
    
    private static YogaNode doNotClone(YogaNode node, YogaNode owner, int childIndex) {
        return null;
    }
    
    @Test
    public void testUsesValuesProvidedByCloningCallback() {
        config.setCloneNodeCallback(YGConfigTest::cloneNode);
        
        YogaNode node = new YogaNode();
        YogaNode owner = new YogaNode();
        YogaNode clone = config.cloneNode(node, owner, 0);
        
        assertSame(clonedNode, clone);
    }
    
    @Test
    public void testFallsBackToRegularCloningIfCallbackReturnsNull() {
        config.setCloneNodeCallback(YGConfigTest::doNotClone);
        
        YogaNode node = new YogaNode();
        YogaNode owner = new YogaNode();
        YogaNode clone = config.cloneNode(node, owner, 0);
        
        assertNotNull(clone);
        assertNotSame(node, clone);
    }
}