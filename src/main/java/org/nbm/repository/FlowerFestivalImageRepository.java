package org.nbm.repository;

import org.nbm.domain.FlowerFestivalImage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FlowerFestivalImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowerFestivalImageRepository extends JpaRepository<FlowerFestivalImage, Long> {

}
