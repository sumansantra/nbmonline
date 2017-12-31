package org.nbm.repository;

import org.nbm.domain.SubTender;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SubTender entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubTenderRepository extends JpaRepository<SubTender, Long> {

}
