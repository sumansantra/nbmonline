package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.Notice;
import org.nbm.repository.NoticeRepository;
import org.nbm.service.NoticeService;
import org.nbm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.nbm.web.rest.TestUtil.sameInstant;
import static org.nbm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NoticeResource REST controller.
 *
 * @see NoticeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class NoticeResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNoticeMockMvc;

    private Notice notice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NoticeResource noticeResource = new NoticeResource(noticeService);
        this.restNoticeMockMvc = MockMvcBuilders.standaloneSetup(noticeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notice createEntity(EntityManager em) {
        Notice notice = new Notice()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        return notice;
    }

    @Before
    public void initTest() {
        notice = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotice() throws Exception {
        int databaseSizeBeforeCreate = noticeRepository.findAll().size();

        // Create the Notice
        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isCreated());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeCreate + 1);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNotice.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testNotice.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testNotice.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createNoticeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noticeRepository.findAll().size();

        // Create the Notice with an existing ID
        notice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setTitle(null);

        // Create the Notice, which fails.

        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setDescription(null);

        // Create the Notice, which fails.

        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setPublishDate(null);

        // Create the Notice, which fails.

        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setIsActive(null);

        // Create the Notice, which fails.

        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setIsDeleted(null);

        // Create the Notice, which fails.

        restNoticeMockMvc.perform(post("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotices() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get all the noticeList
        restNoticeMockMvc.perform(get("/api/notices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(sameInstant(DEFAULT_PUBLISH_DATE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get the notice
        restNoticeMockMvc.perform(get("/api/notices/{id}", notice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notice.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.publishDate").value(sameInstant(DEFAULT_PUBLISH_DATE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotice() throws Exception {
        // Get the notice
        restNoticeMockMvc.perform(get("/api/notices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotice() throws Exception {
        // Initialize the database
        noticeService.save(notice);

        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Update the notice
        Notice updatedNotice = noticeRepository.findOne(notice.getId());
        // Disconnect from session so that the updates on updatedNotice are not directly saved in db
        em.detach(updatedNotice);
        updatedNotice
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .publishDate(UPDATED_PUBLISH_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restNoticeMockMvc.perform(put("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotice)))
            .andExpect(status().isOk());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNotice.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testNotice.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testNotice.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Create the Notice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNoticeMockMvc.perform(put("/api/notices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notice)))
            .andExpect(status().isCreated());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotice() throws Exception {
        // Initialize the database
        noticeService.save(notice);

        int databaseSizeBeforeDelete = noticeRepository.findAll().size();

        // Get the notice
        restNoticeMockMvc.perform(delete("/api/notices/{id}", notice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notice.class);
        Notice notice1 = new Notice();
        notice1.setId(1L);
        Notice notice2 = new Notice();
        notice2.setId(notice1.getId());
        assertThat(notice1).isEqualTo(notice2);
        notice2.setId(2L);
        assertThat(notice1).isNotEqualTo(notice2);
        notice1.setId(null);
        assertThat(notice1).isNotEqualTo(notice2);
    }
}
