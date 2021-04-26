package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CauseService {

    private final CauseRepository causeRepository;
    private final DonationRepository donationRepository;

    @Autowired
    public CauseService(final CauseRepository causeRepository, final DonationRepository donationRepository) {
        this.causeRepository = causeRepository;
        this.donationRepository = donationRepository;
    }

    public Cause findCauseById(final int id) throws DataAccessException{
        return this.causeRepository.findById(id).orElse(null);
    }

    public Donation findDonationById(final int id) throws DataAccessException{
        return this.donationRepository.findById(id).orElse(null);
    }

    public List<Cause> findAllCauses() throws DataAccessException{
        final List<Cause> result = new ArrayList<>();
        this.causeRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public void saveCause(final Cause cause) throws DataAccessException{
        this.causeRepository.save(cause);
    }

    @Transactional
    public void saveDonation(final Donation donation) throws DataAccessException{
        this.donationRepository.save(donation);
    }

    @Transactional
    public void deleteCauseById(final int id) throws DataAccessException{
        this.causeRepository.deleteById(id);
    }

    @Transactional
    public void deleteDonationById(final int id) throws DataAccessException{
        this.donationRepository.deleteById(id);
    }

    public List<Donation> findAllDonations(){
    	return this.donationRepository.findAll();
    }

    public List<Donation> findDonationsByCause(final int id) {
		return this.donationRepository.findDonationsByCauseId(id);
	}
}
