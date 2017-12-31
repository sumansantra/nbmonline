package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.Tender;
import org.nbm.service.TenderService;
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
 * REST controller for managing Tender.
 */
@RestController
@RequestMapping("/api")
public class TenderResource {

    private final Logger log = LoggerFactory.getLogger(TenderResource.class);

    private static final String ENTITY_NAME = "tender";

    private final TenderService tenderService;

    public TenderResource(TenderService tenderService) {
        this.tenderService = tenderService;
    }

    /**
     * POST  /tenders : Create a new tender.
     *
     * @param tender the tender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tender, or with status 400 (Bad Request) if the tender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tenders")
    @Timed
    public ResponseEntity<Tender> createTender(@Valid @RequestBody Tender tender) throws URISyntaxException {
        log.debug("REST request to save Tender : {}", tender);
        if (tender.getId() != null) {
            throw new BadRequestAlertException("A new tender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tender result = tenderService.save(tender);
        return ResponseEntity.created(new URI("/api/tenders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tenders : Updates an existing tender.
     *
     * @param tender the tender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tender,
     * or with status 400 (Bad Request) if the tender is not valid,
     * or with status 500 (Internal Server Error) if the tender couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tenders")
    @Timed
    public ResponseEntity<Tender> updateTender(@Valid @RequestBody Tender tender) throws URISyntaxException {
        log.debug("REST request to update Tender : {}", tender);
        if (tender.getId() == null) {
            return createTender(tender);
        }
        Tender result = tenderService.save(tender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tenders : get all the tenders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tenders in body
     */
    @GetMapping("/tenders")
    @Timed
    public ResponseEntity<List<Tender>> getAllTenders(Pageable pageable) {
        log.debug("REST request to get a page of Tenders");
        Page<Tender> page = tenderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tenders/:id : get the "id" tender.
     *
     * @param id the id of the tender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tender, or with status 404 (Not Found)
     */
    @GetMapping("/tenders/{id}")
    @Timed
    public ResponseEntity<Tender> getTender(@PathVariable Long id) {
        log.debug("REST request to get Tender : {}", id);
        Tender tender = tenderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tender));
    }

    /**
     * DELETE  /tenders/:id : delete the "id" tender.
     *
     * @param id the id of the tender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tenders/{id}")
    @Timed
    public ResponseEntity<Void> deleteTender(@PathVariable Long id) {
        log.debug("REST request to delete Tender : {}", id);
        tenderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
