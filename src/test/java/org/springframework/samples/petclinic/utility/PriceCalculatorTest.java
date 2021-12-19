package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

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

	@Test
	public void testInfantPetWithNoVisitsAndRecentDiscount() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet.getVisitsUntilAge(0)).thenReturn(new ArrayList<>());
		assertEquals(161.2, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testInfantPetWithNoVisitsAndOldVisitedDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2020,12,25));
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet.getVisitsUntilAge(0)).thenReturn(List.of(visit));
		assertEquals(325.6, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet), 10, 10), 0.1);
	}

}
