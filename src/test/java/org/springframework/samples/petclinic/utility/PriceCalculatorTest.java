package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceCalculatorTest {
	LocalDate today, fiveYearsAgo, thisYear, lastYear;

	@BeforeEach
	public void setUp() {
		today = LocalDate.now();
		thisYear = LocalDate.now().minusDays(90);
		lastYear = LocalDate.now().minusYears(1).minusDays(50);
		fiveYearsAgo = today.minusYears(5).minusDays(20);
	}

	@Test
	public void testInitialized() {
		PriceCalculator p = new PriceCalculator();
		assertNotNull(p);
	}

	@Test
	public void testZeroPetsPriceCalculator() {
		assertEquals(0, PriceCalculator.calcPrice(new ArrayList<>(), 10, 10));
	}

	@Test
	public void testMaturePetWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(fiveYearsAgo);
		assertEquals(12, PriceCalculator.calcPrice(List.of(pet), 10, 10));
	}

	@Test
	public void testInfantPetWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(thisYear);
		assertEquals(16.8, PriceCalculator.calcPrice(List.of(pet), 10, 10), 0.1);
	}

	@Test
	public void testInfantPetWithNoVisitsAndRecentDiscount() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(thisYear);
		assertEquals(161.2, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testInfantPetWithOldVisitedDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2020,12,25));
		when(pet.getBirthDate()).thenReturn(thisYear);
		when(pet.getVisitsUntilAge(0)).thenReturn(List.of(visit, visit));
		assertEquals(402.8, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleMaturePetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(fiveYearsAgo);
		assertEquals(36, PriceCalculator.calcPrice(List.of(pet, pet, pet), 10, 10));
	}

	@Test
	public void testMultipleInfantPetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		assertEquals(50.4, PriceCalculator.calcPrice(List.of(pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleTwoYearOldPetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2019,7,9));
		assertEquals(16.8, PriceCalculator.calcPrice(List.of(pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleDifferentAgesPetsWithNoVisits() {
		Pet infant = mock(Pet.class);
		Pet mature = mock(Pet.class);
		when(infant.getBirthDate()).thenReturn(thisYear);
		when(mature.getBirthDate()).thenReturn(fiveYearsAgo);
		assertEquals(45.6, PriceCalculator.calcPrice(List.of(infant, mature, infant), 10, 10), 0.1);
	}

	@Test
	public void testMaturePetWithNoVisitsAndRecentDiscount() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(fiveYearsAgo);
		assertEquals(238, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet, pet, pet, pet, pet, pet), 10, 10));
	}

	@Test
	public void testMultipleMaturePetWithOldVisitedDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit = new Visit();
		visit.setDate(lastYear);
		when(pet.getBirthDate()).thenReturn(fiveYearsAgo);
		when(pet.getVisitsUntilAge(5)).thenReturn(List.of(visit, visit));
		assertEquals(720, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet, pet, pet, pet, pet, pet), 10, 10));
	}

	@Test
	public void test100DayOldLastVisitForDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit = new Visit();
		LocalDate today = LocalDate.now();
		LocalDate past = today.minusDays(100);
		visit.setDate(past);
		when(pet.getBirthDate()).thenReturn(fiveYearsAgo);
		when(pet.getVisitsUntilAge(5)).thenReturn(List.of(visit, visit));
		assertEquals(1140, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet, pet, pet, pet, pet, pet, pet), 10, 10));
	}

	@Test
	public void testMultipleDifferentAgesPetWithOldVisitedDiscount() {
		Pet pet1 = mock(Pet.class);
		Pet pet2 = mock(Pet.class);
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		visit1.setDate(lastYear);
		visit2.setDate(thisYear);
		when(pet1.getBirthDate()).thenReturn(fiveYearsAgo);
		when(pet1.getVisitsUntilAge(5)).thenReturn(List.of(visit1, visit2));
		when(pet2.getBirthDate()).thenReturn(thisYear);
		when(pet2.getVisitsUntilAge(0)).thenReturn(List.of(visit1));
		assertEquals(3158.8, PriceCalculator.calcPrice(List.of(pet1, pet2, pet2, pet1, pet2, pet2, pet1, pet2, pet1, pet1), 10, 10), 0.1);
	}

}
