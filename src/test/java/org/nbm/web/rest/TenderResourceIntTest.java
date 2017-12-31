package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.Tender;
import org.nbm.repository.TenderRepository;
import org.nbm.service.TenderService;
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
 * Test class for the TenderResource REST controller.
 *
 * @see TenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class TenderResourceIntTest {

    private static final String DEFAULT_TENDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TENDER_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SUBMIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_SINGLE = false;
    private static final Boolean UPDATED_IS_SINGLE = true;

    private static final String DEFAULT_TENDER_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TENDER_FILE_PATH = "BBBBBBBBBB";

    private static final byte[] DEFAULT_TENDER_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TENDER_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_TENDER_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TENDER_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTenderMockMvc;

    private Tender tender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TenderResource tenderResource = new TenderResource(tenderService);
        this.restTenderMockMvc = MockMvcBuilders.standaloneSetup(tenderResource)
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
    public static Tender createEntity(EntityManager em) {
        Tender tender = new Tender()
            .tenderName(DEFAULT_TENDER_NAME)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .submitDate(DEFAULT_SUBMIT_DATE)
            .endDate(DEFAULT_END_DATE)
            .isSingle(DEFAULT_IS_SINGLE)
            .tenderFilePath(DEFAULT_TENDER_FILE_PATH)
            .tenderFile(DEFAULT_TENDER_FILE)
            .tenderFileContentType(DEFAULT_TENDER_FILE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        return tender;
    }

    @Before
    public void initTest() {
        tender = createEntity(em);
    }

    @Test
    @Transactional
    public void createTender() throws Exception {
        int databaseSizeBeforeCreate = tenderRepository.findAll().size();

        // Create the Tender
        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isCreated());

        // Validate the Tender in the database
        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeCreate + 1);
        Tender testTender = tenderList.get(tenderList.size() - 1);
        assertThat(testTender.getTenderName()).isEqualTo(DEFAULT_TENDER_NAME);
        assertThat(testTender.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testTender.getSubmitDate()).isEqualTo(DEFAULT_SUBMIT_DATE);
        assertThat(testTender.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTender.isIsSingle()).isEqualTo(DEFAULT_IS_SINGLE);
        assertThat(testTender.getTenderFilePath()).isEqualTo(DEFAULT_TENDER_FILE_PATH);
        assertThat(testTender.getTenderFile()).isEqualTo(DEFAULT_TENDER_FILE);
        assertThat(testTender.getTenderFileContentType()).isEqualTo(DEFAULT_TENDER_FILE_CONTENT_TYPE);
        assertThat(testTender.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTender.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createTenderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenderRepository.findAll().size();

        // Create the Tender with an existing ID
        tender.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        // Validate the Tender in the database
        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTenderNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setTenderName(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setPublishDate(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubmitDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setSubmitDate(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setEndDate(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTenderFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setTenderFilePath(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTenderFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setTenderFile(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setIsActive(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenderRepository.findAll().size();
        // set the field null
        tender.setIsDeleted(null);

        // Create the Tender, which fails.

        restTenderMockMvc.perform(post("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isBadRequest());

        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTenders() throws Exception {
        // Initialize the database
        tenderRepository.saveAndFlush(tender);

        // Get all the tenderList
        restTenderMockMvc.perform(get("/api/tenders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tender.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenderName").value(hasItem(DEFAULT_TENDER_NAME.toString())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(sameInstant(DEFAULT_PUBLISH_DATE))))
            .andExpect(jsonPath("$.[*].submitDate").value(hasItem(sameInstant(DEFAULT_SUBMIT_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].isSingle").value(hasItem(DEFAULT_IS_SINGLE.booleanValue())))
            .andExpect(jsonPath("$.[*].tenderFilePath").value(hasItem(DEFAULT_TENDER_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].tenderFileContentType").value(hasItem(DEFAULT_TENDER_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].tenderFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_TENDER_FILE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getTender() throws Exception {
        // Initialize the database
        tenderRepository.saveAndFlush(tender);

        // Get the tender
        restTenderMockMvc.perform(get("/api/tenders/{id}", tender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tender.getId().intValue()))
            .andExpect(jsonPath("$.tenderName").value(DEFAULT_TENDER_NAME.toString()))
            .andExpect(jsonPath("$.publishDate").value(sameInstant(DEFAULT_PUBLISH_DATE)))
            .andExpect(jsonPath("$.submitDate").value(sameInstant(DEFAULT_SUBMIT_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.isSingle").value(DEFAULT_IS_SINGLE.booleanValue()))
            .andExpect(jsonPath("$.tenderFilePath").value(DEFAULT_TENDER_FILE_PATH.toString()))
            .andExpect(jsonPath("$.tenderFileContentType").value(DEFAULT_TENDER_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.tenderFile").value(Base64Utils.encodeToString(DEFAULT_TENDER_FILE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTender() throws Exception {
        // Get the tender
        restTenderMockMvc.perform(get("/api/tenders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTender() throws Exception {
        // Initialize the database
        tenderService.save(tender);

        int databaseSizeBeforeUpdate = tenderRepository.findAll().size();

        // Update the tender
        Tender updatedTender = tenderRepository.findOne(tender.getId());
        // Disconnect from session so that the updates on updatedTender are not directly saved in db
        em.detach(updatedTender);
        updatedTender
            .tenderName(UPDATED_TENDER_NAME)
            .publishDate(UPDATED_PUBLISH_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .endDate(UPDATED_END_DATE)
            .isSingle(UPDATED_IS_SINGLE)
            .tenderFilePath(UPDATED_TENDER_FILE_PATH)
            .tenderFile(UPDATED_TENDER_FILE)
            .tenderFileContentType(UPDATED_TENDER_FILE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restTenderMockMvc.perform(put("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTender)))
            .andExpect(status().isOk());

        // Validate the Tender in the database
        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeUpdate);
        Tender testTender = tenderList.get(tenderList.size() - 1);
        assertThat(testTender.getTenderName()).isEqualTo(UPDATED_TENDER_NAME);
        assertThat(testTender.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testTender.getSubmitDate()).isEqualTo(UPDATED_SUBMIT_DATE);
        assertThat(testTender.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTender.isIsSingle()).isEqualTo(UPDATED_IS_SINGLE);
        assertThat(testTender.getTenderFilePath()).isEqualTo(UPDATED_TENDER_FILE_PATH);
        assertThat(testTender.getTenderFile()).isEqualTo(UPDATED_TENDER_FILE);
        assertThat(testTender.getTenderFileContentType()).isEqualTo(UPDATED_TENDER_FILE_CONTENT_TYPE);
        assertThat(testTender.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTender.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingTender() throws Exception {
        int databaseSizeBeforeUpdate = tenderRepository.findAll().size();

        // Create the Tender

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTenderMockMvc.perform(put("/api/tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tender)))
            .andExpect(status().isCreated());

        // Validate the Tender in the database
        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTender() throws Exception {
        // Initialize the database
        tenderService.save(tender);

        int databaseSizeBeforeDelete = tenderRepository.findAll().size();

        // Get the tender
        restTenderMockMvc.perform(delete("/api/tenders/{id}", tender.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tender> tenderList = tenderRepository.findAll();
        assertThat(tenderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tender.class);
        Tender tender1 = new Tender();
        tender1.setId(1L);
        Tender tender2 = new Tender();
        tender2.setId(tender1.getId());
        assertThat(tender1).isEqualTo(tender2);
        tender2.setId(2L);
        assertThat(tender1).isNotEqualTo(tender2);
        tender1.setId(null);
        assertThat(tender1).isNotEqualTo(tender2);
    }
}
