package org.nbm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nbm.domain.News;
import org.nbm.service.NewsService;
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
 * REST controller for managing News.
 */
@RestController
@RequestMapping("/api")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    private static final String ENTITY_NAME = "news";

    private final NewsService newsService;

    public NewsResource(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * POST  /news : Create a new news.
     *
     * @param news the news to create
     * @return the ResponseEntity with status 201 (Created) and with body the new news, or with status 400 (Bad Request) if the news has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news")
    @Timed
    public ResponseEntity<News> createNews(@Valid @RequestBody News news) throws URISyntaxException {
        log.debug("REST request to save News : {}", news);
        if (news.getId() != null) {
            throw new BadRequestAlertException("A new news cannot already have an ID", ENTITY_NAME, "idexists");
        }
        News result = newsService.save(news);
        return ResponseEntity.created(new URI("/api/news/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /news : Updates an existing news.
     *
     * @param news the news to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated news,
     * or with status 400 (Bad Request) if the news is not valid,
     * or with status 500 (Internal Server Error) if the news couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/news")
    @Timed
    public ResponseEntity<News> updateNews(@Valid @RequestBody News news) throws URISyntaxException {
        log.debug("REST request to update News : {}", news);
        if (news.getId() == null) {
            return createNews(news);
        }
        News result = newsService.save(news);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, news.getId().toString()))
            .body(result);
    }

    /**
     * GET  /news : get all the news.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of news in body
     */
    @GetMapping("/news")
    @Timed
    public ResponseEntity<List<News>> getAllNews(Pageable pageable) {
        log.debug("REST request to get a page of News");
        Page<News> page = newsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/news");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /news/:id : get the "id" news.
     *
     * @param id the id of the news to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the news, or with status 404 (Not Found)
     */
    @GetMapping("/news/{id}")
    @Timed
    public ResponseEntity<News> getNews(@PathVariable Long id) {
        log.debug("REST request to get News : {}", id);
        News news = newsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(news));
    }

    /**
     * DELETE  /news/:id : delete the "id" news.
     *
     * @param id the id of the news to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/news/{id}")
    @Timed
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        log.debug("REST request to delete News : {}", id);
        newsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
