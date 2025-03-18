package org.appliedenergistics.yoga.benchmark;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.appliedenergistics.yoga.YogaAlign;
import org.appliedenergistics.yoga.YogaConstants;
import org.appliedenergistics.yoga.YogaDirection;
import org.appliedenergistics.yoga.YogaDisplay;
import org.appliedenergistics.yoga.YogaErrata;
import org.appliedenergistics.yoga.YogaExperimentalFeature;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaJustify;
import org.appliedenergistics.yoga.YogaMeasureMode;
import org.appliedenergistics.yoga.YogaOverflow;
import org.appliedenergistics.yoga.YogaPositionType;
import org.appliedenergistics.yoga.YogaUnit;
import org.appliedenergistics.yoga.YogaWrap;
import org.appliedenergistics.yoga.capture.SerializedMeasureFunc;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

public class TreeDeserialization {

    private static boolean isAuto(JsonElement element) {
        return element.isJsonPrimitive() && 
               element.getAsJsonPrimitive().isString() && 
               "auto".equals(element.getAsString());
    }

    private static boolean isUndefined(JsonElement element) {
        return element.isJsonPrimitive() && 
               element.getAsJsonPrimitive().isString() && 
               "undefined".equals(element.getAsString());
    }

    private static String invalidArgumentMessage(String arg, String enumName) {
        return arg + " does not represent any " + enumName + " values";
    }

    private static float floatFromJson(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return YogaConstants.UNDEFINED;
        }
        return element.getAsFloat();
    }

    public static YogaFlexDirection flexDirectionFromString(String str) {
        return switch (str) {
            case "row" -> YogaFlexDirection.ROW;
            case "row-reverse" -> YogaFlexDirection.ROW_REVERSE;
            case "column" -> YogaFlexDirection.COLUMN;
            case "column-reverse" -> YogaFlexDirection.COLUMN_REVERSE;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaFlexDirection"));
        };
    }

    public static YogaJustify justifyContentFromString(String str) {
        return switch (str) {
            case "flex-start" -> YogaJustify.FLEX_START;
            case "center" -> YogaJustify.CENTER;
            case "flex-end" -> YogaJustify.FLEX_END;
            case "space-between" -> YogaJustify.SPACE_BETWEEN;
            case "space-around" -> YogaJustify.SPACE_AROUND;
            case "space-evenly" -> YogaJustify.SPACE_EVENLY;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaJustify"));
        };
    }

    public static YogaAlign alignFromString(String str) {
        return switch (str) {
            case "auto" -> YogaAlign.AUTO;
            case "flex-start" -> YogaAlign.FLEX_START;
            case "center" -> YogaAlign.CENTER;
            case "flex-end" -> YogaAlign.FLEX_END;
            case "stretch" -> YogaAlign.STRETCH;
            case "baseline" -> YogaAlign.BASELINE;
            case "space-between" -> YogaAlign.SPACE_BETWEEN;
            case "space-around" -> YogaAlign.SPACE_AROUND;
            case "space-evenly" -> YogaAlign.SPACE_EVENLY;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaAlign"));
        };
    }

    public static YogaWrap wrapFromString(String str) {
        return switch (str) {
            case "no-wrap" -> YogaWrap.NO_WRAP;
            case "wrap" -> YogaWrap.WRAP;
            case "wrap-reverse" -> YogaWrap.WRAP_REVERSE;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaWrap"));
        };
    }

    public static YogaOverflow overflowFromString(String str) {
        return switch (str) {
            case "visible" -> YogaOverflow.VISIBLE;
            case "hidden" -> YogaOverflow.HIDDEN;
            case "scroll" -> YogaOverflow.SCROLL;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaOverflow"));
        };
    }

    public static YogaDisplay displayFromString(String str) {
        return switch (str) {
            case "flex" -> YogaDisplay.FLEX;
            case "none" -> YogaDisplay.NONE;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaDisplay"));
        };
    }

    public static YogaPositionType positionTypeFromString(String str) {
        return switch (str) {
            case "static" -> YogaPositionType.STATIC;
            case "relative" -> YogaPositionType.RELATIVE;
            case "absolute" -> YogaPositionType.ABSOLUTE;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaPositionType"));
        };
    }

    public static YogaUnit unitFromJson(JsonElement element) {
        if (isAuto(element)) {
            return YogaUnit.AUTO;
        }
        if (isUndefined(element)) {
            return YogaUnit.UNDEFINED;
        }

        var jsonObject = element.getAsJsonObject();
        var unit = jsonObject.get("unit").getAsString();
        
        return switch (unit) {
            case "px" -> YogaUnit.POINT;
            case "pct" -> YogaUnit.PERCENT;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(unit, "YogaUnit"));
        };
    }

    public static Set<YogaErrata> errataFromString(String str) {
        return switch (str) {
            case "none" -> YogaErrata.NONE;
            case "all" -> YogaErrata.ALL;
            case "classic" -> YogaErrata.CLASSIC;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaErrata"));
        };
    }

    public static YogaExperimentalFeature experimentalFeatureFromString(String str) {
        return switch (str) {
            case "web-flex-basis" -> YogaExperimentalFeature.WEB_FLEX_BASIS;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaExperimentalFeature"));
        };
    }

    public static String edgeStringFromPropertyName(String key, String propertyName) {
        return key.substring(propertyName.length() + 1);
    }

    public static YogaDirection directionFromString(String str) {
        return switch (str) {
            case "ltr" -> YogaDirection.LTR;
            case "rtl" -> YogaDirection.RTL;
            case "inherit" -> YogaDirection.INHERIT;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaDirection"));
        };
    }

    public static YogaMeasureMode measureModeFromString(String str) {
        return switch (str) {
            case "at-most" -> YogaMeasureMode.AT_MOST;
            case "exactly" -> YogaMeasureMode.EXACTLY;
            case "undefined" -> YogaMeasureMode.UNDEFINED;
            default -> throw new IllegalArgumentException(invalidArgumentMessage(str, "YogaMeasureMode"));
        };
    }

    public static SerializedMeasureFunc serializedMeasureFuncFromJson(JsonObject jsonObject) {
        return new SerializedMeasureFunc(
            floatFromJson(jsonObject.get("width")),
            measureModeFromString(jsonObject.get("width-mode").getAsString()),
            floatFromJson(jsonObject.get("height")),
            measureModeFromString(jsonObject.get("height-mode").getAsString()),
            floatFromJson(jsonObject.get("output-width")),
            floatFromJson(jsonObject.get("output-height")),
            jsonObject.get("duration-ns").getAsLong()
        );
    }
}
