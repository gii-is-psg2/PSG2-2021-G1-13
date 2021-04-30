package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;

@ExtendWith(MockitoExtension.class)
public class CauseServiceTests {
	
	@Mock
	private CauseRepository causeRepo;
	
	@Mock
	private DonationRepository donationRepo;
	
	protected CauseService causeService;
	
	@BeforeEach
    void setup() {
		this.causeService = new CauseService(this.causeRepo,this.donationRepo);
    }
	
	@Test
	void addingCause() {
		final Cause new_cause = new Cause();
		new_cause.setName("CauseName");
		new_cause.setClosed(false);
		new_cause.setDescription("CauseDescription");
		new_cause.setDonations(new HashSet<Donation>());
		new_cause.setOrganization("CauseOrganization");
		new_cause.setTarget(1000.0);
		new_cause.setId(1);
		final Collection<Cause> sampleCauses = new ArrayList<Cause>();
		sampleCauses.add(new_cause);
        Mockito.when(this.causeRepo.findAll()).thenReturn(sampleCauses);
        Mockito.when(this.causeRepo.findById(1)).thenReturn(Optional.of(new_cause));
		
        
		final List<Cause> causes = StreamSupport.stream(this.causeService.findAllCauses().spliterator(), false).collect(Collectors.toList());
		Cause saved_cause = causes.get(causes.size()-1);
		Assertions.assertTrue(saved_cause.getName().equals("CauseName"));
		Assertions.assertTrue(saved_cause.getClosed()==false);
		Assertions.assertTrue(saved_cause.getDescription().equals("CauseDescription"));
		Assertions.assertTrue(saved_cause.getOrganization().equals("CauseOrganization"));
		Assertions.assertTrue(saved_cause.getTarget()==1000.0);
		
		saved_cause = this.causeService.findCauseById(1);
		Assertions.assertTrue(saved_cause.getName().equals("CauseName"));
		Assertions.assertTrue(saved_cause.getClosed()==false);
		Assertions.assertTrue(saved_cause.getDescription().equals("CauseDescription"));
		Assertions.assertTrue(saved_cause.getOrganization().equals("CauseOrganization"));
		Assertions.assertTrue(saved_cause.getTarget()==1000.0);
	}
	
	@Test
	void addingDonation() {
		final Donation new_donation = new Donation();
		new_donation.setAmount(100.0);
		new_donation.setCause(new Cause());
		new_donation.setClient(new Owner());
		new_donation.setDate(LocalDate.of(2021, 4, 15));
		new_donation.setId(1);
		final List<Donation> sampleDonations = new ArrayList<Donation>();
		sampleDonations.add(new_donation);
        Mockito.when(this.donationRepo.findAll()).thenReturn(sampleDonations);
        Mockito.when(this.donationRepo.findById(1)).thenReturn(Optional.of(new_donation));
        
        final List<Donation> donations = StreamSupport.stream(this.causeService.findAllDonations().spliterator(), false).collect(Collectors.toList());
		Donation saved_donation = donations.get(donations.size()-1);
		Assertions.assertTrue(saved_donation.getDate().isEqual(LocalDate.of(2021, 4, 15)));
		Assertions.assertTrue(saved_donation.getAmount()==100.0);
		
		saved_donation = this.causeService.findDonationById(1);
		Assertions.assertTrue(saved_donation.getDate().isEqual(LocalDate.of(2021, 4, 15)));
		Assertions.assertTrue(saved_donation.getAmount()==100.0);
		
	}

}
