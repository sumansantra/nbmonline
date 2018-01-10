package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.FlowerFestival;
import org.nbm.service.FlowerFestivalService;
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
 * REST controller for managing FlowerFestival.
 */
@RestController
@RequestMapping("/api")
public class FlowerFestivalResource {

    private final Logger log = LoggerFactory.getLogger(FlowerFestivalResource.class);

    private static final String ENTITY_NAME = "flowerFestival";

    private final FlowerFestivalService flowerFestivalService;

    public FlowerFestivalResource(FlowerFestivalService flowerFestivalService) {
        this.flowerFestivalService = flowerFestivalService;
    }

    /**
     * POST  /flower-festivals : Create a new flowerFestival.
     *
     * @param flowerFestival the flowerFestival to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flowerFestival, or with status 400 (Bad Request) if the flowerFestival has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flower-festivals")
    @Timed
    public ResponseEntity<FlowerFestival> createFlowerFestival(@Valid @RequestBody FlowerFestival flowerFestival) throws URISyntaxException {
        log.debug("REST request to save FlowerFestival : {}", flowerFestival);
        if (flowerFestival.getId() != null) {
            throw new BadRequestAlertException("A new flowerFestival cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowerFestival result = flowerFestivalService.save(flowerFestival);
        return ResponseEntity.created(new URI("/api/flower-festivals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flower-festivals : Updates an existing flowerFestival.
     *
     * @param flowerFestival the flowerFestival to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flowerFestival,
     * or with status 400 (Bad Request) if the flowerFestival is not valid,
     * or with status 500 (Internal Server Error) if the flowerFestival couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flower-festivals")
    @Timed
    public ResponseEntity<FlowerFestival> updateFlowerFestival(@Valid @RequestBody FlowerFestival flowerFestival) throws URISyntaxException {
        log.debug("REST request to update FlowerFestival : {}", flowerFestival);
        if (flowerFestival.getId() == null) {
            return createFlowerFestival(flowerFestival);
        }
        FlowerFestival result = flowerFestivalService.save(flowerFestival);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, flowerFestival.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flower-festivals : get all the flowerFestivals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of flowerFestivals in body
     */
    @GetMapping("/flower-festivals")
    @Timed
    public ResponseEntity<List<FlowerFestival>> getAllFlowerFestivals(Pageable pageable) {
        log.debug("REST request to get a page of FlowerFestivals");
        Page<FlowerFestival> page = flowerFestivalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flower-festivals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flower-festivals/:id : get the "id" flowerFestival.
     *
     * @param id the id of the flowerFestival to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flowerFestival, or with status 404 (Not Found)
     */
    @GetMapping("/flower-festivals/{id}")
    @Timed
    public ResponseEntity<FlowerFestival> getFlowerFestival(@PathVariable Long id) {
        log.debug("REST request to get FlowerFestival : {}", id);
        FlowerFestival flowerFestival = flowerFestivalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(flowerFestival));
    }

    /**
     * DELETE  /flower-festivals/:id : delete the "id" flowerFestival.
     *
     * @param id the id of the flowerFestival to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flower-festivals/{id}")
    @Timed
    public ResponseEntity<Void> deleteFlowerFestival(@PathVariable Long id) {
        log.debug("REST request to delete FlowerFestival : {}", id);
        flowerFestivalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
