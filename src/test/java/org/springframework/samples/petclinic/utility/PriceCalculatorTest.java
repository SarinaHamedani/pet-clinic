package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PriceCalculatorTest {
	@Test
	public void testZeroPetsPriceCalculator() {
		assertEquals(0, PriceCalculator.calcPrice(new ArrayList<>(), 10, 10));
	}

	@Test
	public void testMaturePetWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2017,7,9));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(12, PriceCalculator.calcPrice(List.of(pet), 10, 10));
	}

	@Test
	public void testInfantPetWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(16.8, PriceCalculator.calcPrice(List.of(pet), 10, 10), 0.1);
	}

}
