package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerDependentPriceCalculatorTest {
	Pet p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;

	public void setUp() {
		Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
		p1 = new Pet();
		p2 = new Pet();
		p3 = new Pet();
		p4 = new Pet();
		p5 = new Pet();
		p6 = new Pet();
		p7 = new Pet();
		p8 = new Pet();
		p9 = new Pet();
		p10 = new Pet();
		p1.setType(new PetType());
		p1.setBirthDate(date);
		p2.setType(new PetType());
		p2.setBirthDate(date);
		p3.setType(new PetType());
		p3.setBirthDate(date);
		p4.setType(new PetType());
		p4.setBirthDate(date);
		p5.setType(new PetType());
		p5.setBirthDate(date);
		p6.setType(new PetType());
		p6.setBirthDate(date);
		p7.setType(new PetType());
		p7.setBirthDate(date);
		p8.setType(new PetType());
		p8.setBirthDate(date);
		p9.setType(new PetType());
		p9.setBirthDate(date);
		p10.setType(new PetType());
		p10.setBirthDate(date);
	}

	@Test
	public void newUsersWithEmptyPetListsAreNotCharged() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0,
			0.01);
	}

	@Test
	public void silverUsersWithEmptyPetListsAreNotCharged() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		UserType userType = UserType.SILVER;
		List<Pet> pets = new ArrayList<>();
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			0,
			0.01);
	}

	@Test
	public void emptyPetListsPriceForGoldUsersIsBaseCharge() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			15,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePetsAndDiscountedForNewUsersWithSufficientDiscountScore() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		pets.add(p6);
		pets.add(p7);
		pets.add(p8);
		pets.add(p9);
		pets.add(p10);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			49.2,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientIsUsedForRarePets() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			18,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientPlusBaseChargeIsUsedForRarePetsAndDiscountedForGoldUsers() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			29.4,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByRareCoefficientPlusBaseChargeIsUsedForRarePetsAndTotalPriceIsDiscountedForOldUsersWithSufficientDiscountScore() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		pets.add(p6);
		pets.add(p7);
		pets.add(p8);
		pets.add(p9);
		pets.add(p10);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			40.8,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByInfantRareCoefficientPlusBaseChargeIsUsedForInfantAndRarePetsAndDiscountedForNewUsersWithSufficientDiscountScore() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		p1.setBirthDate(new Date());
		p2.setBirthDate(new Date());
		p3.setBirthDate(new Date());
		p4.setBirthDate(new Date());
		p5.setBirthDate(new Date());
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			38.94,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByInfantRareCoefficientPlusBaseChargeIsUsedForInfantAndRarePetsAndTotalPriceIsDiscountedForOldUsersWithSufficientDiscountScore() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		p1.setBirthDate(new Date());
		p2.setBirthDate(new Date());
		p3.setBirthDate(new Date());
		p4.setBirthDate(new Date());
		p5.setBirthDate(new Date());
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			32.16,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByInfantCoefficientPlusBaseChargeIsUsedForInfantPetsAndTotalPriceIsDiscountedForOldUsersWithSufficientDiscountScore() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		Pet pet = new Pet();
		pet.setBirthDate(new Date());
		PetType petType = mock(PetType.class);
		pet.setType(petType);
		when(petType.getRare()).thenReturn(false);
		p1.setBirthDate(new Date());
		p2.setBirthDate(new Date());
		pets.add(pet);
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		pets.add(p5);
		pets.add(p6);
		pets.add(p7);
		pets.add(p8);
		pets.add(p9);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			43.10,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByInfantCoefficientPlusBaseChargeIsUsedForInfantPetsAndDiscountedForGoldUsers() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.GOLD;
		List<Pet> pets = new ArrayList<>();
		Pet pet = new Pet();
		pet.setBirthDate(new Date());
		PetType petType = mock(PetType.class);
		pet.setType(petType);
		when(petType.getRare()).thenReturn(false);
		p1.setBirthDate(new Date());
		p2.setBirthDate(new Date());
		pets.add(pet);
		pets.add(p1);
		pets.add(p2);
		pets.add(p3);
		pets.add(p4);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			31.70,
			0.01);
	}

	@Test
	public void basePricePerPetMultipliedByInfantCoefficientPlusBaseChargeIsUsedForInfantPets() {
		CustomerDependentPriceCalculator priceCalculator = new CustomerDependentPriceCalculator();
		this.setUp();
		UserType userType = UserType.NEW;
		List<Pet> pets = new ArrayList<>();
		Pet pet = new Pet();
		pet.setBirthDate(new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime());
		PetType petType = mock(PetType.class);
		pet.setType(petType);
		when(petType.getRare()).thenReturn(false);
		p1.setBirthDate(new Date());
		p2.setBirthDate(new Date());
		pets.add(pet);
		pets.add(p1);
		pets.add(p2);
		assertEquals(
			priceCalculator.calcPrice(pets, 15.0, 3.0, userType),
			13.07,
			0.01);
	}

}
