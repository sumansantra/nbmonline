package org.nbm.service;

import org.nbm.domain.FlowerFestivalImage;
import org.nbm.repository.FlowerFestivalImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FlowerFestivalImage.
 */
@Service
@Transactional
public class FlowerFestivalImageService {

    private final Logger log = LoggerFactory.getLogger(FlowerFestivalImageService.class);

    private final FlowerFestivalImageRepository flowerFestivalImageRepository;

    public FlowerFestivalImageService(FlowerFestivalImageRepository flowerFestivalImageRepository) {
        this.flowerFestivalImageRepository = flowerFestivalImageRepository;
    }

    /**
     * Save a flowerFestivalImage.
     *
     * @param flowerFestivalImage the entity to save
     * @return the persisted entity
     */
    public FlowerFestivalImage save(FlowerFestivalImage flowerFestivalImage) {
        log.debug("Request to save FlowerFestivalImage : {}", flowerFestivalImage);
        return flowerFestivalImageRepository.save(flowerFestivalImage);
    }

    /**
     * Get all the flowerFestivalImages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FlowerFestivalImage> findAll(Pageable pageable) {
        log.debug("Request to get all FlowerFestivalImages");
        return flowerFestivalImageRepository.findAll(pageable);
    }

    /**
     * Get one flowerFestivalImage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FlowerFestivalImage findOne(Long id) {
        log.debug("Request to get FlowerFestivalImage : {}", id);
        return flowerFestivalImageRepository.findOne(id);
    }

    /**
     * Delete the flowerFestivalImage by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FlowerFestivalImage : {}", id);
        flowerFestivalImageRepository.delete(id);
    }
}
