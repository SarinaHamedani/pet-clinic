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
}
