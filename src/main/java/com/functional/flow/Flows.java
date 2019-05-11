package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public class Flows {

    private Flows() {}

    public static DoubleFlow from(DoubleSupplier supplier) {
        return new DoubleFlow() {
            @Override
            public double getAsDouble() {
                return supplier.getAsDouble();
            }

            @Override
            public DoubleFlow map(DoubleUnaryOperator operator) {
                return of(operator.applyAsDouble(supplier.getAsDouble()));
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

    public static DoubleFlow of(double value) {
        return from(Suppliers.of(value));
    }
}
