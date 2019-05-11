package com.functional.flow;

import com.functional.Suppliers;
import org.junit.Test;

import java.util.OptionalDouble;
import java.util.function.DoubleSupplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DoubleFlowTest {

    @Test
    public void get_withValue_collectsAndReturns() throws Exception {
        final double VALUE = 5.2334;
        DoubleFlow flow = DoubleFlow.of(VALUE);

        assertOptionalWithValue(flow.get(), VALUE);
    }

    @Test
    public void get_withSupplier_collectsAndReturns() throws Exception {
        final double VALUE = 5.2334;
        DoubleFlow flow = DoubleFlow.from(()->VALUE);

        assertOptionalWithValue(flow.get(), VALUE);
    }

    @Test
    public void get_withSupplier_callsSupplierWhenEvaluated() throws Exception {
        final DoubleSupplier SUPPLIER = mock(DoubleSupplier.class);
        when(SUPPLIER.getAsDouble()).thenReturn(1.0);

        DoubleFlow flow = DoubleFlow.from(SUPPLIER);

        verify(SUPPLIER, times(0)).getAsDouble();

        flow.get();

        verify(SUPPLIER, times(1)).getAsDouble();
    }

    @Test
    public void map_withValue_collectsAndReturnsMapped() throws Exception {
        final double VALUE = 5.2334;
        final double MAPPED = 103.3123;
        DoubleFlow flow = DoubleFlow.of(VALUE)
                .map((value) -> MAPPED);

        assertOptionalWithValue(flow.get(), MAPPED);
    }

    @Test
    public void filter_valueMatches_hasValue() throws Exception {
        final double VALUE = 5.2334;
        DoubleFlow flow = DoubleFlow.of(VALUE)
                .filter((value) -> value > 0.0);

        assertOptionalWithValue(flow.get(), VALUE);
    }

    @Test
    public void filter_valueDoesNotMatche_hasValue() throws Exception {
        final double VALUE = 5.2334;
        DoubleFlow flow = DoubleFlow.of(VALUE)
                .filter((value) -> value < 0.0);

        assertOptionalWithoutValue(flow.get());
    }

    private void assertOptionalWithValue(OptionalDouble optionalDouble, double value) {
        assertTrue("optional has no value", optionalDouble.isPresent());
        assertEquals(value, optionalDouble.getAsDouble(), 0.001);
    }

    private void assertOptionalWithoutValue(OptionalDouble optionalDouble) {
        assertFalse("optional has value", optionalDouble.isPresent());
    }
}