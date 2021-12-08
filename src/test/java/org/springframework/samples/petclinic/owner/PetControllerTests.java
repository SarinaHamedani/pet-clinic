package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
	value = { PetController.class },
	includeFilters = {
	@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE)
})
class PetControllerTests {
	// TODO
	@Autowired
	private MockMvc mvc;
	@MockBean
	private PetRepository petRepository;
	@MockBean
	private OwnerRepository ownerRepository;
	@MockBean
	private PetService petService;
	private Owner owner;
	private PetType cat;

	@BeforeEach
	public void setUp() {
		owner = new Owner();
		cat = new PetType();
		cat.setId(20);
		cat.setName("cat");
		Pet p = new Pet();
		p.setId(1);
		given(petRepository.findPetTypes()).willReturn(Lists.newArrayList(cat));
		given(petRepository.findById(1)).willReturn(p);
		given(petService.findOwner(1)).willReturn(owner);
		given(petService.newPet(owner)).willReturn(p);
		given(petService.findPet(1)).willReturn(p);
	}

	@Test
	public void initCreationFormIsReturnedCorrectly() throws Exception {
		mvc.perform(get("/owners/1/pets/new")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"));
		verify(petService).newPet(owner);
	}

	@Test
	void processCreationFormIsRedirectedWhenThereAreNoErrors() throws Exception {
		mvc.perform(post("/owners/1/pets/new")
				.param("name", "sparkles")
				.param("type", cat.toString())
				.param("birthDate", "2021-01-11"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void createOrUpdatePetFormIsReturnedWhenPetHasErrors() throws Exception {
		mvc.perform(post("/owners/1/pets/new")
				.param("name", "sparkles")
				.param("birthDate", "2021-01-11"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void createOrUpdatePetFormIsReturnedWhenPetNameIsDuplicate() throws Exception {
		Owner owner = mock(Owner.class);
		Pet pet = new Pet();
		pet.setName("whiskers");
		when(owner.getPet("whiskers", true)).thenReturn(pet);
		given(petService.findOwner(2)).willReturn(owner);

		mvc.perform(post("/owners/2/pets/new")
				.param("name", "whiskers")
				.param("type", cat.toString())
				.param("birthDate", "2021-01-11"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "name"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void updatePetFormIsReturnedCorrectly() throws Exception {
		mvc.perform(get("/owners/1/pets/1/edit")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"));
		verify(petService).findPet(1);
	}

	@Test
	void updatePetFormIsRedirectedCorrectlyGivenNoErrors() throws Exception {
		mvc.perform(post("/owners/1/pets/1/edit")
				.param("name", "sparkles")
				.param("type", cat.toString())
				.param("birthDate", "2021-01-11"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void updatePetFormIsReturnedCorrectlyGivenNoName() throws Exception {
		mvc.perform(post("/owners/1/pets/1/edit")
				.param("type", cat.toString())
				.param("birthDate", "2021-01-11"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
}
