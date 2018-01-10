package org.nbm.service;

import org.nbm.domain.Notice;
import org.nbm.repository.NoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Notice.
 */
@Service
@Transactional
public class NoticeService {

    private final Logger log = LoggerFactory.getLogger(NoticeService.class);

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    /**
     * Save a notice.
     *
     * @param notice the entity to save
     * @return the persisted entity
     */
    public Notice save(Notice notice) {
        log.debug("Request to save Notice : {}", notice);
        return noticeRepository.save(notice);
    }

    /**
     * Get all the notices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Notice> findAll(Pageable pageable) {
        log.debug("Request to get all Notices");
        return noticeRepository.findAll(pageable);
    }

    /**
     * Get one notice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Notice findOne(Long id) {
        log.debug("Request to get Notice : {}", id);
        return noticeRepository.findOne(id);
    }

    /**
     * Delete the notice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notice : {}", id);
        noticeRepository.delete(id);
    }
}
