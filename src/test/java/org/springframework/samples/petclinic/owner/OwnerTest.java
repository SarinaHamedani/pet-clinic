package org.springframework.samples.petclinic.owner;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(Theories.class)
public class OwnerTest {
	private static Owner owner;
	private static Pet dog, cat, duck, lama;
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

	@Test
	public void Pets_are_sorted_and_returned_correctly() {
		assertThat(owner.getPets()).hasSize(3).containsExactly(cat, dog, duck);
	}

	@Test
	public void New_pets_are_added_correctly() {
		owner.addPet(lama);
		assertThat(lama.getOwner()).isEqualTo(owner);
		assertThat(owner.getPetsInternal()).hasSize(4);
	}

	@Test
	public void Pets_with_id_are_added_correctly() {
		lama.setId(133);
		owner.addPet(lama);
		assertThat(lama.getOwner()).isEqualTo(owner);
		assertThat(owner.getPetsInternal()).hasSize(3);
	}

	@Test
	public void Pet_with_id_and_specific_case_insensitive_name_is_returned_correctly() {
		assertThat(owner.getPet("DOG", true)).isEqualTo(dog);
	}

	@Test
	public void Pet_without_id_and_specific_case_insensitive_name_is_not_returned() {
		assertThat(owner.getPet("CaT", true)).isNull();
	}

	@Test
	public void Pet_with_specific_case_insensitive_name_is_returned_correctly() {
		assertThat(owner.getPet("DOG")).isEqualTo(dog);
	}

	@Test
	public void Null_is_returned_if_name_does_not_exist_in_owners_pets_internal() {
		assertThat(owner.getPet("Seal")).isNull();
	}

	@DataPoint
	public static Set<Pet> petsOutOfOrder() {
		Set<Pet> pets = new HashSet<>();
		pets.add(dog);
		pets.add(cat);
		pets.add(lama);
		return pets;
	}

	@DataPoint
	public static Set<Pet> petsInOrder() {
		Set<Pet> pets = new HashSet<>();
		pets.add(cat);
		pets.add(dog);
		pets.add(duck);
		pets.add(lama);
		return pets;
	}

	@DataPoint
	public static Set<Pet> petsInReverseOrder() {
		Set<Pet> pets = new HashSet<>();
		pets.add(duck);
		pets.add(dog);
		pets.add(cat);
		return pets;
	}
}
