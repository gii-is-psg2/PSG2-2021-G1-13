package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

@WebMvcTest(controllers=AdoptionController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class AdoptionControllerTest {

	private static final int TEST_ADOPTION_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;

	@Autowired
	private AdoptionController adoptionController;

	@MockBean
	private AdoptionService adoptionService;

    @MockBean
	private OwnerService ownerService;

    @MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Adoption adoption;
	private Owner owner;
	private Pet pet;

	@BeforeEach
	void setup() {
		//Hay que preparar un user
		final User user = new User();
		user.setUsername("spring");
		user.setPassword("spring");
		final Authorities auth = new Authorities();
		auth.setUser(user);
		auth.setAuthority("owner");
		auth.setId(1);
		final Set<Authorities> auths = new HashSet<Authorities>();
		auths.add(auth);
		user.setAuthorities(auths);
		BDDMockito.given(this.ownerService.getUser(user.getUsername())).willReturn(user);

		this.adoption = this.adoptionService.findById(AdoptionControllerTest.TEST_ADOPTION_ID);
		this.owner = this.ownerService.findOwnerById(AdoptionControllerTest.TEST_OWNER_ID);
		this.pet = this.petService.findPetById(AdoptionControllerTest.TEST_PET_ID);
		BDDMockito.given(this.adoptionService.findById(AdoptionControllerTest.TEST_ADOPTION_ID)).willReturn(this.adoption);
		BDDMockito.given(this.ownerService.findOwnerById(AdoptionControllerTest.TEST_OWNER_ID)).willReturn(this.owner);
		BDDMockito.given(this.petService.findPetById(AdoptionControllerTest.TEST_PET_ID)).willReturn(this.pet);
	}

	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/{ownerId}/{petId}/new", AdoptionControllerTest.TEST_OWNER_ID, AdoptionControllerTest.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("adoption"))
			.andExpect(MockMvcResultMatchers.view().name("adoptions/formAdoption"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testSelectOwner() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("ownerList"))
			.andExpect(MockMvcResultMatchers.view().name("adoptions/selectOwner"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testListOwnerAdoptions() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/{ownerId}", AdoptionControllerTest.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("adoptions"))
			.andExpect(MockMvcResultMatchers.view().name("adoptions/adoptionsOwner"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testListAdoptions() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/{ownerId}/list", AdoptionControllerTest.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("adoptions"))
			.andExpect(MockMvcResultMatchers.view().name("adoptions/adoptionList"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testAdoptionMenu() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/{ownerId}/list", AdoptionControllerTest.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("adoptions/adoptionList"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testInitSendForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/adoptions/"+AdoptionControllerTest.TEST_OWNER_ID+"/"+AdoptionControllerTest.TEST_ADOPTION_ID+"/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("pet", "1"))
	.andExpect(MockMvcResultMatchers.status().isOk())
	.andExpect(MockMvcResultMatchers.view().name("adoptions/adoptionList"));
	}


	@WithMockUser(value = "spring")
    @Test
    void testInitSendFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/adoptions/"+AdoptionControllerTest.TEST_OWNER_ID+"/"+AdoptionControllerTest.TEST_ADOPTION_ID+"/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("pet", "1"))
	.andExpect(MockMvcResultMatchers.status().isOk())
	.andExpect(MockMvcResultMatchers.model().attributeHasErrors("adoption"))
	.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("adoption", "description"))
	.andExpect(MockMvcResultMatchers.view().name("adoptions/formAdoption"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testDeleteAdoption() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/{ownerId}/{petId}/delete", AdoptionControllerTest.TEST_OWNER_ID, AdoptionControllerTest.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.view().name("adoptions/adoptionsOwner"));
	}
}
