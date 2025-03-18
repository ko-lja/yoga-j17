package org.appliedenergistics.yoga.benchmark;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.appliedenergistics.yoga.LogLevel;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaExperimentalFeature;
import org.appliedenergistics.yoga.YogaGutter;
import org.appliedenergistics.yoga.YogaMeasureFunction;
import org.appliedenergistics.yoga.YogaMeasureMode;
import org.appliedenergistics.yoga.YogaNode;
import org.appliedenergistics.yoga.YogaSize;
import org.appliedenergistics.yoga.YogaUnit;
import org.appliedenergistics.yoga.YogaValue;
import org.appliedenergistics.yoga.capture.SerializedMeasureFunc;
import org.appliedenergistics.yoga.config.MutableYogaConfig;
import org.appliedenergistics.yoga.config.YogaConfig;
import org.appliedenergistics.yoga.style.StyleLength;
import org.appliedenergistics.yoga.style.StyleSizeLength;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Utility class for building Yoga trees from JSON representations.
 * This complements the TreeDeserialization class by providing higher-level functions
 * to construct complete Yoga trees from serialized data.
 */
public class TreeBuilder {

    /**
     * Deserializes a tree from JSON and returns the root node.
     *
     * @param treeJson The JSON representation of the tree
     * @return The root YogaNode of the deserialized tree
     */
    public static YogaNode deserializeTree(JsonObject treeJson) {
        YogaNode rootNode = new YogaNode();
        applyNodeProperties(rootNode, treeJson);
        return rootNode;
    }

    /**
     * Applies node properties from JSON to a YogaNode.
     *
     * @param node       The target YogaNode
     * @param jsonObject The JSON object containing node properties
     */
    private static void applyNodeProperties(YogaNode node, JsonObject jsonObject) {
        // Apply style properties
        var style = jsonObject.get("style");
        if (style instanceof JsonObject styleObj) {
            applyStyleProperties(node, styleObj);
        }

        // Apply config properties
        if (jsonObject.has("config")) {
            var config = YogaConfig.create();
            applyConfigProperties(config, jsonObject.getAsJsonObject("config"));
            node.setConfig(config);
        }

        // Apply node-specific properties
        var nodeJson = jsonObject.get("node");
        if (nodeJson != null && !nodeJson.isJsonNull()) {
            applyNodeSpecificProperties(node, nodeJson.getAsJsonObject());
        }

        // Process children recursively
        if (jsonObject.has("children") && jsonObject.get("children").isJsonArray()) {
            JsonArray childrenArray = jsonObject.getAsJsonArray("children");
            for (JsonElement childElement : childrenArray) {
                if (childElement.isJsonObject()) {
                    YogaNode childNode = new YogaNode();
                    applyNodeProperties(childNode, childElement.getAsJsonObject());
                    node.addChildAt(childNode, node.getChildCount());
                }
            }
        }
    }

    /**
     * Applies style properties from JSON to a YogaNode.
     *
     * @param node  The target YogaNode
     * @param style The JSON object containing style properties
     */
    private static void applyStyleProperties(YogaNode node, JsonObject style) {
        // Apply flex direction
        getStringProperty(style, "flex-direction")
                .ifPresent(value -> node.setFlexDirection(TreeDeserialization.flexDirectionFromString(value)));

        // Apply justify content
        getStringProperty(style, "justify-content")
                .ifPresent(value -> node.setJustifyContent(TreeDeserialization.justifyContentFromString(value)));

        // Apply align items
        getStringProperty(style, "align-items")
                .ifPresent(value -> node.setAlignItems(TreeDeserialization.alignFromString(value)));

        // Apply align content
        getStringProperty(style, "align-content")
                .ifPresent(value -> node.setAlignContent(TreeDeserialization.alignFromString(value)));

        // Apply align self
        getStringProperty(style, "align-self")
                .ifPresent(value -> node.setAlignSelf(TreeDeserialization.alignFromString(value)));

        // Apply flex wrap
        getStringProperty(style, "flex-wrap")
                .ifPresent(value -> node.getStyle().setFlexWrap(TreeDeserialization.wrapFromString(value)));

        // Apply overflow
        getStringProperty(style, "overflow")
                .ifPresent(value -> node.setOverflow(TreeDeserialization.overflowFromString(value)));

        // Apply display
        getStringProperty(style, "display")
                .ifPresent(value -> node.setDisplay(TreeDeserialization.displayFromString(value)));

        // Apply position type
        getStringProperty(style, "position-type")
                .ifPresent(value -> node.setPositionType(TreeDeserialization.positionTypeFromString(value)));

        // Apply flex grow
        getFloatProperty(style, "flex-grow")
                .ifPresent(node::setFlexGrow);

        // Apply flex shrink
        getFloatProperty(style, "flex-shrink")
                .ifPresent(node::setFlexShrink);

        // Apply flex
        getFloatProperty(style, "flex")
                .ifPresent(node::setFlex);

        // Apply width
        getYogaValue(style, "width")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setWidth);

        // Apply height
        getYogaValue(style, "height")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setHeight);

        // Apply min width
        getYogaValue(style, "min-width")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setMinWidth);

        // Apply min height
        getYogaValue(style, "min-height")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setMinHeight);

        // Apply max width
        getYogaValue(style, "max-width")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setMaxWidth);

        // Apply max height
        getYogaValue(style, "max-height")
                .map(StyleSizeLength::fromYogaValue)
                .ifPresent(node::setMaxHeight);

        // Apply flex basis
        getYogaValue(style, "flex-basis")
                .map(v -> v.value)
                .ifPresent(node::setFlexBasis);

        // Apply margins, paddings, borders, and positions
        applyEdgeValues(style, "margin", (e, v) -> node.setMargin(e, StyleLength.fromYogaValue(v)));
        applyEdgeValues(style, "padding", (e, v) -> node.setPadding(e, StyleLength.fromYogaValue(v)));
        applyEdgeValues(style, "border", (e, v) -> node.setBorder(e, v.value));
        applyEdgeValues(style, "position", (e, v) -> node.setPosition(e, StyleLength.fromYogaValue(v)));

        // Apply gap
        getYogaValue(style, "gap")
                .map(StyleLength::fromYogaValue)
                .ifPresent(value -> node.setGap(YogaGutter.ALL, value));

        getYogaValue(style, "column-gap")
                .map(StyleLength::fromYogaValue)
                .ifPresent(value -> node.setGap(YogaGutter.COLUMN, value));

        getYogaValue(style, "row-gap")
                .map(StyleLength::fromYogaValue)
                .ifPresent(value -> node.setGap(YogaGutter.ROW, value));
    }

    /**
     * Applies edge values (margin, padding, border, position) from JSON to a YogaNode.
     *
     * @param style          The JSON object containing style properties
     * @param propertyPrefix The property prefix (e.g., "margin", "padding")
     * @param setter         The setter function for the edge value
     */
    private static void applyEdgeValues(
            JsonObject style,
            String propertyPrefix,
            BiConsumer<YogaEdge, YogaValue> setter) {

        // For each property that starts with the prefix followed by a dash
        for (var edge : YogaEdge.values()) {
            var value = style.get(propertyPrefix + "-" + edge.toString());
            if (value != null) {
                if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                    String valueStr = value.getAsString();
                    if ("auto".equals(valueStr)) {
                        setter.accept(edge, YogaValue.AUTO);
                    } else if ("undefined".equals(valueStr)) {
                        setter.accept(edge, YogaValue.UNDEFINED);
                    }
                } else if (value.isJsonObject()) {
                    JsonObject valueObj = value.getAsJsonObject();
                    float floatValue = valueObj.has("value") ? valueObj.get("value").getAsFloat() : 0;
                    YogaUnit unit = TreeDeserialization.unitFromJson(value);
                    setter.accept(edge, new YogaValue(floatValue, unit));
                }
            }
        }
    }

    /**
     * Applies config properties from JSON to a YogaNode.
     *
     * @param config The target YogaConfig
     * @param json   The JSON object containing config properties
     */
    private static void applyConfigProperties(MutableYogaConfig config, JsonObject json) {
        // Apply use web defaults
        getBooleanProperty(json, "use-web-defaults")
                .ifPresent(config::setUseWebDefaults);

        // Apply point scale factor
        getFloatProperty(json, "point-scale-factor")
                .ifPresent(config::setPointScaleFactor);

        // Apply errata
        getStringProperty(json, "errata")
                .ifPresent(value -> config.setErrata(TreeDeserialization.errataFromString(value)));

        // Apply experimental features
        if (json.has("experimental-features") && json.get("experimental-features").isJsonArray()) {
            JsonArray features = json.getAsJsonArray("experimental-features");
            for (JsonElement feature : features) {
                if (feature.isJsonPrimitive() && feature.getAsJsonPrimitive().isString()) {
                    YogaExperimentalFeature experimentalFeature =
                            TreeDeserialization.experimentalFeatureFromString(feature.getAsString());
                    config.setExperimentalFeatureEnabled(experimentalFeature, true);
                }
            }
        }
    }

    /**
     * Applies node-specific properties from JSON to a YogaNode.
     *
     * @param node      The target YogaNode
     * @param nodeProps The JSON object containing node properties
     */
    private static void applyNodeSpecificProperties(YogaNode node, JsonObject nodeProps) {
        // Apply always forms containing block
        getBooleanProperty(nodeProps, "always-forms-containing-block")
                .ifPresent(node::setAlwaysFormsContainingBlock);

        var measureFuncsNode = nodeProps.get("measure-funcs");
        if (measureFuncsNode != null && !measureFuncsNode.isJsonNull()) {
            List<SerializedMeasureFunc> measureFuncs = new ArrayList<>();
            for (var measureFuncJson : measureFuncsNode.getAsJsonArray()) {
                measureFuncs.add(TreeDeserialization.serializedMeasureFuncFromJson(measureFuncJson.getAsJsonObject()));
            }
            node.setMeasureFunction(new SerializedMeasureFuncEvaluator(measureFuncs));
        }
    }

    /**
     * Gets a string property from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the string value, or empty if not found
     */
    private static Optional<String> getStringProperty(JsonObject json, String key) {
        return Optional.ofNullable(json.get(key))
                .filter(JsonElement::isJsonPrimitive)
                .filter(element -> element.getAsJsonPrimitive().isString())
                .map(JsonElement::getAsString);
    }

    /**
     * Gets a float property from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the float value, or empty if not found
     */
    private static Optional<Float> getFloatProperty(JsonObject json, String key) {
        return Optional.ofNullable(json.get(key))
                .filter(JsonElement::isJsonPrimitive)
                .filter(element -> element.getAsJsonPrimitive().isNumber())
                .map(JsonElement::getAsFloat);
    }

    /**
     * Gets a boolean property from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the boolean value, or empty if not found
     */
    private static Optional<Boolean> getBooleanProperty(JsonObject json, String key) {
        return Optional.ofNullable(json.get(key))
                .filter(JsonElement::isJsonPrimitive)
                .filter(element -> element.getAsJsonPrimitive().isBoolean())
                .map(JsonElement::getAsBoolean);
    }

    /**
     * Gets a YogaValue from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the YogaValue, or empty if not found
     */
    private static Optional<YogaValue> getYogaValue(JsonObject json, String key) {
        if (!json.has(key)) {
            return Optional.empty();
        }

        JsonElement element = json.get(key);

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String valueStr = element.getAsString();
            if ("auto".equals(valueStr)) {
                return Optional.of(YogaValue.AUTO);
            } else if ("undefined".equals(valueStr)) {
                return Optional.of(YogaValue.UNDEFINED);
            }
        } else if (element.isJsonObject()) {
            JsonObject valueObj = element.getAsJsonObject();
            float value = valueObj.has("value") ? valueObj.get("value").getAsFloat() : 0;
            YogaUnit unit = TreeDeserialization.unitFromJson(element);
            return Optional.of(new YogaValue(value, unit));
        }

        return Optional.empty();
    }

    private record SerializedMeasureFuncEvaluator(
            List<SerializedMeasureFunc> measureFuncs) implements YogaMeasureFunction {
        @Override
        public YogaSize measure(YogaNode node, float availableWidth, YogaMeasureMode widthMode, float availableHeight, YogaMeasureMode heightMode) {
            for (var measureFunc : measureFuncs) {
                if (measureFunc.inputsMatch(availableWidth, availableHeight, widthMode, heightMode)) {
                    //std::this_thread::sleep_for(
                    //    std::chrono::nanoseconds(measureFunc.durationNs));
                    return new YogaSize(measureFunc.outputWidth(), measureFunc.outputHeight());
                }
            }

            node.getConfig().log(node, LogLevel.ERROR, "Trying to measure a node that wasn't serialized");
            return new YogaSize(10, 10);
        }
    }
}
