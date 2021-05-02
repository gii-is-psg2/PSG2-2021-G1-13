package org.springframework.samples.petclinic.web;

import org.hamcrest.Matchers;
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
import org.springframework.samples.petclinic.service.HotelReservationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers= HotelReservationController.class,
includeFilters= {@ComponentScan.Filter(value = PetFormatter.class, type = FilterType.ASSIGNABLE_TYPE ),
				@ComponentScan.Filter(value = HotelReservationValidator.class, type = FilterType.ASSIGNABLE_TYPE )},
excludeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class HotelReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HotelReservationService reservationService;

	private HotelReservation reservation;

	@BeforeEach
	void setup() {

		final Authorities auth = new Authorities();
		auth.setAuthority("admin");
		auth.setId(1);
		auth.setUser(new User());
		BDDMockito.given(this.reservationService.getAuthority("spring")).willReturn(auth);

		final Pet pet = new Pet();
		pet.setName("Coco");
		pet.setId(1);
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		pet.setType(new PetType());
		final List<Pet> pets = new ArrayList<Pet>();
		pets.add(pet);
		BDDMockito.given(this.reservationService.findPets()).willReturn(pets);

		this.reservation = new HotelReservation();
		this.reservation.setId(1);
		this.reservation.setPet(pet);
		this.reservation.setStart(LocalDate.of(2025, 4, 1));
		this.reservation.setFinish(LocalDate.of(2025, 4, 5));
		BDDMockito.given(this.reservationService.findHotelReservationById(1)).willReturn(Optional.of(this.reservation));
		final List<HotelReservation> reservations = new ArrayList<HotelReservation>();
		reservations.add(this.reservation);
		BDDMockito.given(this.reservationService.findByPet(pet)).willReturn(reservations);
		BDDMockito.given(this.reservationService.findAll()).willReturn(reservations);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListHotelReservations() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/hotelreservations")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("hotelreservations"))
		.andExpect(MockMvcResultMatchers.view().name("hotelreservations/hotelReservationsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/hotelreservations/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("hotelReservation"))
		.andExpect(MockMvcResultMatchers.view().name("hotelreservations/addHotelReservation"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/hotelreservations/save").param("pet", "Coco")
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("start", "2025/04/10")
						.param("finish", "2025/04/15"))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(MockMvcResultMatchers.view().name("hotelreservations/hotelReservationsList"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/hotelreservations/save")
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("start", "2025/04/10")
						.param("finish", "2025/04/15"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("hotelReservation"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("hotelReservation", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("hotelreservations/addHotelReservation"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/hotelreservations/edit/{hotelReservationId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("hotelReservation"))
				.andExpect(MockMvcResultMatchers.model().attribute("hotelReservation", Matchers.hasProperty("pet", Matchers.is(this.reservation.getPet()))))
				.andExpect(MockMvcResultMatchers.model().attribute("hotelReservation", Matchers.hasProperty("start", Matchers.is(this.reservation.getStart()))))
				.andExpect(MockMvcResultMatchers.model().attribute("hotelReservation", Matchers.hasProperty("finish", Matchers.is(this.reservation.getFinish()))))
				.andExpect(MockMvcResultMatchers.view().name("hotelreservations/updateHotelReservation"));
	}

    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/hotelreservations/edit/{hotelReservationId}", 1)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("pet", "Coco")
							.param("start", "2025/04/10")
							.param("finish", "2025/04/15"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/hotelreservations"));
	}

    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/hotelreservations/edit/{hotelReservationId}", 1)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("pet", "Coco")
							.param("finish", "2025/04/15"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("hotelReservation"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("hotelReservation", "start"))
				.andExpect(MockMvcResultMatchers.view().name("hotelreservations/updateHotelReservation"));
	}

}
