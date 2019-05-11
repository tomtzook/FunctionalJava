package com.functional.flow;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public interface DoubleFlow extends DoubleSupplier {

    DoubleFlow map(DoubleUnaryOperator operator);

    Runnable futureConsume(DoubleConsumer consumer);
    void consume(DoubleConsumer consumer);
}
