package org.nbm.service;

import org.nbm.domain.Galary;
import org.nbm.repository.GalaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Galary.
 */
@Service
@Transactional
public class GalaryService {

    private final Logger log = LoggerFactory.getLogger(GalaryService.class);

    private final GalaryRepository galaryRepository;

    public GalaryService(GalaryRepository galaryRepository) {
        this.galaryRepository = galaryRepository;
    }

    /**
     * Save a galary.
     *
     * @param galary the entity to save
     * @return the persisted entity
     */
    public Galary save(Galary galary) {
        log.debug("Request to save Galary : {}", galary);
        return galaryRepository.save(galary);
    }

    /**
     * Get all the galaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Galary> findAll(Pageable pageable) {
        log.debug("Request to get all Galaries");
        return galaryRepository.findAll(pageable);
    }

    /**
     * Get one galary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Galary findOne(Long id) {
        log.debug("Request to get Galary : {}", id);
        return galaryRepository.findOne(id);
    }

    /**
     * Delete the galary by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Galary : {}", id);
        galaryRepository.delete(id);
    }
}
