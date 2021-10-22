package org.springframework.samples.petclinic.owner;

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

	@BeforeEach
	public void initialize() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);
	}

	@Test
	public void Owner_is_found_in_repository_and_returned_correctly() {
		Owner owner = mock(Owner.class);
		when(ownerRepository.findById(1700)).thenReturn(owner);
		assertEquals(petManager.findOwner(1700), owner);
		verify(ownerRepository).findById(1700);
		verify(logger).info("find owner {}", 1700);
	}

	@Test
	public void Null_is_returned_if_owner_does_not_exist_in_repository() {
		assertNull(petManager.findOwner(801));
		verify(ownerRepository).findById(801);
		verify(logger).info("find owner {}", 801);
	}


	// Owner is used as spy
	@Test
	public void New_pet_is_added_to_owner_correctly() {
		Owner owner = mock(Owner.class);
		Pet pet = petManager.newPet(owner);
		verify(owner).addPet(pet);
	}

	@Test
	public void Pet_is_found_in_cache_and_returned_correctly() {
		Pet pet = mock(Pet.class);
		when(petTimedCache.get(123)).thenReturn(pet);
		assertEquals(petManager.findPet(123), pet);
		verify(logger).info("find pet by id {}", 123);
	}

	@Test
	public void Null_is_returned_if_pet_does_not_exist_in_cache() {
		assertNull(petManager.findPet(124));
		verify(petTimedCache).get(124);
		verify(logger).info("find pet by id {}", 124);
	}

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
}
