package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {
	@Test
	public void testZeroPetsPriceCalculator() {
		assertEquals(0, PriceCalculator.calcPrice(new ArrayList<>(), 10, 10));
	}

}
