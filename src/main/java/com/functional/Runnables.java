package com.functional;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class Runnables {

    private Runnables() {}

    public static Runnable empty() {
        return () -> { };
    }

    public static Runnable fromConsumer(IntConsumer consumer, int value) {
        return ()->consumer.accept(value);
    }

    public static Runnable fromConsumer(DoubleConsumer consumer, double value) {
        return ()->consumer.accept(value);
    }

    public static Runnable fromConsumer(LongConsumer consumer, long value) {
        return ()->consumer.accept(value);
    }

    public static <T> Runnable fromConsumer(Consumer<T> consumer, T value) {
        return ()->consumer.accept(value);
    }
}
