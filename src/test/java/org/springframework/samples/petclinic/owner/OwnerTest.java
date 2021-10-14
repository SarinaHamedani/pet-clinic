package org.springframework.samples.petclinic.owner;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
		owner.setAddress("123 Main Street");
		owner.setCity("New York");
		owner.setTelephone("+123456789");
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setId(101);

		Set<Pet> pets = new HashSet<>();
		pets.add(dog);
		pets.add(cat);
		pets.add(duck);
		owner.setPetsInternal(pets);
	}

	@Test
	public void Owners_first_name_is_returned_correctly() {
		assertEquals("John", owner.getFirstName());
	}

	@Test
	public void Owners_last_name_is_returned_correctly() {
		assertEquals("Doe", owner.getLastName());
	}

	@Test
	public void Owners_telephone_number_is_returned_correctly() {
		assertEquals("+123456789", owner.getTelephone());
	}

	@Test
	public void Owners_city_is_returned_correctly() {
		assertEquals("New York", owner.getCity());
	}

	@Test
	public void Owners_address_is_returned_correctly() {
		assertEquals("123 Main Street", owner.getAddress());
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
	public void New_pets_with_specific_case_insensitive_names_are_excluded_when_ignore_new_is_enabled() {
		assertThat(owner.getPet("CaT", true)).isNull();
	}

	@Test
	public void New_pets_with_specific_case_insensitive_names_are_included_when_ignore_new_is_disabled() {
		assertThat(owner.getPet("CaT", false)).isEqualTo(cat);
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

	@Theory
	public void Pets_are_sorted_by_name_correctly(Set<Pet> pets) {
		owner.setPetsInternal(pets);
		List<String> actual = owner.getPets().stream().map(Pet::getName).collect(Collectors.toList());
		assertThat(actual).isSortedAccordingTo(Comparator.naturalOrder());
	}
}
