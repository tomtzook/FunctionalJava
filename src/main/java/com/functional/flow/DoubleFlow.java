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
import java.util.function.Supplier;

public interface DoubleFlow {

    DoubleFlow map(DoubleUnaryOperator mapper);
    IntFlow mapToInt(DoubleToIntFunction mapper);
    LongFlow mapToLong(DoubleToLongFunction mapper);
    <R> Flow<R> mapToObj(DoubleFunction<R> mapper);

    DoubleFlow filter(DoublePredicate predicate);

    OptionalDouble get();
    boolean doesAnswer(DoublePredicate predicate);
    Runnable futureConsume(DoubleConsumer consumer);
    void consume(DoubleConsumer consumer);

    static DoubleFlow from(DoubleSupplier supplier) {
        return new Pipeline(new ValueBase(supplier));
    }

    static DoubleFlow of(double value) {
        return from(Suppliers.of(value));
    }

    static DoubleFlow empty() {
        return new Empty();
    }

    class Pipeline implements DoubleFlow {

        private final Collector mCollector;
        private final ValueBase mValueBase;
        private final DoubleConsumer mDownflow;

        public Pipeline(Collector collector, ValueBase valueBase, DoubleConsumer downflow) {
            mCollector = collector;
            mValueBase = valueBase;
            mDownflow = downflow;
        }

        public Pipeline(ValueBase valueBase) {
            mValueBase = valueBase;

            mCollector = new Collector();
            mDownflow = mCollector;
        }

        @Override
        public DoubleFlow map(DoubleUnaryOperator mapper) {
            return new Pipeline(mCollector, mValueBase, (value)-> {
                mDownflow.accept(mapper.applyAsDouble(value));
            });
        }

        @Override
        public IntFlow mapToInt(DoubleToIntFunction mapper) {
            return null;
        }

        @Override
        public LongFlow mapToLong(DoubleToLongFunction mapper) {
            return null;
        }

        @Override
        public <R> Flow<R> mapToObj(DoubleFunction<R> mapper) {
            return null;
        }

        @Override
        public DoubleFlow filter(DoublePredicate predicate) {
            return new Pipeline(mCollector, mValueBase, (value) -> {
                if (predicate.test(value)) {
                    mDownflow.accept(value);
                }
            });
        }

        @Override
        public OptionalDouble get() {
            return evaluate();
        }

        @Override
        public boolean doesAnswer(DoublePredicate predicate) {
            OptionalDouble value = get();
            return value.isPresent() && predicate.test(value.getAsDouble());
        }

        @Override
        public Runnable futureConsume(DoubleConsumer consumer) {
            return ()->consume(consumer);
        }

        @Override
        public void consume(DoubleConsumer consumer) {
            OptionalDouble value = get();
            if (value.isPresent()) {
                consumer.accept(value.getAsDouble());
            }
        }

        private OptionalDouble evaluate() {
            mValueBase.forValue(mDownflow);
            return mCollector.get();
        }
    }

    class ValueBase {

        private final DoubleSupplier mSupplier;

        public ValueBase(DoubleSupplier supplier) {
            mSupplier = supplier;
        }

        public void forValue(DoubleConsumer consumer) {
            consumer.accept(mSupplier.getAsDouble());
        }
    }

    class Collector implements Supplier<OptionalDouble>, DoubleConsumer {

        private double mValue;
        private boolean mHasValue;

        public Collector() {
            mValue = 0.0;
            mHasValue = false;
        }

        @Override
        public void accept(double value) {
            mValue = value;
            mHasValue = true;
        }

        @Override
        public OptionalDouble get() {
            return mHasValue ? OptionalDouble.of(mValue) : OptionalDouble.empty();
        }
    }

    class Empty implements DoubleFlow {

        private Empty() {}

        @Override
        public DoubleFlow map(DoubleUnaryOperator operator) {
            return this;
        }

        @Override
        public IntFlow mapToInt(DoubleToIntFunction mapper) {
            return IntFlow.empty();
        }

        @Override
        public LongFlow mapToLong(DoubleToLongFunction mapper) {
            return LongFlow.empty();
        }

        @Override
        public <R> Flow<R> mapToObj(DoubleFunction<R> mapper) {
            return Flow.empty();
        }

        @Override
        public DoubleFlow filter(DoublePredicate predicate) {
            return this;
        }

        @Override
        public OptionalDouble get() {
            return OptionalDouble.empty();
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
