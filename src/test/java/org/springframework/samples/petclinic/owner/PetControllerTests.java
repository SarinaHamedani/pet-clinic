package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ PetController.class })
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

	@Test
	public void testInitCreationForm() throws Exception {
		Owner owner = new Owner();
		Pet p = new Pet();
		given(petService.findOwner(1)).willReturn(owner);
		given(petService.newPet(owner)).willReturn(p);

		mvc.perform(get("/owners/1/pets/new")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
}
