package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;

public interface DoubleFlow {

    OptionalDouble get();
    boolean hasValue();

    DoubleFlow map(DoubleUnaryOperator mapper);
    IntFlow map(DoubleToIntFunction mapper);
    LongFlow map(DoubleToLongFunction mapper);
    <R> Flow<R> map(DoubleFunction<R> mapper);

    DoubleFlow filter(DoublePredicate predicate);
    boolean doesAnswer(DoublePredicate predicate);

    Runnable futureConsume(DoubleConsumer consumer);
    void consume(DoubleConsumer consumer);

    static DoubleFlow from(DoubleSupplier supplier) {
        return new Supplied(supplier);
    }

    static DoubleFlow of(double value) {
        return from(Suppliers.of(value));
    }

    static DoubleFlow empty() {
        return new Empty();
    }

    class Supplied implements DoubleFlow {

        private final DoubleSupplier mSupplier;

        private Supplied(DoubleSupplier supplier) {
            mSupplier = supplier;
        }

        @Override
        public OptionalDouble get() {
            return OptionalDouble.of(mSupplier.getAsDouble());
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public DoubleFlow map(DoubleUnaryOperator operator) {
            return of(operator.applyAsDouble(mSupplier.getAsDouble()));
        }

        @Override
        public IntFlow map(DoubleToIntFunction mapper) {
            return IntFlow.of(mapper.applyAsInt(mSupplier.getAsDouble()));
        }

        @Override
        public LongFlow map(DoubleToLongFunction mapper) {
            return LongFlow.of(mapper.applyAsLong(mSupplier.getAsDouble()));
        }

        @Override
        public <R> Flow<R> map(DoubleFunction<R> mapper) {
            return Flow.of(mapper.apply(mSupplier.getAsDouble()));
        }

        @Override
        public DoubleFlow filter(DoublePredicate predicate) {
            double value = mSupplier.getAsDouble();
            return predicate.test(value) ? from(mSupplier) : empty();
        }

        @Override
        public boolean doesAnswer(DoublePredicate predicate) {
            return predicate.test(mSupplier.getAsDouble());
        }

        @Override
        public Runnable futureConsume(DoubleConsumer consumer) {
            return Runnables.fromConsumer(consumer, mSupplier.getAsDouble());
        }

        @Override
        public void consume(DoubleConsumer consumer) {
            consumer.accept(mSupplier.getAsDouble());
        }
    }

    class Empty implements DoubleFlow {

        private Empty() {}

        @Override
        public OptionalDouble get() {
            return OptionalDouble.empty();
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public DoubleFlow map(DoubleUnaryOperator operator) {
            return this;
        }

        @Override
        public IntFlow map(DoubleToIntFunction mapper) {
            return IntFlow.empty();
        }

        @Override
        public LongFlow map(DoubleToLongFunction mapper) {
            return LongFlow.empty();
        }

        @Override
        public <R> Flow<R> map(DoubleFunction<R> mapper) {
            return Flow.empty();
        }

        @Override
        public DoubleFlow filter(DoublePredicate predicate) {
            return this;
        }

        @Override
        public boolean doesAnswer(DoublePredicate predicate) {
            return false;
        }

        @Override
        public Runnable futureConsume(DoubleConsumer consumer) {
            return Runnables.empty();
        }

        @Override
        public void consume(DoubleConsumer consumer) {
        }
    }
}
