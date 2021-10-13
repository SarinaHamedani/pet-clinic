package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PetServiceTest {
	private final PetTimedCache cache;
	@Mock
	private OwnerRepository ownerRepository;
	private final PetService petService;
	private static Pet dog, cat, hamster, bunny, parrot;
	private final int inputId;
	private final Pet expectedPet;

	public PetServiceTest(int id, Pet pet) {
		super();
		this.inputId = id;
		this.expectedPet = pet;
		cache = mock(PetTimedCache.class);
		Logger logger = LoggerFactory.getLogger("TEST");
		petService = new PetService(cache, ownerRepository, logger);
	}


	@BeforeAll
	public void initialize() {
		dog = new Pet();
		cat = new Pet();
		hamster = new Pet();
		bunny = new Pet();
		parrot = new Pet();
		when(cache.get(2)).thenReturn(dog);
		when(cache.get(17)).thenReturn(cat);
		when(cache.get(29)).thenReturn(parrot);
		when(cache.get(6)).thenReturn(bunny);
		when(cache.get(10)).thenReturn(hamster);
	}


	@Parameterized.Parameters
	public static Collection pets() {
		return Arrays.asList(new Object[][] {
			{ 2, dog },
			{ 6, bunny },
			{ 29, parrot },
			{ 17, cat },
			{ 10, hamster }
		});
	}

	@Test
	public void Pets_are_found_correctly() {
		System.out.println("Parameterized Number is : " + inputId);
		assertEquals(expectedPet, petService.findPet(inputId));
	}
}
