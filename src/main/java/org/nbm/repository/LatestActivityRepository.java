package org.nbm.repository;

import org.nbm.domain.LatestActivity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LatestActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LatestActivityRepository extends JpaRepository<LatestActivity, Long> {

}
