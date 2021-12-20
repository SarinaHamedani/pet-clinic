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
	LocalDate today, fiveYearsAgo;

	@BeforeEach
	public void setUp() {
		today = LocalDate.now();
		fiveYearsAgo = today.minusYears(5);
		fiveYearsAgo = fiveYearsAgo.minusDays(20);
	}
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
	public void testInfantPetWithOldVisitedDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2020,12,25));
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet.getVisitsUntilAge(0)).thenReturn(List.of(visit));
		assertEquals(325.6, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleMaturePetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2017,7,9));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(36, PriceCalculator.calcPrice(List.of(pet, pet, pet), 10, 10));
	}

	@Test
	public void testMultipleInfantPetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(50.4, PriceCalculator.calcPrice(List.of(pet, pet, pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleTwoYearOldPetsWithNoVisits() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2019,7,9));
		when(pet.getVisitsUntilAge(2)).thenReturn(new ArrayList<>());
		assertEquals(16.8, PriceCalculator.calcPrice(List.of(pet), 10, 10), 0.1);
	}

	@Test
	public void testMultipleDifferentAgesPetsWithNoVisits() {
		Pet infant = mock(Pet.class);
		Pet mature = mock(Pet.class);
		when(infant.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(infant.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		when(mature.getBirthDate()).thenReturn(LocalDate.of(2017,7,9));
		when(mature.getVisitsUntilAge(4)).thenReturn(new ArrayList<>());
		assertEquals(45.6, PriceCalculator.calcPrice(List.of(infant, mature, infant), 10, 10), 0.1);
	}

	@Test
	public void testMaturePetWithNoVisitsAndRecentDiscount() {
		Pet pet = mock(Pet.class);
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2016,10,9));
		when(pet.getVisitsUntilAge(5)).thenReturn(new ArrayList<>());
		assertEquals(238, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet, pet, pet, pet, pet, pet), 10, 10));
	}

	@Test
	public void testMultipleMaturePetWithOldVisitedDiscount() {
		Pet pet = mock(Pet.class);
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		LocalDate today = LocalDate.now();
		LocalDate past = today.minusYears(5);
		visit1.setDate(LocalDate.of(2020,12,25));
		visit2.setDate(LocalDate.of(2020,12,25));
		when(pet.getBirthDate()).thenReturn(past);
		when(pet.getVisitsUntilAge(5)).thenReturn(List.of(visit1, visit2));
		assertEquals(602, PriceCalculator.calcPrice(List.of(pet, pet, pet, pet, pet, pet, pet, pet, pet, pet), 10, 10));
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
		visit1.setDate(LocalDate.of(2020,12,25));
		visit2.setDate(LocalDate.of(2020,12,25));
		when(pet1.getBirthDate()).thenReturn(LocalDate.of(2016,7,9));
		when(pet1.getVisitsUntilAge(5)).thenReturn(List.of(visit1, visit2));
		when(pet2.getBirthDate()).thenReturn(LocalDate.of(2021,7,9));
		when(pet2.getVisitsUntilAge(0)).thenReturn(List.of(visit1));
		assertEquals(185192, PriceCalculator.calcPrice(List.of(pet1, pet2, pet2, pet1, pet2, pet2, pet1, pet2, pet1, pet1), 10, 10));
	}

}
