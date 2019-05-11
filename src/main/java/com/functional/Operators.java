package com.functional;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Operators {

    private Operators() {}

    public static IntUnaryOperator testOrDefault(IntPredicate predicate, int defaultValue) {
        return (value) -> predicate.test(value) ? value : defaultValue;
    }

    public static DoubleUnaryOperator testOrDefault(DoublePredicate predicate, double defaultValue) {
        return (value) -> predicate.test(value) ? value : defaultValue;
    }

    public static LongUnaryOperator testOrDefault(LongPredicate predicate, long defaultValue) {
        return (value) -> predicate.test(value) ? value : defaultValue;
    }

    public static <T> UnaryOperator<T> testOrDefault(Predicate<T> predicate, T defaultValue) {
        return (value) -> predicate.test(value) ? value : defaultValue;
    }
}
