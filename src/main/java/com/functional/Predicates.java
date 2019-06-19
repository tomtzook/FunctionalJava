package com.functional;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class Predicates {

    private Predicates() {}

    public static <T> Predicate<T> truePredicate() {
        return (value) -> true;
    }

    public static <T> Predicate<T> falsePredicate() {
        return (value) -> false;
    }

    public static IntPredicate trueIntPredicate() {
        return (value) -> true;
    }

    public static IntPredicate falseIntPredicate() {
        return (value) -> false;
    }

    public static LongPredicate trueLongPredicate() {
        return (value) -> true;
    }

    public static LongPredicate falseLongPredicate() {
        return (value) -> false;
    }

    public static DoublePredicate trueDoublePredicate() {
        return (value) -> true;
    }

    public static DoublePredicate falseDoublePredicate() {
        return (value) -> false;
    }
}
