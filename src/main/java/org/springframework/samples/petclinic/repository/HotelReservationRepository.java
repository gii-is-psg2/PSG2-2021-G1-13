package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.HotelReservation;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelReservationRepository  extends CrudRepository<HotelReservation, Integer>{

}
