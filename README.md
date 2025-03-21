
# Facebook Yoga (Pure Java Version)

This repository is a port of [Facebook Yoga](https://github.com/facebook/yoga) to pure Java.

The public API is a mixture of the original C++ API and the Java wrapper around the C API.

It combines and passes the entire test suite of Yoga, including the Java Wrapper test suite.

It is up to date with https://github.com/facebook/yoga/commit/6455a848a76f433bdb48b2640b7f4644774c76fd

## Download

This library is on [Maven Central](https://central.sonatype.com/artifact/org.appliedenergistics.yoga/yoga).

## Javadoc

https://appliedenergistics.org/yoga/

## Performance

When porting and using the benchmarking tool found in the C++ repository, the relative performance
between the JNI-based yoga and the pure-Java yoga is roughly 25% (in favor of JNI).
