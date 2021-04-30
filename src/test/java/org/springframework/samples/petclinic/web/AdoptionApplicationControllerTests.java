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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(value = AdoptionApplicationController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
public class AdoptionApplicationControllerTests {

	private static final int TEST_ADOPTION_APPLICATION_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	
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
		given(this.adoptionApplicationService.findById(TEST_ADOPTION_APPLICATION_ID)).willReturn(adoptionApplication);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitAdoptionApplicationDetails() throws Exception{
		mockMvc.perform(get("/adoptionApplication/{ownerId}/{adoptionApplicationId}", TEST_OWNER_ID,TEST_ADOPTION_APPLICATION_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("ownerId")).andExpect(model().attributeExists("adoptionApplication"));
	}
}
