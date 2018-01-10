package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.LatestActivity;
import org.nbm.repository.LatestActivityRepository;
import org.nbm.service.LatestActivityService;
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
import java.util.List;

import static org.nbm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LatestActivityResource REST controller.
 *
 * @see LatestActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class LatestActivityResourceIntTest {

    private static final String DEFAULT_ACTIVITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private LatestActivityRepository latestActivityRepository;

    @Autowired
    private LatestActivityService latestActivityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLatestActivityMockMvc;

    private LatestActivity latestActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LatestActivityResource latestActivityResource = new LatestActivityResource(latestActivityService);
        this.restLatestActivityMockMvc = MockMvcBuilders.standaloneSetup(latestActivityResource)
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
    public static LatestActivity createEntity(EntityManager em) {
        LatestActivity latestActivity = new LatestActivity()
            .activityName(DEFAULT_ACTIVITY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        return latestActivity;
    }

    @Before
    public void initTest() {
        latestActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createLatestActivity() throws Exception {
        int databaseSizeBeforeCreate = latestActivityRepository.findAll().size();

        // Create the LatestActivity
        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isCreated());

        // Validate the LatestActivity in the database
        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeCreate + 1);
        LatestActivity testLatestActivity = latestActivityList.get(latestActivityList.size() - 1);
        assertThat(testLatestActivity.getActivityName()).isEqualTo(DEFAULT_ACTIVITY_NAME);
        assertThat(testLatestActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLatestActivity.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLatestActivity.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testLatestActivity.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testLatestActivity.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createLatestActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = latestActivityRepository.findAll().size();

        // Create the LatestActivity with an existing ID
        latestActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isBadRequest());

        // Validate the LatestActivity in the database
        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = latestActivityRepository.findAll().size();
        // set the field null
        latestActivity.setActivityName(null);

        // Create the LatestActivity, which fails.

        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isBadRequest());

        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = latestActivityRepository.findAll().size();
        // set the field null
        latestActivity.setImage(null);

        // Create the LatestActivity, which fails.

        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isBadRequest());

        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = latestActivityRepository.findAll().size();
        // set the field null
        latestActivity.setIsActive(null);

        // Create the LatestActivity, which fails.

        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isBadRequest());

        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = latestActivityRepository.findAll().size();
        // set the field null
        latestActivity.setIsDeleted(null);

        // Create the LatestActivity, which fails.

        restLatestActivityMockMvc.perform(post("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isBadRequest());

        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLatestActivities() throws Exception {
        // Initialize the database
        latestActivityRepository.saveAndFlush(latestActivity);

        // Get all the latestActivityList
        restLatestActivityMockMvc.perform(get("/api/latest-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(latestActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityName").value(hasItem(DEFAULT_ACTIVITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getLatestActivity() throws Exception {
        // Initialize the database
        latestActivityRepository.saveAndFlush(latestActivity);

        // Get the latestActivity
        restLatestActivityMockMvc.perform(get("/api/latest-activities/{id}", latestActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(latestActivity.getId().intValue()))
            .andExpect(jsonPath("$.activityName").value(DEFAULT_ACTIVITY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLatestActivity() throws Exception {
        // Get the latestActivity
        restLatestActivityMockMvc.perform(get("/api/latest-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLatestActivity() throws Exception {
        // Initialize the database
        latestActivityService.save(latestActivity);

        int databaseSizeBeforeUpdate = latestActivityRepository.findAll().size();

        // Update the latestActivity
        LatestActivity updatedLatestActivity = latestActivityRepository.findOne(latestActivity.getId());
        // Disconnect from session so that the updates on updatedLatestActivity are not directly saved in db
        em.detach(updatedLatestActivity);
        updatedLatestActivity
            .activityName(UPDATED_ACTIVITY_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restLatestActivityMockMvc.perform(put("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLatestActivity)))
            .andExpect(status().isOk());

        // Validate the LatestActivity in the database
        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeUpdate);
        LatestActivity testLatestActivity = latestActivityList.get(latestActivityList.size() - 1);
        assertThat(testLatestActivity.getActivityName()).isEqualTo(UPDATED_ACTIVITY_NAME);
        assertThat(testLatestActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLatestActivity.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLatestActivity.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testLatestActivity.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLatestActivity.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingLatestActivity() throws Exception {
        int databaseSizeBeforeUpdate = latestActivityRepository.findAll().size();

        // Create the LatestActivity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLatestActivityMockMvc.perform(put("/api/latest-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(latestActivity)))
            .andExpect(status().isCreated());

        // Validate the LatestActivity in the database
        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLatestActivity() throws Exception {
        // Initialize the database
        latestActivityService.save(latestActivity);

        int databaseSizeBeforeDelete = latestActivityRepository.findAll().size();

        // Get the latestActivity
        restLatestActivityMockMvc.perform(delete("/api/latest-activities/{id}", latestActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LatestActivity> latestActivityList = latestActivityRepository.findAll();
        assertThat(latestActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LatestActivity.class);
        LatestActivity latestActivity1 = new LatestActivity();
        latestActivity1.setId(1L);
        LatestActivity latestActivity2 = new LatestActivity();
        latestActivity2.setId(latestActivity1.getId());
        assertThat(latestActivity1).isEqualTo(latestActivity2);
        latestActivity2.setId(2L);
        assertThat(latestActivity1).isNotEqualTo(latestActivity2);
        latestActivity1.setId(null);
        assertThat(latestActivity1).isNotEqualTo(latestActivity2);
    }
}
