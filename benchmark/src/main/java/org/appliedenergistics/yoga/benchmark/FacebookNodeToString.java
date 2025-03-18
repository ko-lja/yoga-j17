/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.appliedenergistics.yoga.benchmark;

import com.facebook.yoga.YogaNodeFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.facebook.yoga.YogaConstants;
import com.facebook.yoga.YogaDimension;
import com.facebook.yoga.YogaDirection;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaErrata;
import com.facebook.yoga.YogaExperimentalFeature;
import com.facebook.yoga.YogaGutter;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaUnit;
import com.facebook.yoga.YogaValue;
import com.facebook.yoga.YogaConfig;
import org.appliedenergistics.yoga.capture.PrintOptions;
import org.appliedenergistics.yoga.capture.SerializedMeasureFunc;
import org.appliedenergistics.yoga.style.StyleLength;
import org.appliedenergistics.yoga.style.StyleSizeLength;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FacebookNodeToString {

    private static void appendFloatIfNotDefault(
            JsonObject json, 
            String key, 
            float num, 
            float defaultNum) {
        if (num != defaultNum && !YogaConstants.isUndefined(num)) {
            json.addProperty(key, num);
        }
    }

    private static void appendYogaValueIfNotDefault(
            JsonObject json,
            String key,
            YogaValue value,
            YogaValue defaultValue) {
        if (!value.equals(defaultValue)) {
            if (value.unit == YogaUnit.AUTO) {
                json.addProperty(key, "auto");
            } else if (value.unit == YogaUnit.UNDEFINED) {
                json.addProperty(key, "undefined");
            } else {
                var valueObj = new JsonObject();
                valueObj.addProperty("value", value.value);
                valueObj.addProperty("unit", value.unit == YogaUnit.POINT ? "px" : "pct");
                json.add(key, valueObj);
            }
        }
    }

    private static void appendEnumValueIfNotDefault(
            JsonObject json,
            String key,
            String value,
            String defaultValue) {
        if (!value.equals(defaultValue)) {
            json.addProperty(key, value);
        }
    }

    private static void appendBoolIfNotDefault(
            JsonObject json,
            String key,
            boolean value,
            boolean defaultValue) {
        if (value != defaultValue) {
            json.addProperty(key, value);
        }
    }

    private static void appendEdges(
            JsonObject json,
            String key,
            YogaNode node,
            YogaNode defaultNode,
            EdgeValueGetter valueGetter) {
            
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-left",
                valueGetter.getValue(node, YogaEdge.LEFT),
                valueGetter.getValue(defaultNode, YogaEdge.LEFT));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-right",
                valueGetter.getValue(node, YogaEdge.RIGHT),
                valueGetter.getValue(defaultNode, YogaEdge.RIGHT));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-top",
                valueGetter.getValue(node, YogaEdge.TOP),
                valueGetter.getValue(defaultNode, YogaEdge.TOP));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-bottom",
                valueGetter.getValue(node, YogaEdge.BOTTOM),
                valueGetter.getValue(defaultNode, YogaEdge.BOTTOM));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-all",
                valueGetter.getValue(node, YogaEdge.ALL),
                valueGetter.getValue(defaultNode, YogaEdge.ALL));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-start",
                valueGetter.getValue(node, YogaEdge.START),
                valueGetter.getValue(defaultNode, YogaEdge.START));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-end",
                valueGetter.getValue(node, YogaEdge.END),
                valueGetter.getValue(defaultNode, YogaEdge.END));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-vertical",
                valueGetter.getValue(node, YogaEdge.VERTICAL),
                valueGetter.getValue(defaultNode, YogaEdge.VERTICAL));
                
        appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                key + "-horizontal",
                valueGetter.getValue(node, YogaEdge.HORIZONTAL),
                valueGetter.getValue(defaultNode, YogaEdge.HORIZONTAL));
    }

    @FunctionalInterface
    private interface EdgeValueGetter {
        YogaValue getValue(YogaNode node, YogaEdge edge);
    }

    private static YogaValue borderFloatToYogaValue(YogaNode node, YogaEdge edge) {
        float val = node.getBorder(edge);
        var unit = YogaConstants.isUndefined(val) ? YogaUnit.UNDEFINED : YogaUnit.POINT;
        return new YogaValue(val, unit);
    }

    private static void serializeMeasureFuncResults(
            JsonObject json,
            List<SerializedMeasureFunc> measureFuncs) {
            
        var measureFuncsArray = new JsonArray();
        for (var measureFunc : measureFuncs) {
            var measureFuncObj = new JsonObject();
            measureFuncObj.addProperty("width", measureFunc.inputWidth());
            measureFuncObj.addProperty("width-mode", measureFunc.widthMode().toString());
            measureFuncObj.addProperty("height", measureFunc.inputHeight());
            measureFuncObj.addProperty("height-mode", measureFunc.heightMode().toString());
            measureFuncObj.addProperty("output-width", measureFunc.outputWidth());
            measureFuncObj.addProperty("output-height", measureFunc.outputHeight());
            measureFuncObj.addProperty("duration-ns", measureFunc.durationNs());
            
            measureFuncsArray.add(measureFuncObj);
        }
        
        json.add("measure-funcs", measureFuncsArray);
    }

    private static void serializeTreeImpl(
            JsonObject json,
            Map<YogaNode, List<SerializedMeasureFunc>> nodesToMeasureFuncs,
            YogaNode node,
            Set<PrintOptions> options) {
            
        if (options.contains(PrintOptions.LAYOUT)) {
            var layoutObj = new JsonObject();
            layoutObj.addProperty("width", node.getLayoutWidth());
            layoutObj.addProperty("height", node.getLayoutHeight());
            layoutObj.addProperty("top", node.getLayoutY());
            layoutObj.addProperty("left", node.getLayoutX());
            json.add("layout", layoutObj);
        }
        
        // Create default node for comparison
        var defaultNode = YogaNodeFactory.create();

        if (options.contains(PrintOptions.STYLE)) {
            if (!json.has("style")) {
                json.add("style", new JsonObject());
            }
            
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "flex-direction",
                node.getFlexDirection().toString(),
                defaultNode.getFlexDirection().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "justify-content",
                node.getJustifyContent().toString(),
                defaultNode.getJustifyContent().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "align-items",
                node.getAlignItems().toString(),
                defaultNode.getAlignItems().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "align-content",
                node.getAlignContent().toString(),
                defaultNode.getAlignContent().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "align-self",
                node.getAlignSelf().toString(),
                defaultNode.getAlignSelf().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "flex-wrap",
                node.getWrap().toString(),
                defaultNode.getWrap().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "overflow",
                node.getOverflow().toString(),
                defaultNode.getOverflow().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "display",
                node.getDisplay().toString(),
                defaultNode.getDisplay().toString());
                
            appendEnumValueIfNotDefault(
                json.getAsJsonObject("style"),
                "position-type",
                node.getPositionType().toString(),
                defaultNode.getPositionType().toString());

            appendFloatIfNotDefault(
                json.getAsJsonObject("style"),
                "flex-grow",
                node.getFlexGrow(),
                defaultNode.getFlexGrow());
                
            appendFloatIfNotDefault(
                json.getAsJsonObject("style"),
                "flex-shrink",
                node.getFlexShrink(),
                defaultNode.getFlexShrink());
                
            appendFloatIfNotDefault(
                json.getAsJsonObject("style"),
                "flex",
                node.getFlex(),
                defaultNode.getFlex());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "flex-basis",
                node.getFlexBasis(),
                defaultNode.getFlexBasis());

            // Lambda for margin getter
            appendEdges(json, "margin", node, defaultNode, YogaNode::getMargin);
            
            // Lambda for padding getter
            appendEdges(json, "padding", node, defaultNode, YogaNode::getPadding);
            
            // Lambda for border getter
            appendEdges(json, "border", node, defaultNode, FacebookNodeToString::borderFloatToYogaValue);
            
            // Lambda for position getter
            EdgeValueGetter positionGetter = YogaNode::getPosition;
            appendEdges(json, "position", node, defaultNode, positionGetter);

            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "gap",
                node.getGap(YogaGutter.ALL),
                defaultNode.getGap(YogaGutter.ALL));
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "column-gap",
                node.getGap(YogaGutter.COLUMN),
                defaultNode.getGap(YogaGutter.COLUMN));
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "row-gap",
                node.getGap(YogaGutter.ROW),
                defaultNode.getGap(YogaGutter.ROW));

            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "width",
                node.getWidth(),
                defaultNode.getWidth());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "height",
                node.getHeight(),
                defaultNode.getHeight());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "max-width",
                node.getMaxWidth(),
                defaultNode.getMaxWidth());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "max-height",
                node.getMaxHeight(),
                defaultNode.getMaxHeight());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "min-width",
                node.getMinWidth(),
                defaultNode.getMinWidth());
                
            appendYogaValueIfNotDefault(
                json.getAsJsonObject("style"),
                "min-height",
                node.getMinHeight(),
                defaultNode.getMinHeight());
        }

        int childCount = node.getChildCount();
        if (options.contains(PrintOptions.CHILDREN) && childCount > 0) {
            var childrenArray = new JsonArray();
            
            for (var i = 0; i < childCount; i++) {
                var childObj = new JsonObject();
                serializeTreeImpl(
                    childObj,
                    nodesToMeasureFuncs,
                    node.getChildAt(i),
                    options);
                childrenArray.add(childObj);
            }
            
            json.add("children", childrenArray);
        }
    }

    private static String errataToString(Set<YogaErrata> errata) {
        if (errata.equals(YogaErrata.ALL)) {
            return "all";
        } else if (errata.equals(YogaErrata.CLASSIC)) {
            return "classic";
        } else if (errata.equals(YogaErrata.NONE)) {
            return "none";
        } else {
            return errata.toString();
        }
    }

    private static JsonElement toJson(StyleLength length) {
        return new JsonPrimitive(length.toString());
    }

    private static JsonElement toJson(StyleSizeLength length) {
        return new JsonPrimitive(length.toString());
    }

    public static void serializeTree(
            JsonObject json,
            Map<YogaNode, List<SerializedMeasureFunc>> nodesToMeasureFuncs,
            YogaNode node,
            Set<PrintOptions> options) {
            
        var treeObj = new JsonObject();
        json.add("tree", treeObj);
        serializeTreeImpl(treeObj, nodesToMeasureFuncs, node, options);
    }

    public static void serializeLayoutInputs(
            JsonObject json,
            float availableWidth,
            float availableHeight,
            YogaDirection ownerDirection) {
            
        var layoutInputsObj = new JsonObject();
        layoutInputsObj.addProperty("available-width", availableWidth);
        layoutInputsObj.addProperty("available-height", availableHeight);
        layoutInputsObj.addProperty("owner-direction", ownerDirection.toString());
        
        json.add("layout-inputs", layoutInputsObj);
    }
}
