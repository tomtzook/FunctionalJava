package com.functional.flow;

import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public interface IntFlow {

    IntFlow filter(IntPredicate predicate);

    IntFlow map(IntUnaryOperator mapper);
    <U> Flow<U> mapToObj(IntFunction<? extends U> mapper);
    LongFlow mapToLong(IntToLongFunction mapper);
    DoubleFlow mapToDouble(IntToDoubleFunction mapper);

    IntFlow peek(IntConsumer action);
    void doIfPresent(IntConsumer action);

    boolean doesMatch(IntPredicate predicate);
    boolean notMatch(IntPredicate predicate);

    OptionalInt get();

    LongFlow asLongFlow();
    DoubleFlow asDoubleFlow();
    Flow<Integer> boxed();

    static IntFlow of(int value) {
        return new StreamBackedIntFlow(IntStream.of(value));
    }

    class StreamBackedIntFlow implements IntFlow {

        private final IntStream mStream;

        public StreamBackedIntFlow(IntStream stream) {
            mStream = stream;
        }

        @Override
        public IntFlow filter(IntPredicate predicate) {
            return new StreamBackedIntFlow(mStream.filter(predicate));
        }

        @Override
        public IntFlow map(IntUnaryOperator mapper) {
            return new StreamBackedIntFlow(mStream.map(mapper));
        }

        @Override
        public <U> Flow<U> mapToObj(IntFunction<? extends U> mapper) {
            return new Flow.StreamBackedFlow<>(mStream.mapToObj(mapper));
        }

        @Override
        public LongFlow mapToLong(IntToLongFunction mapper) {
            return new LongFlow.StreamBackedLongFlow(mStream.mapToLong(mapper));
        }

        @Override
        public DoubleFlow mapToDouble(IntToDoubleFunction mapper) {
            return new DoubleFlow.StreamBackedDoubleFlow(mStream.mapToDouble(mapper));
        }

        @Override
        public IntFlow peek(IntConsumer action) {
            return new StreamBackedIntFlow(mStream.peek(action));
        }

        @Override
        public void doIfPresent(IntConsumer action) {
            mStream.forEach(action);
        }

        @Override
        public boolean doesMatch(IntPredicate predicate) {
            return mStream.anyMatch(predicate);
        }

        @Override
        public boolean notMatch(IntPredicate predicate) {
            return mStream.noneMatch(predicate);
        }

        @Override
        public OptionalInt get() {
            return mStream.findAny();
        }

        @Override
        public LongFlow asLongFlow() {
            return new LongFlow.StreamBackedLongFlow(mStream.asLongStream());
        }

        @Override
        public DoubleFlow asDoubleFlow() {
            return new DoubleFlow.StreamBackedDoubleFlow(mStream.asDoubleStream());
        }

        @Override
        public Flow<Integer> boxed() {
            return new Flow.StreamBackedFlow<>(mStream.boxed());
        }
    }
}
