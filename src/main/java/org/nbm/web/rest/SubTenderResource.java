package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.SubTender;
import org.nbm.service.SubTenderService;
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
 * REST controller for managing SubTender.
 */
@RestController
@RequestMapping("/api")
public class SubTenderResource {

    private final Logger log = LoggerFactory.getLogger(SubTenderResource.class);

    private static final String ENTITY_NAME = "subTender";

    private final SubTenderService subTenderService;

    public SubTenderResource(SubTenderService subTenderService) {
        this.subTenderService = subTenderService;
    }

    /**
     * POST  /sub-tenders : Create a new subTender.
     *
     * @param subTender the subTender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subTender, or with status 400 (Bad Request) if the subTender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-tenders")
    @Timed
    public ResponseEntity<SubTender> createSubTender(@Valid @RequestBody SubTender subTender) throws URISyntaxException {
        log.debug("REST request to save SubTender : {}", subTender);
        if (subTender.getId() != null) {
            throw new BadRequestAlertException("A new subTender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubTender result = subTenderService.save(subTender);
        return ResponseEntity.created(new URI("/api/sub-tenders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-tenders : Updates an existing subTender.
     *
     * @param subTender the subTender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subTender,
     * or with status 400 (Bad Request) if the subTender is not valid,
     * or with status 500 (Internal Server Error) if the subTender couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-tenders")
    @Timed
    public ResponseEntity<SubTender> updateSubTender(@Valid @RequestBody SubTender subTender) throws URISyntaxException {
        log.debug("REST request to update SubTender : {}", subTender);
        if (subTender.getId() == null) {
            return createSubTender(subTender);
        }
        SubTender result = subTenderService.save(subTender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subTender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-tenders : get all the subTenders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subTenders in body
     */
    @GetMapping("/sub-tenders")
    @Timed
    public ResponseEntity<List<SubTender>> getAllSubTenders(Pageable pageable) {
        log.debug("REST request to get a page of SubTenders");
        Page<SubTender> page = subTenderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-tenders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sub-tenders/:id : get the "id" subTender.
     *
     * @param id the id of the subTender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subTender, or with status 404 (Not Found)
     */
    @GetMapping("/sub-tenders/{id}")
    @Timed
    public ResponseEntity<SubTender> getSubTender(@PathVariable Long id) {
        log.debug("REST request to get SubTender : {}", id);
        SubTender subTender = subTenderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subTender));
    }

    /**
     * DELETE  /sub-tenders/:id : delete the "id" subTender.
     *
     * @param id the id of the subTender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-tenders/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubTender(@PathVariable Long id) {
        log.debug("REST request to delete SubTender : {}", id);
        subTenderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
