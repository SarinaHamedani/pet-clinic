package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {
	Owner owner;
	Pet dog, cat, duck, lama;
	@BeforeEach
	void setUp() {
		dog = new Pet();
		dog.setName("dog");
		dog.setId(101);
		cat = new Pet();
		cat.setName("cat");
		duck = new Pet();
		duck.setName("duck");
		lama = new Pet();
		lama.setName("lama");
		owner = new Owner();
		Set<Pet> pets = new HashSet<>();
		pets.add(dog);
		pets.add(cat);
		pets.add(duck);
		owner.setPetsInternal(pets);
	}
	@Test
	public void Empty_pet_internals_are_returned_correctly() {
		owner = new Owner();
		assertThat(owner.getPetsInternal()).hasSize(0);
	}

	@Test
	public void Pet_internals_are_returned_correctly() {
		assertThat(owner.getPetsInternal()).hasSize(3);
	}
}
