package org.springframework.samples.petclinic.owner;

import org.aspectj.lang.annotation.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PetManagerTest {
	@MockBean
	private PetTimedCache petTimedCache;
	@MockBean
	private OwnerRepository ownerRepository;
	@MockBean
	private Logger logger;
	private PetManager petManager;
	private Pet poodle, kitten, monkey, hamster;
	private PetType dogs, cats, monkeys, mice;

	@BeforeEach
	public void initialize() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);
		poodle = new Pet();
		dogs = new PetType();
		dogs.setName("dogs");
		poodle.setType(dogs);

		kitten = new Pet();
		cats = new PetType();
		cats.setName("cats");
		kitten.setType(cats);

		monkey = new Pet();
		monkeys = new PetType();
		monkeys.setName("monkeys");
		monkey.setType(monkeys);

		hamster = new Pet();
		mice = new PetType();
		mice.setName("rats and mouse");
		hamster.setType(mice);
	}

	// Stub, State verification, Mockisty
	@Test
	public void Owner_is_found_in_repository_and_returned_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(1700)).thenReturn(owner);
		assertEquals(petManager.findOwner(1700), owner);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Owner_is_found_in_repository_and_logged_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(1700)).thenReturn(owner);
		petManager.findOwner(1700);
		verify(ownerRepository).findById(1700);
		verify(logger).info("find owner {}", 1700);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Null_is_returned_if_owner_does_not_exist_in_repository() {
		assertNull(petManager.findOwner(801));
		verify(ownerRepository).findById(801);
		verify(logger).info("find owner {}", 801);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void New_pet_is_added_to_owner_correctly() {
		Owner owner = mock(Owner.class);
		Pet pet = petManager.newPet(owner);
		verify(owner).addPet(pet);
	}

	// Stub, State verification, Mockisty
	@Test
	public void Pet_is_found_in_cache_and_returned_correctly() {
		when(petTimedCache.get(123)).thenReturn(poodle);
		assertEquals(petManager.findPet(123), poodle);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Pet_is_found_in_cache_and_logged_correctly() {
		when(petTimedCache.get(123)).thenReturn(poodle);
		petManager.findPet(123);
		verify(logger).info("find pet by id {}", 123);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Null_is_returned_if_pet_does_not_exist_in_cache() {
		assertNull(petManager.findPet(124));
		verify(petTimedCache).get(124);
		verify(logger).info("find pet by id {}", 124);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Pet_is_added_to_owner_and_saved_in_cache_correctly() {
		Owner owner = mock(Owner.class);
		Pet pet = mock(Pet.class);
		when(pet.getId()).thenReturn(125);
		petManager.savePet(pet, owner);
		verify(owner).addPet(pet);
		verify(petTimedCache).save(pet);
		verify(logger).info("save pet {}", 125);
	}

	// Stub, State verification, Mockisty
	@Test
	public void Owners_pets_are_returned_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(912)).thenReturn(owner);
		when(owner.getPets()).thenReturn(List.of(poodle, kitten, hamster));
		assertEquals(petManager.getOwnerPets(912), List.of(poodle, kitten, hamster));
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Owners_pets_are_logged_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(912)).thenReturn(owner);
		when(owner.getPets()).thenReturn(List.of(poodle, kitten, hamster));
		petManager.getOwnerPets(912);
		verify(ownerRepository).findById(912);
		verify(owner).getPets();
		verify(logger).info("finding the owner's pets by id {}", 912);
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Exception_is_thrown_if_owner_does_not_exist() {
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPets(952));
		verify(ownerRepository).findById(952);
		verify(logger).info("finding the owner's pets by id {}", 952);
	}

	// Stub, State verification, Mockisty
	@Test
	public void Owners_pet_types_are_returned_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(939)).thenReturn(owner);
		when(owner.getPets()).thenReturn(List.of(poodle, kitten, monkey, hamster));
		assertEquals(petManager.getOwnerPetTypes(939), Set.of(dogs, cats, monkeys, mice));
	}

	// Stub, State verification, Mockisty
	@Test
	public void Pets_visits_in_a_date_range_are_returned_correctly() {
		Pet pet = mock(Pet.class);
		Visit visit1 = mock(Visit.class);
		Visit visit2 = mock(Visit.class);
		Visit visit3 = mock(Visit.class);
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2021, 3, 20);
		when(petTimedCache.get(53)).thenReturn(pet);
		when(pet.getVisitsBetween(start, end)).thenReturn(List.of(visit1, visit2, visit3));
		assertEquals(petManager.getVisitsBetween(53, start, end), List.of(visit1, visit2, visit3));
	}

	// Spy, Behaviour verification, Mockisty
	@Test
	public void Exception_is_thrown_if_pet_is_not_found_to_get_visits_for() {
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2021, 3, 20);
		assertThrows(NullPointerException.class, () -> petManager.getVisitsBetween(100, start, end));
		verify(petTimedCache).get(100);
		verify(logger).info("get visits for pet {} from {} since {}", 100, start, end);
	}
}
