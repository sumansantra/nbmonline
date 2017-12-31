package org.nbm.service;

import org.nbm.domain.SubTender;
import org.nbm.repository.SubTenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SubTender.
 */
@Service
@Transactional
public class SubTenderService {

    private final Logger log = LoggerFactory.getLogger(SubTenderService.class);

    private final SubTenderRepository subTenderRepository;

    public SubTenderService(SubTenderRepository subTenderRepository) {
        this.subTenderRepository = subTenderRepository;
    }

    /**
     * Save a subTender.
     *
     * @param subTender the entity to save
     * @return the persisted entity
     */
    public SubTender save(SubTender subTender) {
        log.debug("Request to save SubTender : {}", subTender);
        return subTenderRepository.save(subTender);
    }

    /**
     * Get all the subTenders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubTender> findAll(Pageable pageable) {
        log.debug("Request to get all SubTenders");
        return subTenderRepository.findAll(pageable);
    }

    /**
     * Get one subTender by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubTender findOne(Long id) {
        log.debug("Request to get SubTender : {}", id);
        return subTenderRepository.findOne(id);
    }

    /**
     * Delete the subTender by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SubTender : {}", id);
        subTenderRepository.delete(id);
    }
}
