package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.SubTender;
import org.nbm.domain.Tender;
import org.nbm.repository.SubTenderRepository;
import org.nbm.service.SubTenderService;
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
 * Test class for the SubTenderResource REST controller.
 *
 * @see SubTenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class SubTenderResourceIntTest {

    private static final String DEFAULT_SUB_TENDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TENDER_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SUBMIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SUB_TENDER_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TENDER_FILE_PATH = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SUB_TENDER_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SUB_TENDER_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SUB_TENDER_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SUB_TENDER_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private SubTenderRepository subTenderRepository;

    @Autowired
    private SubTenderService subTenderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubTenderMockMvc;

    private SubTender subTender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubTenderResource subTenderResource = new SubTenderResource(subTenderService);
        this.restSubTenderMockMvc = MockMvcBuilders.standaloneSetup(subTenderResource)
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
    public static SubTender createEntity(EntityManager em) {
        SubTender subTender = new SubTender()
            .subTenderName(DEFAULT_SUB_TENDER_NAME)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .submitDate(DEFAULT_SUBMIT_DATE)
            .endDate(DEFAULT_END_DATE)
            .subTenderFilePath(DEFAULT_SUB_TENDER_FILE_PATH)
            .subTenderFile(DEFAULT_SUB_TENDER_FILE)
            .subTenderFileContentType(DEFAULT_SUB_TENDER_FILE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        Tender tender = TenderResourceIntTest.createEntity(em);
        em.persist(tender);
        em.flush();
        subTender.setTender(tender);
        return subTender;
    }

    @Before
    public void initTest() {
        subTender = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubTender() throws Exception {
        int databaseSizeBeforeCreate = subTenderRepository.findAll().size();

        // Create the SubTender
        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isCreated());

        // Validate the SubTender in the database
        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeCreate + 1);
        SubTender testSubTender = subTenderList.get(subTenderList.size() - 1);
        assertThat(testSubTender.getSubTenderName()).isEqualTo(DEFAULT_SUB_TENDER_NAME);
        assertThat(testSubTender.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testSubTender.getSubmitDate()).isEqualTo(DEFAULT_SUBMIT_DATE);
        assertThat(testSubTender.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSubTender.getSubTenderFilePath()).isEqualTo(DEFAULT_SUB_TENDER_FILE_PATH);
        assertThat(testSubTender.getSubTenderFile()).isEqualTo(DEFAULT_SUB_TENDER_FILE);
        assertThat(testSubTender.getSubTenderFileContentType()).isEqualTo(DEFAULT_SUB_TENDER_FILE_CONTENT_TYPE);
        assertThat(testSubTender.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSubTender.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createSubTenderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subTenderRepository.findAll().size();

        // Create the SubTender with an existing ID
        subTender.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        // Validate the SubTender in the database
        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSubTenderNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setSubTenderName(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setPublishDate(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubmitDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setSubmitDate(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setEndDate(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubTenderFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setSubTenderFile(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setIsActive(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTenderRepository.findAll().size();
        // set the field null
        subTender.setIsDeleted(null);

        // Create the SubTender, which fails.

        restSubTenderMockMvc.perform(post("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isBadRequest());

        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubTenders() throws Exception {
        // Initialize the database
        subTenderRepository.saveAndFlush(subTender);

        // Get all the subTenderList
        restSubTenderMockMvc.perform(get("/api/sub-tenders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTender.getId().intValue())))
            .andExpect(jsonPath("$.[*].subTenderName").value(hasItem(DEFAULT_SUB_TENDER_NAME.toString())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(sameInstant(DEFAULT_PUBLISH_DATE))))
            .andExpect(jsonPath("$.[*].submitDate").value(hasItem(sameInstant(DEFAULT_SUBMIT_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].subTenderFilePath").value(hasItem(DEFAULT_SUB_TENDER_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].subTenderFileContentType").value(hasItem(DEFAULT_SUB_TENDER_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].subTenderFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SUB_TENDER_FILE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSubTender() throws Exception {
        // Initialize the database
        subTenderRepository.saveAndFlush(subTender);

        // Get the subTender
        restSubTenderMockMvc.perform(get("/api/sub-tenders/{id}", subTender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subTender.getId().intValue()))
            .andExpect(jsonPath("$.subTenderName").value(DEFAULT_SUB_TENDER_NAME.toString()))
            .andExpect(jsonPath("$.publishDate").value(sameInstant(DEFAULT_PUBLISH_DATE)))
            .andExpect(jsonPath("$.submitDate").value(sameInstant(DEFAULT_SUBMIT_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.subTenderFilePath").value(DEFAULT_SUB_TENDER_FILE_PATH.toString()))
            .andExpect(jsonPath("$.subTenderFileContentType").value(DEFAULT_SUB_TENDER_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.subTenderFile").value(Base64Utils.encodeToString(DEFAULT_SUB_TENDER_FILE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubTender() throws Exception {
        // Get the subTender
        restSubTenderMockMvc.perform(get("/api/sub-tenders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubTender() throws Exception {
        // Initialize the database
        subTenderService.save(subTender);

        int databaseSizeBeforeUpdate = subTenderRepository.findAll().size();

        // Update the subTender
        SubTender updatedSubTender = subTenderRepository.findOne(subTender.getId());
        // Disconnect from session so that the updates on updatedSubTender are not directly saved in db
        em.detach(updatedSubTender);
        updatedSubTender
            .subTenderName(UPDATED_SUB_TENDER_NAME)
            .publishDate(UPDATED_PUBLISH_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .endDate(UPDATED_END_DATE)
            .subTenderFilePath(UPDATED_SUB_TENDER_FILE_PATH)
            .subTenderFile(UPDATED_SUB_TENDER_FILE)
            .subTenderFileContentType(UPDATED_SUB_TENDER_FILE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restSubTenderMockMvc.perform(put("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubTender)))
            .andExpect(status().isOk());

        // Validate the SubTender in the database
        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeUpdate);
        SubTender testSubTender = subTenderList.get(subTenderList.size() - 1);
        assertThat(testSubTender.getSubTenderName()).isEqualTo(UPDATED_SUB_TENDER_NAME);
        assertThat(testSubTender.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testSubTender.getSubmitDate()).isEqualTo(UPDATED_SUBMIT_DATE);
        assertThat(testSubTender.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubTender.getSubTenderFilePath()).isEqualTo(UPDATED_SUB_TENDER_FILE_PATH);
        assertThat(testSubTender.getSubTenderFile()).isEqualTo(UPDATED_SUB_TENDER_FILE);
        assertThat(testSubTender.getSubTenderFileContentType()).isEqualTo(UPDATED_SUB_TENDER_FILE_CONTENT_TYPE);
        assertThat(testSubTender.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSubTender.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingSubTender() throws Exception {
        int databaseSizeBeforeUpdate = subTenderRepository.findAll().size();

        // Create the SubTender

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubTenderMockMvc.perform(put("/api/sub-tenders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTender)))
            .andExpect(status().isCreated());

        // Validate the SubTender in the database
        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubTender() throws Exception {
        // Initialize the database
        subTenderService.save(subTender);

        int databaseSizeBeforeDelete = subTenderRepository.findAll().size();

        // Get the subTender
        restSubTenderMockMvc.perform(delete("/api/sub-tenders/{id}", subTender.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubTender> subTenderList = subTenderRepository.findAll();
        assertThat(subTenderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTender.class);
        SubTender subTender1 = new SubTender();
        subTender1.setId(1L);
        SubTender subTender2 = new SubTender();
        subTender2.setId(subTender1.getId());
        assertThat(subTender1).isEqualTo(subTender2);
        subTender2.setId(2L);
        assertThat(subTender1).isNotEqualTo(subTender2);
        subTender1.setId(null);
        assertThat(subTender1).isNotEqualTo(subTender2);
    }
}
