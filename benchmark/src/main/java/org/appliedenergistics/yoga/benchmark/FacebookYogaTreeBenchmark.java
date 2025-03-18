package org.appliedenergistics.yoga.benchmark;/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.appliedenergistics.yoga.capture.PrintOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Benchmark for measuring Yoga layout performance using captured layout trees.
 * Uses JMH (Java Microbenchmark Harness) for accurate performance measurement.
 */
public class FacebookYogaTreeBenchmark {
    private static final int WARMUP_REPETITIONS = 50;
    private static final int NUM_REPETITIONS = 100;

    record BenchmarkResult(long treeCreationDuration, long layoutDuration, float layoutWidth, float layoutHeight) {
        // Compute total duration
        long totalDuration() {
            return treeCreationDuration + layoutDuration;
        }
    }

    private static BenchmarkResult generateBenchmark(JsonObject capturedLayout) {
        var treeCreationBegin = System.nanoTime();
        var rootNode = FacebookTreeBuilder.deserializeTree(capturedLayout.getAsJsonObject("tree"));
        var treeCreationEnd = System.nanoTime();

        JsonObject treeOut = new JsonObject();
        FacebookNodeToString.serializeTree(treeOut, Map.of(), rootNode,             EnumSet.of(PrintOptions.STYLE, PrintOptions.CHILDREN, PrintOptions.CONFIG, PrintOptions.NODE)
        );
        try {
            Files.writeString(Paths.get("fbout.json"), new GsonBuilder().setPrettyPrinting().create().toJson(treeOut));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var layoutInputs = capturedLayout.getAsJsonObject("layout-inputs");
        var availableWidth = layoutInputs.get("available-width").getAsFloat();
        var availableHeight = layoutInputs.get("available-height").getAsFloat();
        var direction = FacebookTreeDeserialization.directionFromString(
                layoutInputs.get("owner-direction").getAsString());

        var layoutBegin = System.nanoTime();
        rootNode.setDirection(direction);
        rootNode.calculateLayout(availableWidth, availableHeight);
        var layoutEnd = System.nanoTime();

        return new BenchmarkResult(
                treeCreationEnd - treeCreationBegin,
                layoutEnd - layoutBegin,
                rootNode.getLayoutWidth(),
                rootNode.getLayoutHeight()
        );
    }

    private static void printBenchmarkResult(String name, List<Long> durations) {
        var timesInMs = new double[NUM_REPETITIONS];
        var mean = 0.0;

        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var ms = durations.get(i) / 1_000_000.0;
            timesInMs[i] = ms;
            mean += ms;
        }
        mean /= NUM_REPETITIONS;

        Arrays.sort(timesInMs);
        var median = timesInMs[NUM_REPETITIONS / 2];

        var variance = 0.0;
        for (var time : timesInMs) {
            variance += Math.pow(time - mean, 2);
        }
        variance /= NUM_REPETITIONS;
        var stddev = Math.sqrt(variance);

        System.out.printf(Locale.ROOT, "%s: median: %f ms, stddev: %f ms%n", name, median, stddev);
    }

    public static void benchmark(Path capturesDir) {
        try (var files = Files.list(capturesDir)) {
            files.filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".json"))
                    .forEach(FacebookYogaTreeBenchmark::processCaptureFile);
        } catch (IOException e) {
            System.err.println("Error accessing directory: " + e.getMessage());
        }
    }

    private static void processCaptureFile(Path captureFile) {
        var treeCreationDurations = new ArrayList<Long>(NUM_REPETITIONS);
        var layoutDurations = new ArrayList<Long>(NUM_REPETITIONS);
        var totalDurations = new ArrayList<Long>(NUM_REPETITIONS);

        try {
            var content = Files.readString(captureFile);
            var json = JsonParser.parseString(content).getAsJsonObject();
            var captureName = captureFile.getFileName().toString().replaceFirst("[.][^.]+$", "");

            System.out.println("Starting benchmark for " + captureName);

            for (int i = 0; i < WARMUP_REPETITIONS; i++) {
                var result = generateBenchmark(json);
                treeCreationDurations.add(result.treeCreationDuration());
                layoutDurations.add(result.layoutDuration());
                totalDurations.add(result.totalDuration());
            }
            treeCreationDurations.clear();
            layoutDurations.clear();
            totalDurations.clear();

            for (int i = 0; i < NUM_REPETITIONS; i++) {
                var result = generateBenchmark(json);
                treeCreationDurations.add(result.treeCreationDuration());
                layoutDurations.add(result.layoutDuration());
                totalDurations.add(result.totalDuration());
            }

            printBenchmarkResult(captureName + " tree creation", treeCreationDurations);
            printBenchmarkResult(captureName + " layout", layoutDurations);
            printBenchmarkResult(captureName + " total", totalDurations);

            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException("Error processing file " + captureFile, e);
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            benchmark(Path.of(args[0]));
        } else {
            throw new IllegalArgumentException("Expecting a path as an argument");
        }
    }
}