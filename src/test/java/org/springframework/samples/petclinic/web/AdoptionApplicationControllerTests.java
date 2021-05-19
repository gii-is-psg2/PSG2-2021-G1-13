package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AdoptionApplicationController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class AdoptionApplicationControllerTests {

	private static final int TEST_ADOPTION_APPLICATION_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_ADOPTION_ID = 1;

	@Autowired
	private AdoptionApplicationController adoptionApplicationController;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private AdoptionService adoptionService;

	@MockBean
	private AdoptionApplicationService adoptionApplicationService;

	@Autowired
	private MockMvc mockMvc;

	private AdoptionApplication adoptionApplication;

	@BeforeEach
	void setUp() {
		adoptionApplication = new AdoptionApplication();
		adoptionApplication.setId(TEST_ADOPTION_APPLICATION_ID);
		adoptionApplication.setDescription("Esto es una prueba");
		adoptionApplication.setApproved(Boolean.FALSE);

		List<AdoptionApplication> adoptionApplications = new ArrayList<AdoptionApplication>();
		adoptionApplications.add(adoptionApplication);
		given(this.adoptionApplicationService.findApplicationsByAdoption(TEST_ADOPTION_ID)).willReturn(adoptionApplications);
		given(this.adoptionApplicationService.findById(anyInt())).willReturn(adoptionApplication);
	}

	@WithMockUser(value = "spring")
    @Test
    void testApplicationsByAdoption() throws Exception{
		mockMvc.perform(get("/adoptionApplication/{adoptionId}/{ownerId}", TEST_ADOPTION_ID ,TEST_OWNER_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("ownerId")).andExpect(model().attributeExists("adoptionApplicationDetails"));
	}


	@WithMockUser(value = "spring")
    @Test
    void testAcceptAdoptionApplication() throws Exception{
		mockMvc.perform(get("/adoptionApplication/{ownerId}/{adoptionApplicationId}/accept", TEST_OWNER_ID ,TEST_ADOPTION_APPLICATION_ID)).andExpect(status().isOk());

	}

	@WithMockUser(value = "spring")
    @Test
    void testRejectAdoptionApplication() throws Exception{
		mockMvc.perform(get("/adoptionApplication/{ownerId}/{adoptionApplicationId}/reject", TEST_OWNER_ID ,TEST_ADOPTION_APPLICATION_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("ownerId"));

		verify(this.adoptionApplicationService).deleteById(anyInt());
	}

	@WithMockUser(value = "spring")
    @Test
    void testInitFormAdoptionApplication() throws Exception{
		mockMvc.perform(get("/adoptionApplication/new/{ownerId}/{adoptionId}", TEST_OWNER_ID ,TEST_ADOPTION_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("adoptionApplication")).andExpect(view().name("adoptionApplications/formAdoptionApplication"));

	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessFormAdoptionApplicationSucess() throws Exception{
		mockMvc.perform(post("/adoptionApplication/new").with(csrf())
				.param("approved", "false").param("description", "Esto es una prueba"))
		.andExpect(model().attributeExists("message")).andExpect(view().name("welcome")).andExpect(status().isOk());

	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessFormAdoptionApplicationErrors() throws Exception{
		mockMvc.perform(post("/adoptionApplication/new").with(csrf())
				.param("approved", "false"))
		.andExpect(model().attributeHasErrors("adoptionApplication")).andExpect(view().name("adoptionApplications/formAdoptionApplication"))
		.andExpect(status().isOk());

	}
}
