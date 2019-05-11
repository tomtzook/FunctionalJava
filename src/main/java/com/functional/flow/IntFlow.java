package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

public interface IntFlow {

    OptionalInt get();
    boolean hasValue();

    IntFlow map(IntUnaryOperator operator);
    DoubleFlow map(IntToDoubleFunction mapper);
    LongFlow map(IntToLongFunction mapper);
    <R> Flow<R> map(IntFunction<R> mapper);

    IntFlow filter(IntPredicate predicate);
    boolean doesAnswer(IntPredicate predicate);

    Runnable futureConsume(IntConsumer consumer);
    void consume(IntConsumer consumer);

    static IntFlow from(IntSupplier supplier) {
        return new SuppliedFlow(supplier);
    }

    static IntFlow of(int value) {
        return from(Suppliers.of(value));
    }

    static IntFlow empty() {
        return new EmptyFlow();
    }

    class SuppliedFlow implements IntFlow {

        private final IntSupplier mSupplier;

        private SuppliedFlow(IntSupplier supplier) {
            mSupplier = supplier;
        }

        @Override
        public OptionalInt get() {
            return OptionalInt.of(mSupplier.getAsInt());
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public IntFlow map(IntUnaryOperator operator) {
            return from(()->operator.applyAsInt(mSupplier.getAsInt()));
        }

        @Override
        public DoubleFlow map(IntToDoubleFunction mapper) {
            return DoubleFlow.from(()->mapper.applyAsDouble(mSupplier.getAsInt()));
        }

        @Override
        public LongFlow map(IntToLongFunction mapper) {
            return LongFlow.from(()->mapper.applyAsLong(mSupplier.getAsInt()));
        }

        @Override
        public <R> Flow<R> map(IntFunction<R> mapper) {
            return Flow.from(()->mapper.apply(mSupplier.getAsInt()));
        }

        @Override
        public IntFlow filter(IntPredicate predicate) {
            int value = mSupplier.getAsInt();
            return predicate.test(value) ? from(mSupplier) : empty();
        }

        @Override
        public boolean doesAnswer(IntPredicate predicate) {
            return predicate.test(mSupplier.getAsInt());
        }

        @Override
        public Runnable futureConsume(IntConsumer consumer) {
            return Runnables.fromConsumer(consumer, mSupplier.getAsInt());
        }

        @Override
        public void consume(IntConsumer consumer) {
            consumer.accept(mSupplier.getAsInt());
        }
    }

    class EmptyFlow implements IntFlow {

        private EmptyFlow() {}

        @Override
        public OptionalInt get() {
            return OptionalInt.empty();
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public IntFlow map(IntUnaryOperator operator) {
            return this;
        }

        @Override
        public DoubleFlow map(IntToDoubleFunction mapper) {
            return DoubleFlow.empty();
        }

        @Override
        public LongFlow map(IntToLongFunction mapper) {
            return LongFlow.empty();
        }

        @Override
        public <R> Flow<R> map(IntFunction<R> mapper) {
            return Flow.empty();
        }

        @Override
        public IntFlow filter(IntPredicate predicate) {
            return this;
        }

        @Override
        public boolean doesAnswer(IntPredicate predicate) {
            return false;
        }

        @Override
        public Runnable futureConsume(IntConsumer consumer) {
            return Runnables.empty();
        }

        @Override
        public void consume(IntConsumer consumer) {
        }
    }
}
