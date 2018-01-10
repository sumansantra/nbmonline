package org.nbm.repository;

import org.nbm.domain.FlowerFestival;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FlowerFestival entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowerFestivalRepository extends JpaRepository<FlowerFestival, Long> {

}
