package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.OptionalLong;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

public interface LongFlow {

    OptionalLong get();

    LongFlow map(LongUnaryOperator operator);
    IntFlow map(LongToIntFunction mapper);
    DoubleFlow map(LongToDoubleFunction mapper);
    <R> Flow<R> map(LongFunction<R> mapper);

    LongFlow filter(LongPredicate predicate);

    Runnable futureConsume(LongConsumer consumer);
    void consume(LongConsumer consumer);

    static LongFlow from(LongSupplier supplier) {
        return new SuppliedFlow(supplier);
    }

    static LongFlow of(long value) {
        return from(Suppliers.of(value));
    }

    static LongFlow empty() {
        return new EmptyFlow();
    }

    class SuppliedFlow implements LongFlow {

        private final LongSupplier mSupplier;

        private SuppliedFlow(LongSupplier supplier) {
            mSupplier = supplier;
        }

        @Override
        public OptionalLong get() {
            return OptionalLong.of(mSupplier.getAsLong());
        }

        @Override
        public LongFlow map(LongUnaryOperator operator) {
            return of(operator.applyAsLong(mSupplier.getAsLong()));
        }

        @Override
        public IntFlow map(LongToIntFunction mapper) {
            return IntFlow.of(mapper.applyAsInt(mSupplier.getAsLong()));
        }

        @Override
        public DoubleFlow map(LongToDoubleFunction mapper) {
            return DoubleFlow.of(mapper.applyAsDouble(mSupplier.getAsLong()));
        }

        @Override
        public <R> Flow<R> map(LongFunction<R> mapper) {
            return Flow.of(mapper.apply(mSupplier.getAsLong()));
        }

        @Override
        public LongFlow filter(LongPredicate predicate) {
            long value = mSupplier.getAsLong();
            return predicate.test(value) ? from(mSupplier) : empty();
        }

        @Override
        public Runnable futureConsume(LongConsumer consumer) {
            return Runnables.fromConsumer(consumer, mSupplier.getAsLong());
        }

        @Override
        public void consume(LongConsumer consumer) {
            consumer.accept(mSupplier.getAsLong());
        }
    }

    class EmptyFlow implements LongFlow {

        private EmptyFlow() {}

        @Override
        public OptionalLong get() {
            return OptionalLong.empty();
        }

        @Override
        public LongFlow map(LongUnaryOperator operator) {
            return this;
        }

        @Override
        public IntFlow map(LongToIntFunction mapper) {
            return IntFlow.empty();
        }

        @Override
        public DoubleFlow map(LongToDoubleFunction mapper) {
            return DoubleFlow.empty();
        }

        @Override
        public <R> Flow<R> map(LongFunction<R> mapper) {
            return Flow.empty();
        }

        @Override
        public LongFlow filter(LongPredicate predicate) {
            return this;
        }

        @Override
        public Runnable futureConsume(LongConsumer consumer) {
            return Runnables.empty();
        }

        @Override
        public void consume(LongConsumer consumer) {
        }
    }
}
