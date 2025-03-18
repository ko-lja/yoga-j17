/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.config.MutableYogaConfig;

import java.util.Arrays;

public class TestParametrization {
  public static Iterable<NodeFactory> nodeFactories() {
    NodeFactory nodeFactory = new NodeFactory() {
      @Override
      public YogaNode create() {
        return new YogaNode();
      }

      @Override
      public YogaNode create(MutableYogaConfig config) {
        return new YogaNode(config);
      }

      @Override
      public String toString() {
        return "JNI";
      }
    };
    return Arrays.asList(nodeFactory);
  }


  public interface NodeFactory {
    YogaNode create();
    YogaNode create(MutableYogaConfig config);
  }
}
