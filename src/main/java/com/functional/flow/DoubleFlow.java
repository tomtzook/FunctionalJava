package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public interface DoubleFlow {

    OptionalDouble get();

    DoubleFlow map(DoubleUnaryOperator operator);
    DoubleFlow filter(DoublePredicate predicate);

    Runnable futureConsume(DoubleConsumer consumer);
    void consume(DoubleConsumer consumer);

    static DoubleFlow from(DoubleSupplier supplier) {
        return new DoubleFlow() {

            @Override
            public OptionalDouble get() {
                return OptionalDouble.of(supplier.getAsDouble());
            }

            @Override
            public DoubleFlow map(DoubleUnaryOperator operator) {
                return of(operator.applyAsDouble(supplier.getAsDouble()));
            }

            @Override
            public DoubleFlow filter(DoublePredicate predicate) {
                double value = supplier.getAsDouble();
                return predicate.test(value) ? from(supplier) : empty();
            }

            @Override
            public Runnable futureConsume(DoubleConsumer consumer) {
                return Runnables.fromConsumer(consumer, supplier.getAsDouble());
            }

            @Override
            public void consume(DoubleConsumer consumer) {
                consumer.accept(supplier.getAsDouble());
            }
        };
    }

    static DoubleFlow of(double value) {
        return from(Suppliers.of(value));
    }

    static DoubleFlow empty() {
        return new DoubleFlow() {
            @Override
            public OptionalDouble get() {
                return OptionalDouble.empty();
            }

            @Override
            public DoubleFlow map(DoubleUnaryOperator operator) {
                return empty();
            }

            @Override
            public DoubleFlow filter(DoublePredicate predicate) {
                return empty();
            }

            @Override
            public Runnable futureConsume(DoubleConsumer consumer) {
                return Runnables.empty();
            }

            @Override
            public void consume(DoubleConsumer consumer) {
            }
        };
    }
}
