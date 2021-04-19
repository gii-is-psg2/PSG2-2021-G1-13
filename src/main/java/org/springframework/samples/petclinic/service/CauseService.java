package org.springframework.samples.petclinic.service;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Service
public class CauseService {

    private CauseRepository causeRepository;
    private DonationRepository donationRepository;

    public CauseService(CauseRepository causeRepository, DonationRepository donationRepository) {
        this.causeRepository = causeRepository;
        this.donationRepository = donationRepository;
    }

    public Cause findCauseById(int id) throws DataAccessException{
        return this.causeRepository.findById(id).orElse(null);
    }

    public Donation findDonationById(int id) throws DataAccessException{
        return this.donationRepository.findById(id).orElse(null);
    }

    public List<Cause> findAllCauses() throws DataAccessException{
        List<Cause> result = new ArrayList<>();
        this.causeRepository.findAll().forEach(result::add);
        return result;
    }

    public void saveCause(Cause cause) throws DataAccessException{
        this.causeRepository.save(cause);
    }

    public void saveDonation(Donation donation) throws DataAccessException{
        this.donationRepository.save(donation);
    }

    public void deleteCauseById(int id) throws DataAccessException{
        this.causeRepository.deleteById(id);
    }

    public void deleteDonationById(int id) throws DataAccessException{
        this.donationRepository.deleteById(id);
    }
}
