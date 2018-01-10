package org.nbm.service;

import org.nbm.domain.FlowerFestival;
import org.nbm.repository.FlowerFestivalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FlowerFestival.
 */
@Service
@Transactional
public class FlowerFestivalService {

    private final Logger log = LoggerFactory.getLogger(FlowerFestivalService.class);

    private final FlowerFestivalRepository flowerFestivalRepository;

    public FlowerFestivalService(FlowerFestivalRepository flowerFestivalRepository) {
        this.flowerFestivalRepository = flowerFestivalRepository;
    }

    /**
     * Save a flowerFestival.
     *
     * @param flowerFestival the entity to save
     * @return the persisted entity
     */
    public FlowerFestival save(FlowerFestival flowerFestival) {
        log.debug("Request to save FlowerFestival : {}", flowerFestival);
        return flowerFestivalRepository.save(flowerFestival);
    }

    /**
     * Get all the flowerFestivals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FlowerFestival> findAll(Pageable pageable) {
        log.debug("Request to get all FlowerFestivals");
        return flowerFestivalRepository.findAll(pageable);
    }

    /**
     * Get one flowerFestival by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FlowerFestival findOne(Long id) {
        log.debug("Request to get FlowerFestival : {}", id);
        return flowerFestivalRepository.findOne(id);
    }

    /**
     * Delete the flowerFestival by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FlowerFestival : {}", id);
        flowerFestivalRepository.delete(id);
    }
}
