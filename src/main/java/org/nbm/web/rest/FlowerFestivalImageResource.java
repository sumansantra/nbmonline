package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.FlowerFestivalImage;
import org.nbm.service.FlowerFestivalImageService;
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
 * REST controller for managing FlowerFestivalImage.
 */
@RestController
@RequestMapping("/api")
public class FlowerFestivalImageResource {

    private final Logger log = LoggerFactory.getLogger(FlowerFestivalImageResource.class);

    private static final String ENTITY_NAME = "flowerFestivalImage";

    private final FlowerFestivalImageService flowerFestivalImageService;

    public FlowerFestivalImageResource(FlowerFestivalImageService flowerFestivalImageService) {
        this.flowerFestivalImageService = flowerFestivalImageService;
    }

    /**
     * POST  /flower-festival-images : Create a new flowerFestivalImage.
     *
     * @param flowerFestivalImage the flowerFestivalImage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flowerFestivalImage, or with status 400 (Bad Request) if the flowerFestivalImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flower-festival-images")
    @Timed
    public ResponseEntity<FlowerFestivalImage> createFlowerFestivalImage(@Valid @RequestBody FlowerFestivalImage flowerFestivalImage) throws URISyntaxException {
        log.debug("REST request to save FlowerFestivalImage : {}", flowerFestivalImage);
        if (flowerFestivalImage.getId() != null) {
            throw new BadRequestAlertException("A new flowerFestivalImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowerFestivalImage result = flowerFestivalImageService.save(flowerFestivalImage);
        return ResponseEntity.created(new URI("/api/flower-festival-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flower-festival-images : Updates an existing flowerFestivalImage.
     *
     * @param flowerFestivalImage the flowerFestivalImage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flowerFestivalImage,
     * or with status 400 (Bad Request) if the flowerFestivalImage is not valid,
     * or with status 500 (Internal Server Error) if the flowerFestivalImage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flower-festival-images")
    @Timed
    public ResponseEntity<FlowerFestivalImage> updateFlowerFestivalImage(@Valid @RequestBody FlowerFestivalImage flowerFestivalImage) throws URISyntaxException {
        log.debug("REST request to update FlowerFestivalImage : {}", flowerFestivalImage);
        if (flowerFestivalImage.getId() == null) {
            return createFlowerFestivalImage(flowerFestivalImage);
        }
        FlowerFestivalImage result = flowerFestivalImageService.save(flowerFestivalImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, flowerFestivalImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flower-festival-images : get all the flowerFestivalImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of flowerFestivalImages in body
     */
    @GetMapping("/flower-festival-images")
    @Timed
    public ResponseEntity<List<FlowerFestivalImage>> getAllFlowerFestivalImages(Pageable pageable) {
        log.debug("REST request to get a page of FlowerFestivalImages");
        Page<FlowerFestivalImage> page = flowerFestivalImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flower-festival-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flower-festival-images/:id : get the "id" flowerFestivalImage.
     *
     * @param id the id of the flowerFestivalImage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flowerFestivalImage, or with status 404 (Not Found)
     */
    @GetMapping("/flower-festival-images/{id}")
    @Timed
    public ResponseEntity<FlowerFestivalImage> getFlowerFestivalImage(@PathVariable Long id) {
        log.debug("REST request to get FlowerFestivalImage : {}", id);
        FlowerFestivalImage flowerFestivalImage = flowerFestivalImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(flowerFestivalImage));
    }

    /**
     * DELETE  /flower-festival-images/:id : delete the "id" flowerFestivalImage.
     *
     * @param id the id of the flowerFestivalImage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flower-festival-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteFlowerFestivalImage(@PathVariable Long id) {
        log.debug("REST request to delete FlowerFestivalImage : {}", id);
        flowerFestivalImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
