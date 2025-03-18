package org.appliedenergistics.yoga.benchmark;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import com.facebook.yoga.YogaConfig;
import com.facebook.yoga.YogaConfigFactory;
import com.facebook.yoga.YogaConstants;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaExperimentalFeature;
import com.facebook.yoga.YogaGutter;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaNodeFactory;
import com.facebook.yoga.YogaUnit;
import com.facebook.yoga.YogaValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.appliedenergistics.yoga.capture.FacebookSerializedMeasureFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Utility class for building Yoga trees from JSON representations.
 * This complements the TreeDeserialization class by providing higher-level functions
 * to construct complete Yoga trees from serialized data.
 */
public class FacebookTreeBuilder {

    /**
     * Deserializes a tree from JSON and returns the root node.
     *
     * @param treeJson The JSON representation of the tree
     * @return The root YogaNode of the deserialized tree
     */
    public static YogaNode deserializeTree(JsonObject treeJson) {
        // Apply config properties
        return applyNodeProperties(treeJson);
    }

    /**
     * Applies node properties from JSON to a YogaNode.
     *
     * @param jsonObject The JSON object containing node properties
     */
    private static YogaNode applyNodeProperties(JsonObject jsonObject) {
        YogaNode node;
        var config = jsonObject.get("config");
        if (config != null && !config.isJsonNull()) {
            node = YogaNodeFactory.create(applyConfigProperties(config.getAsJsonObject()));
        } else {
            node = YogaNodeFactory.create();
        }

        // Apply style properties
        var style = jsonObject.get("style");
        if (style instanceof JsonObject styleObj) {
            applyStyleProperties(node, styleObj);
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
                    var childNode = applyNodeProperties(childElement.getAsJsonObject());
                    node.addChildAt(childNode, node.getChildCount());
                }
            }
        }

        return node;
    }

    private static final Set<String> ALLOWED_STYLE_VALUES = Set.of(
            "flex-direction",
            "justify-content",
            "align-items",
            "align-content",
            "align-self",
            "flex-wrap",
            "overflow",
            "display",
            "position-type",
            "flex-grow",
            "flex-shrink",
            "flex",
            "width",
            "height",
            "min-width",
            "min-height",
            "max-width",
            "max-height",
            "flex-basis",
            "margin-all",
            "margin-left",
            "margin-top",
            "margin-right",
            "margin-bottom",
            "margin-horizontal",
            "margin-vertical",
            "margin-start",
            "margin-end",
            "border-all",
            "border-left",
            "border-top",
            "border-right",
            "border-bottom",
            "border-horizontal",
            "border-vertical",
            "border-start",
            "border-end",
            "padding-all",
            "padding-left",
            "padding-top",
            "padding-right",
            "padding-bottom",
            "padding-horizontal",
            "padding-vertical",
            "padding-start",
            "padding-end",
            "position-left",
            "position-top",
            "position-right",
            "position-bottom",
            "position-start",
            "position-end"
    );

    /**
     * Applies style properties from JSON to a YogaNode.
     *
     * @param node  The target YogaNode
     * @param style The JSON object containing style properties
     */
    private static void applyStyleProperties(YogaNode node, JsonObject style) {
        if (!ALLOWED_STYLE_VALUES.containsAll(style.keySet())) {
            throw new IllegalArgumentException(style.keySet().toString());
        }

        // Apply flex direction
        getStringProperty(style, "flex-direction")
                .ifPresent(value -> node.setFlexDirection(FacebookTreeDeserialization.flexDirectionFromString(value)));

        // Apply justify content
        getStringProperty(style, "justify-content")
                .ifPresent(value -> node.setJustifyContent(FacebookTreeDeserialization.justifyContentFromString(value)));

        // Apply align items
        getStringProperty(style, "align-items")
                .ifPresent(value -> node.setAlignItems(FacebookTreeDeserialization.alignFromString(value)));

        // Apply align content
        getStringProperty(style, "align-content")
                .ifPresent(value -> node.setAlignContent(FacebookTreeDeserialization.alignFromString(value)));

        // Apply align self
        getStringProperty(style, "align-self")
                .ifPresent(value -> node.setAlignSelf(FacebookTreeDeserialization.alignFromString(value)));

        // Apply flex wrap
        getStringProperty(style, "flex-wrap")
                .ifPresent(value -> node.setWrap(FacebookTreeDeserialization.wrapFromString(value)));

        // Apply overflow
        getStringProperty(style, "overflow")
                .ifPresent(value -> node.setOverflow(FacebookTreeDeserialization.overflowFromString(value)));

        // Apply display
        getStringProperty(style, "display")
                .ifPresent(value -> node.setDisplay(FacebookTreeDeserialization.displayFromString(value)));

        // Apply position type
        getStringProperty(style, "position-type")
                .ifPresent(value -> node.setPositionType(FacebookTreeDeserialization.positionTypeFromString(value)));

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
                .ifPresent(value -> {
                    switch (value.unit) {
                        case AUTO -> node.setWidthAuto();
                        case STRETCH -> node.setWidthStretch();
                        case FIT_CONTENT -> node.setWidthFitContent();
                        case MAX_CONTENT -> node.setWidthMaxContent();
                        case POINT -> node.setWidth(value.value);
                        case PERCENT -> node.setWidthPercent(value.value);
                        case UNDEFINED -> node.setWidth(YogaConstants.UNDEFINED);
                        default -> throw new IllegalArgumentException("Unsupported unit for width: " + value.unit);
                    }
                });

        // Apply height
        getYogaValue(style, "height")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case AUTO -> node.setHeightAuto();
                        case STRETCH -> node.setHeightStretch();
                        case FIT_CONTENT -> node.setHeightFitContent();
                        case MAX_CONTENT -> node.setHeightMaxContent();
                        case POINT -> node.setHeight(value.value);
                        case PERCENT -> node.setHeightPercent(value.value);
                        case UNDEFINED -> node.setHeight(YogaConstants.UNDEFINED);
                        default -> throw new IllegalArgumentException("Unsupported unit for height: " + value.unit);
                    }
                });

        // Apply min width
        getYogaValue(style, "min-width")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case STRETCH -> node.setMinWidthStretch();
                        case FIT_CONTENT -> node.setMinWidthFitContent();
                        case MAX_CONTENT -> node.setMinWidthMaxContent();
                        case POINT -> node.setMinWidth(value.value);
                        case PERCENT -> node.setMinWidthPercent(value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for minwidth: " + value.unit);
                    }
                });

        // Apply min height
        getYogaValue(style, "min-height")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case STRETCH -> node.setMinHeightStretch();
                        case FIT_CONTENT -> node.setMinHeightFitContent();
                        case MAX_CONTENT -> node.setMinHeightMaxContent();
                        case POINT -> node.setMinHeight(value.value);
                        case PERCENT -> node.setMinHeightPercent(value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for minHeight: " + value.unit);
                    }
                });

        // Apply max width
        getYogaValue(style, "max-width")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case STRETCH -> node.setMaxWidthStretch();
                        case FIT_CONTENT -> node.setMaxWidthFitContent();
                        case MAX_CONTENT -> node.setMaxWidthMaxContent();
                        case POINT -> node.setMaxWidth(value.value);
                        case PERCENT -> node.setMaxWidthPercent(value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for maxWidth: " + value.unit);
                    }
                });

        // Apply max height
        getYogaValue(style, "max-height")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case STRETCH -> node.setMaxHeightStretch();
                        case FIT_CONTENT -> node.setMaxHeightFitContent();
                        case MAX_CONTENT -> node.setMaxHeightMaxContent();
                        case POINT -> node.setMaxHeight(value.value);
                        case PERCENT -> node.setMaxHeightPercent(value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for maxHeight: " + value.unit);
                    }
                });

        // Apply flex basis
        getYogaValue(style, "flex-basis")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case AUTO -> node.setFlexBasisAuto();
                        case STRETCH -> node.setFlexBasisStretch();
                        case FIT_CONTENT -> node.setFlexBasisFitContent();
                        case MAX_CONTENT -> node.setFlexBasisMaxContent();
                        case POINT -> node.setFlexBasis(value.value);
                        case PERCENT -> node.setFlexBasisPercent(value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for flexBasis: " + value.unit);
                    }
                });

        // Apply margins, paddings, borders, and positions
        applyEdgeValues(style, "margin", (e, value) -> {
            switch (value.unit) {
                case AUTO -> node.setMarginAuto(e);
                case POINT -> node.setMargin(e, value.value);
                case PERCENT -> node.setMarginPercent(e, value.value);
                default -> throw new IllegalArgumentException("Unsupported unit for margin: " + value.unit);
            }
        });
        applyEdgeValues(style, "padding", (e, value) -> {
            switch (value.unit) {
                case POINT -> node.setPadding(e, value.value);
                case PERCENT -> node.setPaddingPercent(e, value.value);
                default -> throw new IllegalArgumentException("Unsupported unit for padding: " + value.unit);
            }
        });
        applyEdgeValues(style, "border", (e, value) -> {
            switch (value.unit) {
                case POINT -> node.setBorder(e, value.value);
                default -> throw new IllegalArgumentException("Unsupported unit for border: " + value.unit);
            }
        });
        applyEdgeValues(style, "position", (e, value) -> {
            switch (value.unit) {
                case AUTO -> node.setPositionAuto(e);
                case POINT -> node.setPosition(e, value.value);
                case PERCENT -> node.setPositionPercent(e, value.value);
                default -> throw new IllegalArgumentException("Unsupported unit for position: " + value.unit);
            }
        });

        // Apply gap
        getYogaValue(style, "gap")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case POINT -> node.setGap(YogaGutter.ALL, value.value);
                        case PERCENT -> node.setGapPercent(YogaGutter.ALL, value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for gap: " + value.unit);
                    }
                });

        getYogaValue(style, "column-gap")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case POINT -> node.setGap(YogaGutter.COLUMN, value.value);
                        case PERCENT -> node.setGapPercent(YogaGutter.COLUMN, value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for column-gap: " + value.unit);
                    }
                });

        getYogaValue(style, "row-gap")
                .ifPresent(value -> {
                    switch (value.unit) {
                        case POINT -> node.setGap(YogaGutter.ROW, value.value);
                        case PERCENT -> node.setGapPercent(YogaGutter.ROW, value.value);
                        default -> throw new IllegalArgumentException("Unsupported unit for row-gap: " + value.unit);
                    }
                });
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
            var value = style.get(propertyPrefix + "-" + toString(edge));
            if (value == null) {
                continue;
            }
            if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                String valueStr = value.getAsString();
                if ("auto".equals(valueStr)) {
                    setter.accept(edge, YogaValue.parse("auto"));
                } else if ("undefined".equals(valueStr)) {
                    setter.accept(edge, YogaValue.parse("undefined"));
                }
            } else if (value.isJsonObject()) {
                JsonObject valueObj = value.getAsJsonObject();
                float floatValue = valueObj.has("value") ? valueObj.get("value").getAsFloat() : 0;
                YogaUnit unit = FacebookTreeDeserialization.unitFromJson(value);
                setter.accept(edge, new YogaValue(floatValue, unit));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Returns the string representation of the enum value.
     * @return String representation of the enum value.
     */
    private static String toString(YogaEdge edge) {
        return switch (edge) {
            case LEFT -> "left";
            case TOP -> "top";
            case RIGHT -> "right";
            case BOTTOM -> "bottom";
            case START -> "start";
            case END -> "end";
            case HORIZONTAL -> "horizontal";
            case VERTICAL -> "vertical";
            case ALL -> "all";
        };
    }

    private static final Set<String> ALLOWED_CONFIG_KEYS = Set.of(
            "use-web-defaults",
            "point-scale-factor",
            "errata",
            "experimental-features"
    );

    /**
     * Applies config properties from JSON to a YogaNode.
     *
     * @param config The JSON object containing config properties
     * @return
     */
    private static YogaConfig applyConfigProperties(JsonObject config) {
        if (!ALLOWED_CONFIG_KEYS.containsAll(config.keySet())) {
            throw new IllegalArgumentException(config.keySet().toString());
        }

        YogaConfig yogaConfig = YogaConfigFactory.create();

        // Apply use web defaults
        getBooleanProperty(config, "use-web-defaults")
                .ifPresent(yogaConfig::setUseWebDefaults);

        // Apply point scale factor
        getFloatProperty(config, "point-scale-factor")
                .ifPresent(yogaConfig::setPointScaleFactor);

        // Apply errata
        getStringProperty(config, "errata")
                .ifPresent(value -> yogaConfig.setErrata(FacebookTreeDeserialization.errataFromString(value)));

        // Apply experimental features
        if (config.has("experimental-features") && config.get("experimental-features").isJsonArray()) {
            JsonArray features = config.getAsJsonArray("experimental-features");
            for (JsonElement feature : features) {
                if (feature.isJsonPrimitive() && feature.getAsJsonPrimitive().isString()) {
                    YogaExperimentalFeature experimentalFeature =
                            FacebookTreeDeserialization.experimentalFeatureFromString(feature.getAsString());
                    yogaConfig.setExperimentalFeatureEnabled(experimentalFeature, true);
                }
            }
        }

        return yogaConfig;
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
            List<FacebookSerializedMeasureFunc> measureFuncs = new ArrayList<>();
            for (var measureFuncJson : measureFuncsNode.getAsJsonArray()) {
                measureFuncs.add(FacebookTreeDeserialization.serializedMeasureFuncFromJson(measureFuncJson.getAsJsonObject()));
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
        var node = json.get(key);
        if (node != null && !node.isJsonNull()) {
            return Optional.of(node.getAsString());
        }
        return Optional.empty();
    }

    /**
     * Gets a float property from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the float value, or empty if not found
     */
    private static Optional<Float> getFloatProperty(JsonObject json, String key) {
        var node = json.get(key);
        if (node != null && !node.isJsonNull()) {
            return Optional.of(node.getAsFloat());
        }
        return Optional.empty();
    }

    /**
     * Gets a boolean property from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the boolean value, or empty if not found
     */
    private static Optional<Boolean> getBooleanProperty(JsonObject json, String key) {
        var node = json.get(key);
        if (node != null && !node.isJsonNull()) {
            return Optional.of(node.getAsBoolean());
        }
        return Optional.empty();
    }

    /**
     * Gets a YogaValue from a JSON object.
     *
     * @param json The JSON object
     * @param key  The property key
     * @return An Optional containing the YogaValue, or empty if not found
     */
    private static Optional<YogaValue> getYogaValue(JsonObject json, String key) {
        JsonElement element = json.get(key);
        if (element == null) {
            return Optional.empty();
        }

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String valueStr = element.getAsString();
            if ("auto".equals(valueStr)) {
                return Optional.of(YogaValue.parse("auto"));
            } else if ("undefined".equals(valueStr)) {
                return Optional.of(YogaValue.parse("undefined"));
            }
        } else if (element.isJsonObject()) {
            JsonObject valueObj = element.getAsJsonObject();
            float value = valueObj.has("value") ? valueObj.get("value").getAsFloat() : 0;
            YogaUnit unit = FacebookTreeDeserialization.unitFromJson(element);
            return Optional.of(new YogaValue(value, unit));
        }

        throw new IllegalArgumentException();
    }

    private record SerializedMeasureFuncEvaluator(List<FacebookSerializedMeasureFunc> measureFuncs) implements YogaMeasureFunction {
        @Override
        public long measure(YogaNode node, float availableWidth, YogaMeasureMode widthMode, float availableHeight, YogaMeasureMode heightMode) {
            for (var measureFunc : measureFuncs) {
                if (measureFunc.inputsMatch(availableWidth, availableHeight, widthMode.intValue(), heightMode.intValue())) {
                    //std::this_thread::sleep_for(
                    //    std::chrono::nanoseconds(measureFunc.durationNs));
                    return YogaMeasureOutput.make(measureFunc.outputWidth(), measureFunc.outputHeight());
                }
            }

            System.err.println("No saved invocation of measurement function");
            return YogaMeasureOutput.make(10, 10);
        }
    }
}
