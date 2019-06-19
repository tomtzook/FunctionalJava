package com.functional.flow;

import com.functional.Predicates;

import java.util.OptionalLong;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;

public interface LongFlow {

    LongFlow filter(LongPredicate predicate);

    LongFlow map(LongUnaryOperator mapper);
    <U> Flow<U> mapToObj(LongFunction<? extends U> mapper);
    IntFlow mapToInt(LongToIntFunction mapper);
    DoubleFlow mapToDouble(LongToDoubleFunction mapper);

    LongFlow peek(LongConsumer action);
    void doIfPresent(LongConsumer action);

    boolean doesMatch(LongPredicate predicate);
    boolean notMatch(LongPredicate predicate);

    OptionalLong get();
    boolean hasValue();

    DoubleFlow asDoubleFlow();
    Flow<Long> boxed();

    static LongFlow of(long value) {
        return new LongFlow.StreamBackedLongFlow(LongStream.of(value));
    }

    class StreamBackedLongFlow implements LongFlow {

        private final LongStream mStream;

        public StreamBackedLongFlow(LongStream stream) {
            mStream = stream;
        }

        @Override
        public LongFlow filter(LongPredicate predicate) {
            return new StreamBackedLongFlow(mStream.filter(predicate));
        }

        @Override
        public LongFlow map(LongUnaryOperator mapper) {
            return new StreamBackedLongFlow(mStream.map(mapper));
        }

        @Override
        public <U> Flow<U> mapToObj(LongFunction<? extends U> mapper) {
            return new Flow.StreamBackedFlow<>(mStream.mapToObj(mapper));
        }

        @Override
        public IntFlow mapToInt(LongToIntFunction mapper) {
            return new IntFlow.StreamBackedIntFlow(mStream.mapToInt(mapper));
        }

        @Override
        public DoubleFlow mapToDouble(LongToDoubleFunction mapper) {
            return new DoubleFlow.StreamBackedDoubleFlow(mStream.mapToDouble(mapper));
        }

        @Override
        public LongFlow peek(LongConsumer action) {
            return new StreamBackedLongFlow(mStream.peek(action));
        }

        @Override
        public void doIfPresent(LongConsumer action) {
            mStream.forEach(action);
        }

        @Override
        public boolean doesMatch(LongPredicate predicate) {
            return mStream.anyMatch(predicate);
        }

        @Override
        public boolean notMatch(LongPredicate predicate) {
            return mStream.noneMatch(predicate);
        }

        @Override
        public OptionalLong get() {
            return mStream.findAny();
        }

        @Override
        public boolean hasValue() {
            return mStream.anyMatch(Predicates.trueLongPredicate());
        }

        @Override
        public DoubleFlow asDoubleFlow() {
            return new DoubleFlow.StreamBackedDoubleFlow(mStream.asDoubleStream());
        }

        @Override
        public Flow<Long> boxed() {
            return new Flow.StreamBackedFlow<>(mStream.boxed());
        }
    }
}
