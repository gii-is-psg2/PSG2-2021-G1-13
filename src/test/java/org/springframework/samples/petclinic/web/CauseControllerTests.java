package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers= CauseController.class,
includeFilters= {@ComponentScan.Filter(value = CauseValidator.class, type = FilterType.ASSIGNABLE_TYPE )},
excludeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class CauseControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CauseService causeService;

	@MockBean
	private OwnerService ownerService;

	@Mock
	private User user;

	private Cause cause;

	private Donation donation;

	@BeforeEach
	void setup() {

		//Hay que preparar un user
		this.user = new User();
		this.user.setUsername("spring");
		this.user.setPassword("spring");
		final Authorities auth = new Authorities();
		auth.setUser(this.user);
		auth.setAuthority("owner");
		auth.setId(1);
		final Set<Authorities> auths = new HashSet<Authorities>();
		auths.add(auth);
		this.user.setAuthorities(auths);


		//Hay que preparar un owner
		final Owner owner = new Owner();
		owner.setAddress("Calle Piruleta");
		owner.setCity("Sevilla");
		owner.setFirstName("Amelia");
		owner.setLastName("Watson");
		owner.setId(1);
		owner.setTelephone("123456789");
		owner.setUser(this.user);
		final List<Owner> owners = new ArrayList<Owner>();
		BDDMockito.given(this.ownerService.findOwnerByLastName("")).willReturn(owners);
		BDDMockito.given(this.ownerService.getUser(this.user.getUsername())).willReturn(this.user);
		BDDMockito.given(this.ownerService.findOwnersByUsername(this.user.getUsername())).willReturn(owners);

		//Hay que preparar donations
		this.donation = new Donation();
		this.donation.setAmount(100.0);
		this.donation.setClient(new Owner());
		this.donation.setDate(LocalDate.of(2021, 4, 15));
		this.donation.setId(1);
		final Set<Donation> donations_set = new HashSet<Donation>();
		final List<Donation> donations_list = new ArrayList<Donation>();
		donations_set.add(this.donation);
		donations_list.add(this.donation);
		BDDMockito.given(this.causeService.findAllDonations()).willReturn(donations_list);
		BDDMockito.given(this.causeService.findDonationsByCause(1)).willReturn(donations_list);
		BDDMockito.given(this.causeService.findDonationById(1)).willReturn(this.donation);

		this.cause = new Cause();
		this.cause.setName("CauseName");
		this.cause.setClosed(false);
		this.cause.setDescription("CauseDescription");
		this.cause.setDonations(donations_set);
		this.cause.setOrganization("CauseOrganization");
		this.cause.setTarget(1000.0);
		this.cause.setId(1);
		BDDMockito.given(this.causeService.findCauseById(1)).willReturn(this.cause);
		final List<Cause> causes = new ArrayList<Cause>();
		causes.add(this.cause);
		BDDMockito.given(this.causeService.findAllCauses()).willReturn(causes);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListCause() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("causes"))
		.andExpect(MockMvcResultMatchers.view().name("causes/causesList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCauseDetails() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("donations"))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("name", Matchers.is(this.cause.getName()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("description", Matchers.is(this.cause.getDescription()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("target", Matchers.is(this.cause.getTarget()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("organization", Matchers.is(this.cause.getOrganization()))))
				.andExpect(MockMvcResultMatchers.view().name("causes/causeDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormCause() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
		.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormDonation() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/donate", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("donation"))
		.andExpect(MockMvcResultMatchers.view().name("causes/createDonationForm"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormCauseSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/new").param("name", "pruebaName")
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "pruebaDescription")
						.param("target", "1000")
						.param("organization", "Apple"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormCauseHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/new").param("name", "pruebaName")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "pruebaDescription")
				.param("target", "-1000")
				.param("organization", "Apple"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "target"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormDonationHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/donate", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("amount", "-100"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("donation"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("donation", "owner"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("donation", "amount"))
			.andExpect(MockMvcResultMatchers.view().name("causes/createDonationForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormCause() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/edit", 1)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("name", Matchers.is(this.cause.getName()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("description", Matchers.is(this.cause.getDescription()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("target", Matchers.is(this.cause.getTarget()))))
				.andExpect(MockMvcResultMatchers.model().attribute("cause", Matchers.hasProperty("organization", Matchers.is(this.cause.getOrganization()))))
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormCauseSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", 1)
					.param("name", "pruebaNameActualizada")
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "pruebaDescription")
					.param("target", "1000")
					.param("organization", "Apple"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormCauseHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", 1)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "pruebaDescription")
					.param("target", "1000")
					.param("organization", "Apple"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cause", "name"))
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteCauseSuccesful() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/delete", 1))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/"));

		Mockito.verify(this.causeService).deleteCauseById(1);
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteCauseHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/delete", 2))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/"));

		Mockito.verify(this.causeService, Mockito.times(0)).deleteCauseById(2);
	}



}
