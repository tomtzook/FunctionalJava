package com.functional.flow;

import com.functional.Runnables;
import com.functional.Suppliers;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

public interface Flow<T> {

    Optional<T> get();
    boolean hasValue();

    Flow<T> map(UnaryOperator<T> operator);
    IntFlow map(ToIntFunction<T> mapper);
    DoubleFlow map(ToDoubleFunction<T> mapper);
    LongFlow map(ToLongFunction<T> mapper);

    Flow<T> filter(Predicate<T> predicate);
    boolean doesAnswer(Predicate<T> predicate);

    Runnable futureConsume(Consumer<T> consumer);
    void consume(Consumer<T> consumer);

    static <T> Flow<T> from(Supplier<T> supplier) {
        return new Supplied<>(supplier);
    }

    static <T> Flow<T> of(T value) {
        return from(Suppliers.of(value));
    }

    static <T> Flow<T> empty() {
        return new Empty<>();
    }

    class Supplied<T> implements Flow<T> {

        private final Supplier<T> mSupplier;

        private Supplied(Supplier<T> supplier) {
            mSupplier = supplier;
        }

        @Override
        public Optional<T> get() {
            return Optional.ofNullable(mSupplier.get());
        }

        @Override
        public boolean hasValue() {
            return get().isPresent();
        }

        @Override
        public Flow<T> map(UnaryOperator<T> operator) {
            return Flow.from(()->operator.apply(mSupplier.get()));
        }

        @Override
        public IntFlow map(ToIntFunction<T> mapper) {
            return IntFlow.from(()->mapper.applyAsInt(mSupplier.get()));
        }

        @Override
        public DoubleFlow map(ToDoubleFunction<T> mapper) {
            return DoubleFlow.from(()->mapper.applyAsDouble(mSupplier.get()));
        }

        @Override
        public LongFlow map(ToLongFunction<T> mapper) {
            return LongFlow.from(()->mapper.applyAsLong(mSupplier.get()));
        }

        @Override
        public Flow<T> filter(Predicate<T> predicate) {
            return new Delegated<>(()-> predicate.test(mSupplier.get()) ?
                    Flow.from(mSupplier) : Flow.empty());
        }

        @Override
        public boolean doesAnswer(Predicate<T> predicate) {
            return predicate.test(mSupplier.get());
        }

        @Override
        public Runnable futureConsume(Consumer<T> consumer) {
            return Runnables.fromConsumer(consumer, mSupplier.get());
        }

        @Override
        public void consume(Consumer<T> consumer) {
            consumer.accept(mSupplier.get());
        }
    }

    class Delegated<T> implements Flow<T> {

        private final Supplier<Flow<T>> mSupplier;

        private Delegated(Supplier<Flow<T>> supplier) {
            mSupplier = supplier;
        }

        @Override
        public Optional<T> get() {
            return mSupplier.get().get();
        }

        @Override
        public boolean hasValue() {
            return get().isPresent();
        }

        @Override
        public Flow<T> map(UnaryOperator<T> mapper) {
            return mSupplier.get().map(mapper);
        }

        @Override
        public IntFlow map(ToIntFunction<T> mapper) {
            return mSupplier.get().map(mapper);
        }

        @Override
        public DoubleFlow map(ToDoubleFunction<T> mapper) {
            return mSupplier.get().map(mapper);
        }

        @Override
        public LongFlow map(ToLongFunction<T> mapper) {
            return mSupplier.get().map(mapper);
        }

        @Override
        public Flow<T> filter(Predicate<T> predicate) {
            return mSupplier.get().filter(predicate);
        }

        @Override
        public boolean doesAnswer(Predicate<T> predicate) {
            return mSupplier.get().doesAnswer(predicate);
        }

        @Override
        public Runnable futureConsume(Consumer<T> consumer) {
            return mSupplier.get().futureConsume(consumer);
        }

        @Override
        public void consume(Consumer<T> consumer) {
            mSupplier.get().consume(consumer);
        }
    }

    class Empty<T> implements Flow<T> {

        private Empty() {}

        @Override
        public Optional<T> get() {
            return Optional.empty();
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public Flow<T> map(UnaryOperator<T> operator) {
            return this;
        }

        @Override
        public IntFlow map(ToIntFunction<T> mapper) {
            return IntFlow.empty();
        }

        @Override
        public DoubleFlow map(ToDoubleFunction<T> mapper) {
            return DoubleFlow.empty();
        }

        @Override
        public LongFlow map(ToLongFunction<T> mapper) {
            return LongFlow.empty();
        }

        @Override
        public Flow<T> filter(Predicate<T> predicate) {
            return this;
        }

        @Override
        public boolean doesAnswer(Predicate<T> predicate) {
            return false;
        }

        @Override
        public Runnable futureConsume(Consumer<T> consumer) {
            return Runnables.empty();
        }

        @Override
        public void consume(Consumer<T> consumer) {
        }
    }
}
