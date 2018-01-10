package org.nbm.service;

import org.nbm.domain.LatestActivity;
import org.nbm.repository.LatestActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing LatestActivity.
 */
@Service
@Transactional
public class LatestActivityService {

    private final Logger log = LoggerFactory.getLogger(LatestActivityService.class);

    private final LatestActivityRepository latestActivityRepository;

    public LatestActivityService(LatestActivityRepository latestActivityRepository) {
        this.latestActivityRepository = latestActivityRepository;
    }

    /**
     * Save a latestActivity.
     *
     * @param latestActivity the entity to save
     * @return the persisted entity
     */
    public LatestActivity save(LatestActivity latestActivity) {
        log.debug("Request to save LatestActivity : {}", latestActivity);
        return latestActivityRepository.save(latestActivity);
    }

    /**
     * Get all the latestActivities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LatestActivity> findAll(Pageable pageable) {
        log.debug("Request to get all LatestActivities");
        return latestActivityRepository.findAll(pageable);
    }

    /**
     * Get one latestActivity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LatestActivity findOne(Long id) {
        log.debug("Request to get LatestActivity : {}", id);
        return latestActivityRepository.findOne(id);
    }

    /**
     * Delete the latestActivity by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LatestActivity : {}", id);
        latestActivityRepository.delete(id);
    }
}
