package org.nbm.service;

import org.nbm.domain.Tender;
import org.nbm.repository.TenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Tender.
 */
@Service
@Transactional
public class TenderService {

    private final Logger log = LoggerFactory.getLogger(TenderService.class);

    private final TenderRepository tenderRepository;

    public TenderService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    /**
     * Save a tender.
     *
     * @param tender the entity to save
     * @return the persisted entity
     */
    public Tender save(Tender tender) {
        log.debug("Request to save Tender : {}", tender);
        return tenderRepository.save(tender);
    }

    /**
     * Get all the tenders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Tender> findAll(Pageable pageable) {
        log.debug("Request to get all Tenders");
        return tenderRepository.findAll(pageable);
    }

    /**
     * Get one tender by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Tender findOne(Long id) {
        log.debug("Request to get Tender : {}", id);
        return tenderRepository.findOne(id);
    }

    /**
     * Delete the tender by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tender : {}", id);
        tenderRepository.delete(id);
    }
}
