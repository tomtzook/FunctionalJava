package com.functional.flow;

import com.functional.Predicates;

import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

public interface DoubleFlow {
    
    DoubleFlow filter(DoublePredicate predicate);

    DoubleFlow map(DoubleUnaryOperator mapper);
    <U> Flow<U> mapToObj(DoubleFunction<? extends U> mapper);
    LongFlow mapToLong(DoubleToLongFunction mapper);
    IntFlow mapToInt(DoubleToIntFunction mapper);

    DoubleFlow peek(DoubleConsumer action);
    void doIfPresent(DoubleConsumer action);

    boolean doesMatch(DoublePredicate predicate);
    boolean notMatch(DoublePredicate predicate);

    OptionalDouble get();
    boolean hasValue();

    Flow<Double> boxed();

    static DoubleFlow of(double value) {
        return new DoubleFlow.StreamBackedDoubleFlow(DoubleStream.of(value));
    }

    class StreamBackedDoubleFlow implements DoubleFlow {

        private final DoubleStream mStream;

        public StreamBackedDoubleFlow(DoubleStream stream) {
            mStream = stream;
        }

        @Override
        public DoubleFlow filter(DoublePredicate predicate) {
            return new StreamBackedDoubleFlow(mStream.filter(predicate));
        }

        @Override
        public DoubleFlow map(DoubleUnaryOperator mapper) {
            return new StreamBackedDoubleFlow(mStream.map(mapper));
        }

        @Override
        public <U> Flow<U> mapToObj(DoubleFunction<? extends U> mapper) {
            return new Flow.StreamBackedFlow<>(mStream.mapToObj(mapper));
        }

        @Override
        public LongFlow mapToLong(DoubleToLongFunction mapper) {
            return new LongFlow.StreamBackedLongFlow(mStream.mapToLong(mapper));
        }

        @Override
        public IntFlow mapToInt(DoubleToIntFunction mapper) {
            return new IntFlow.StreamBackedIntFlow(mStream.mapToInt(mapper));
        }

        @Override
        public DoubleFlow peek(DoubleConsumer action) {
            return new StreamBackedDoubleFlow(mStream.peek(action));
        }

        @Override
        public void doIfPresent(DoubleConsumer action) {
            mStream.forEach(action);
        }

        @Override
        public boolean doesMatch(DoublePredicate predicate) {
            return mStream.anyMatch(predicate);
        }

        @Override
        public boolean notMatch(DoublePredicate predicate) {
            return mStream.noneMatch(predicate);
        }

        @Override
        public OptionalDouble get() {
            return mStream.findAny();
        }

        @Override
        public boolean hasValue() {
            return mStream.anyMatch(Predicates.trueDoublePredicate());
        }

        @Override
        public Flow<Double> boxed() {
            return new Flow.StreamBackedFlow<>(mStream.boxed());
        }
    }
}
