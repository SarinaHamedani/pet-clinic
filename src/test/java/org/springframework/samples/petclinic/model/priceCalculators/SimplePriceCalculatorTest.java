package org.springframework.samples.petclinic.model.priceCalculators;


import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimplePriceCalculatorTest {
	@Test
	public void emptyPetListsPriceForOlderUsersAreBaseCharge() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		assertEquals(
			15,
			simplePriceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0.01);
	}

	@Test
	public void emptyPetListsPriceIsDiscountedBaseCharge() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		assertEquals(
			14.25,
			simplePriceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePets() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		Pet p1 = new Pet();
		Pet p2 = new Pet();
		p1.setType(new PetType());
		p2.setType(new PetType());
		pets.add(p1);
		pets.add(p2);
		assertEquals(
			22.2,
			simplePriceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndDiscountedForNewUsers() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		Pet p1 = new Pet();
		Pet p2 = new Pet();
		p1.setType(new PetType());
		p2.setType(new PetType());
		pets.add(p1);
		pets.add(p2);
		assertEquals(
			21.09,
			simplePriceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedIsUsedForNonRarePets() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		PetType petType = mock(PetType.class);
		Pet p1 = new Pet();
		Pet p2 = new Pet();
		p1.setType(petType);
		p2.setType(petType);
		pets.add(p1);
		pets.add(p2);
		when(petType.getRare()).thenReturn(false);
		assertEquals(
			21,
			simplePriceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0.01);
	}
}
