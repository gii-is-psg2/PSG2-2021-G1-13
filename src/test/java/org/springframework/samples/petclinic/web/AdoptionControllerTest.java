package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=AdoptionController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class AdoptionControllerTest {

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
		adoption = adoptionService.findById(TEST_ADOPTION_ID);
		owner = ownerService.findOwnerById(TEST_OWNER_ID);
		pet = petService.findPetById(TEST_PET_ID);
		given(this.adoptionService.findById(TEST_ADOPTION_ID)).willReturn(adoption);
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(owner);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(pet);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
	mockMvc.perform(get("/adoptions/{ownerId}/{petId}/new", TEST_OWNER_ID, TEST_PET_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("adoption"))
			.andExpect(view().name("adoptions/formAdoption"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testSelectOwner() throws Exception {
	mockMvc.perform(get("/adoptions")).andExpect(status().isOk()).andExpect(model().attributeExists("ownerList"))
			.andExpect(view().name("adoptions/selectOwner"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testListOwnerAdoptions() throws Exception {
	mockMvc.perform(get("/adoptions/{ownerId}", TEST_OWNER_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("adoptions"))
			.andExpect(view().name("adoptions/adoptionsOwner"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testListAdoptions() throws Exception {
	mockMvc.perform(get("/adoptions/{ownerId}/list", TEST_OWNER_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("adoptions"))
			.andExpect(view().name("adoptions/adoptionList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testAdoptionMenu() throws Exception {
	mockMvc.perform(get("/adoptions/{ownerId}/list", TEST_OWNER_ID)).andExpect(status().isOk())
			.andExpect(view().name("adoptions/adoptionList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitSendForm() throws Exception {
		mockMvc.perform(post("/adoptions/"+TEST_OWNER_ID+"/"+TEST_ADOPTION_ID+"/new")
				.with(csrf())
				.param("description", "test")
				.param("pet", "1"))
	.andExpect(status().isOk())
	.andExpect(view().name("adoptions/adoptionList"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
    void testInitSendFormError() throws Exception {
		mockMvc.perform(post("/adoptions/"+TEST_OWNER_ID+"/"+TEST_ADOPTION_ID+"/new")
				.with(csrf())
				.param("pet", "1"))
	.andExpect(status().isOk())
	.andExpect(model().attributeHasErrors("adoption"))
	.andExpect(model().attributeHasFieldErrors("adoption", "description"))
	.andExpect(view().name("adoptions/formAdoption"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testDeleteAdoption() throws Exception {
	mockMvc.perform(get("/adoptions/{ownerId}/{petId}/delete", TEST_OWNER_ID, TEST_PET_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("message"))
			.andExpect(view().name("adoptions/adoptionsOwner"));
	}
}
