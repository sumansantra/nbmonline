package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.Notice;
import org.nbm.service.NoticeService;
import org.nbm.web.rest.errors.BadRequestAlertException;
import org.nbm.web.rest.util.HeaderUtil;
import org.nbm.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notice.
 */
@RestController
@RequestMapping("/api")
public class NoticeResource {

    private final Logger log = LoggerFactory.getLogger(NoticeResource.class);

    private static final String ENTITY_NAME = "notice";

    private final NoticeService noticeService;

    public NoticeResource(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * POST  /notices : Create a new notice.
     *
     * @param notice the notice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notice, or with status 400 (Bad Request) if the notice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notices")
    @Timed
    public ResponseEntity<Notice> createNotice(@Valid @RequestBody Notice notice) throws URISyntaxException {
        log.debug("REST request to save Notice : {}", notice);
        if (notice.getId() != null) {
            throw new BadRequestAlertException("A new notice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notice result = noticeService.save(notice);
        return ResponseEntity.created(new URI("/api/notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notices : Updates an existing notice.
     *
     * @param notice the notice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notice,
     * or with status 400 (Bad Request) if the notice is not valid,
     * or with status 500 (Internal Server Error) if the notice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notices")
    @Timed
    public ResponseEntity<Notice> updateNotice(@Valid @RequestBody Notice notice) throws URISyntaxException {
        log.debug("REST request to update Notice : {}", notice);
        if (notice.getId() == null) {
            return createNotice(notice);
        }
        Notice result = noticeService.save(notice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notices : get all the notices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notices in body
     */
    @GetMapping("/notices")
    @Timed
    public ResponseEntity<List<Notice>> getAllNotices(Pageable pageable) {
        log.debug("REST request to get a page of Notices");
        Page<Notice> page = noticeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notices/:id : get the "id" notice.
     *
     * @param id the id of the notice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notice, or with status 404 (Not Found)
     */
    @GetMapping("/notices/{id}")
    @Timed
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        log.debug("REST request to get Notice : {}", id);
        Notice notice = noticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notice));
    }

    /**
     * DELETE  /notices/:id : delete the "id" notice.
     *
     * @param id the id of the notice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notices/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        log.debug("REST request to delete Notice : {}", id);
        noticeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
