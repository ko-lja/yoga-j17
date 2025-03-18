/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.appliedenergistics.yoga.utils;

import org.appliedenergistics.yoga.YogaMeasureMode;
import org.appliedenergistics.yoga.YogaMeasureFunction;
import org.appliedenergistics.yoga.YogaNode;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaSize;

public class TestUtils {

  public static class intrinsicMeasureFunction implements YogaMeasureFunction {

    private static float widthPerChar = 10;
    private static float heightPerChar = 10;

    @Override
    public YogaSize measure(
        YogaNode node,
        float width,
        YogaMeasureMode widthMode,
        float height,
        YogaMeasureMode heightMode) {
      String text = (String) node.getData();
      float measuredWidth;
      float measuredHeight;

      if (widthMode == YogaMeasureMode.EXACTLY) {
        measuredWidth = width;
      } else if (widthMode == YogaMeasureMode.AT_MOST) {
        measuredWidth = Math.min(text.length() * widthPerChar, width);
      } else {
        measuredWidth = text.length() * widthPerChar;
      }

      if (heightMode == YogaMeasureMode.EXACTLY) {
        measuredHeight = height;
      } else if (heightMode == YogaMeasureMode.AT_MOST) {
        measuredHeight =
            Math.min(
                caclulateHeight(text, node.getStyle().getFlexDirection() == YogaFlexDirection.COLUMN
                  ? measuredWidth 
                  : Math.max(measuredWidth, getWidestWordWidth(text))), height);
      } else {
        measuredHeight = caclulateHeight(text, node.getStyle().getFlexDirection() == YogaFlexDirection.COLUMN
          ? measuredWidth 
          : Math.max(measuredWidth, getWidestWordWidth(text)));
      }

      return new YogaSize(measuredWidth, measuredHeight);
    }

    static float getWidestWordWidth(String text) {
      int widestWordLength = 0;
      String[] words = text.split(" ");
      for (String word : words) {
        int wordLength = word.length();
        if (widestWordLength < wordLength) {
          widestWordLength = wordLength;
        }
      }
      return (float) widestWordLength * widthPerChar;
    }

    static float caclulateHeight(String text, float measuredWidth) {
      if (text.length() * widthPerChar <= measuredWidth) {
        return heightPerChar;
      }

      String[] words = text.split(" ");
      float lines = 1;
      float currentLineLength = 0;
      for (String word : words) {
        float wordWidth = word.length() * widthPerChar;
        if (wordWidth > measuredWidth) {
          if (currentLineLength > 0) {
            lines++;
          }
          lines++;
          currentLineLength = 0;
        } else if (currentLineLength + wordWidth <= measuredWidth) {
          currentLineLength += wordWidth + widthPerChar;
        } else {
          lines++;
          currentLineLength = wordWidth + widthPerChar;
        }
      }
      return (currentLineLength == 0 ? lines - 1 : lines) * heightPerChar;
    }
  }
}
