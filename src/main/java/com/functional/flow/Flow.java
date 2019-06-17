package com.functional.flow;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

public interface Flow<T> {

    Flow<T> filter(Predicate<? super T> predicate);

    <R> Flow<R> map(Function<? super T, ? extends R> mapper);
    LongFlow mapToLong(ToLongFunction<? super T> mapper);
    IntFlow mapToInt(ToIntFunction<? super T> mapper);
    DoubleFlow mapToDouble(ToDoubleFunction<? super T> mapper);

    Flow<T> peek(Consumer<? super T> action);
    void doIfPresent(Consumer<? super T> action);

    boolean doesMatch(Predicate<? super T> predicate);
    boolean notMatch(Predicate<? super T> predicate);

    Optional<T> get();

    static <U> Flow<U> of(U value) {
        return new StreamBackedFlow<>(Stream.of(value));
    }

    class StreamBackedFlow<U> implements Flow<U> {

        private final Stream<U> mStream;

        public StreamBackedFlow(Stream<U> stream) {
            mStream = stream;
        }

        @Override
        public Flow<U> filter(Predicate<? super U> predicate) {
            return new StreamBackedFlow<>(mStream.filter(predicate));
        }

        @Override
        public <R> Flow<R> map(Function<? super U, ? extends R> mapper) {
            return new StreamBackedFlow<>(mStream.map(mapper));
        }

        @Override
        public LongFlow mapToLong(ToLongFunction<? super U> mapper) {
            return new LongFlow.StreamBackedLongFlow(mStream.mapToLong(mapper));
        }

        @Override
        public IntFlow mapToInt(ToIntFunction<? super U> mapper) {
            return new IntFlow.StreamBackedIntFlow(mStream.mapToInt(mapper));
        }

        @Override
        public DoubleFlow mapToDouble(ToDoubleFunction<? super U> mapper) {
            return new DoubleFlow.StreamBackedDoubleFlow(mStream.mapToDouble(mapper));
        }

        @Override
        public Flow<U> peek(Consumer<? super U> action) {
            return new StreamBackedFlow<>(mStream.peek(action));
        }

        @Override
        public void doIfPresent(Consumer<? super U> action) {
            mStream.forEach(action);
        }

        @Override
        public boolean doesMatch(Predicate<? super U> predicate) {
            return mStream.anyMatch(predicate);
        }

        @Override
        public boolean notMatch(Predicate<? super U> predicate) {
            return mStream.noneMatch(predicate);
        }

        @Override
        public Optional<U> get() {
            return mStream.findAny();
        }
    }
}
