package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.LatestActivity;
import org.nbm.service.LatestActivityService;
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
 * REST controller for managing LatestActivity.
 */
@RestController
@RequestMapping("/api")
public class LatestActivityResource {

    private final Logger log = LoggerFactory.getLogger(LatestActivityResource.class);

    private static final String ENTITY_NAME = "latestActivity";

    private final LatestActivityService latestActivityService;

    public LatestActivityResource(LatestActivityService latestActivityService) {
        this.latestActivityService = latestActivityService;
    }

    /**
     * POST  /latest-activities : Create a new latestActivity.
     *
     * @param latestActivity the latestActivity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new latestActivity, or with status 400 (Bad Request) if the latestActivity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/latest-activities")
    @Timed
    public ResponseEntity<LatestActivity> createLatestActivity(@Valid @RequestBody LatestActivity latestActivity) throws URISyntaxException {
        log.debug("REST request to save LatestActivity : {}", latestActivity);
        if (latestActivity.getId() != null) {
            throw new BadRequestAlertException("A new latestActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LatestActivity result = latestActivityService.save(latestActivity);
        return ResponseEntity.created(new URI("/api/latest-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /latest-activities : Updates an existing latestActivity.
     *
     * @param latestActivity the latestActivity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated latestActivity,
     * or with status 400 (Bad Request) if the latestActivity is not valid,
     * or with status 500 (Internal Server Error) if the latestActivity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/latest-activities")
    @Timed
    public ResponseEntity<LatestActivity> updateLatestActivity(@Valid @RequestBody LatestActivity latestActivity) throws URISyntaxException {
        log.debug("REST request to update LatestActivity : {}", latestActivity);
        if (latestActivity.getId() == null) {
            return createLatestActivity(latestActivity);
        }
        LatestActivity result = latestActivityService.save(latestActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, latestActivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /latest-activities : get all the latestActivities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of latestActivities in body
     */
    @GetMapping("/latest-activities")
    @Timed
    public ResponseEntity<List<LatestActivity>> getAllLatestActivities(Pageable pageable) {
        log.debug("REST request to get a page of LatestActivities");
        Page<LatestActivity> page = latestActivityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/latest-activities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /latest-activities/:id : get the "id" latestActivity.
     *
     * @param id the id of the latestActivity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the latestActivity, or with status 404 (Not Found)
     */
    @GetMapping("/latest-activities/{id}")
    @Timed
    public ResponseEntity<LatestActivity> getLatestActivity(@PathVariable Long id) {
        log.debug("REST request to get LatestActivity : {}", id);
        LatestActivity latestActivity = latestActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(latestActivity));
    }

    /**
     * DELETE  /latest-activities/:id : delete the "id" latestActivity.
     *
     * @param id the id of the latestActivity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/latest-activities/{id}")
    @Timed
    public ResponseEntity<Void> deleteLatestActivity(@PathVariable Long id) {
        log.debug("REST request to delete LatestActivity : {}", id);
        latestActivityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
