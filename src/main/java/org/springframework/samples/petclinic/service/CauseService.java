package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class CauseService {

    private final CauseRepository causeRepository;
    private final DonationRepository donationRepository;

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

    public void saveCause(final Cause cause) throws DataAccessException{
        this.causeRepository.save(cause);
    }

    public void saveDonation(final Donation donation) throws DataAccessException{
        this.donationRepository.save(donation);
    }

    public void deleteCauseById(final int id) throws DataAccessException{
        this.causeRepository.deleteById(id);
    }

    public void deleteDonationById(final int id) throws DataAccessException{
        this.donationRepository.deleteById(id);
    }
}
