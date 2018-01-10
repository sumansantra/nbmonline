package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.Galary;
import org.nbm.repository.GalaryRepository;
import org.nbm.service.GalaryService;
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
 * Test class for the GalaryResource REST controller.
 *
 * @see GalaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class GalaryResourceIntTest {

    private static final String DEFAULT_IMAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private GalaryRepository galaryRepository;

    @Autowired
    private GalaryService galaryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGalaryMockMvc;

    private Galary galary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GalaryResource galaryResource = new GalaryResource(galaryService);
        this.restGalaryMockMvc = MockMvcBuilders.standaloneSetup(galaryResource)
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
    public static Galary createEntity(EntityManager em) {
        Galary galary = new Galary()
            .imageName(DEFAULT_IMAGE_NAME)
            .imageFile(DEFAULT_IMAGE_FILE)
            .imageFileContentType(DEFAULT_IMAGE_FILE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        return galary;
    }

    @Before
    public void initTest() {
        galary = createEntity(em);
    }

    @Test
    @Transactional
    public void createGalary() throws Exception {
        int databaseSizeBeforeCreate = galaryRepository.findAll().size();

        // Create the Galary
        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isCreated());

        // Validate the Galary in the database
        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeCreate + 1);
        Galary testGalary = galaryList.get(galaryList.size() - 1);
        assertThat(testGalary.getImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
        assertThat(testGalary.getImageFile()).isEqualTo(DEFAULT_IMAGE_FILE);
        assertThat(testGalary.getImageFileContentType()).isEqualTo(DEFAULT_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testGalary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGalary.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testGalary.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createGalaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = galaryRepository.findAll().size();

        // Create the Galary with an existing ID
        galary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isBadRequest());

        // Validate the Galary in the database
        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkImageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = galaryRepository.findAll().size();
        // set the field null
        galary.setImageName(null);

        // Create the Galary, which fails.

        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isBadRequest());

        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = galaryRepository.findAll().size();
        // set the field null
        galary.setImageFile(null);

        // Create the Galary, which fails.

        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isBadRequest());

        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = galaryRepository.findAll().size();
        // set the field null
        galary.setIsActive(null);

        // Create the Galary, which fails.

        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isBadRequest());

        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = galaryRepository.findAll().size();
        // set the field null
        galary.setIsDeleted(null);

        // Create the Galary, which fails.

        restGalaryMockMvc.perform(post("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isBadRequest());

        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGalaries() throws Exception {
        // Initialize the database
        galaryRepository.saveAndFlush(galary);

        // Get all the galaryList
        restGalaryMockMvc.perform(get("/api/galaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(galary.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageFileContentType").value(hasItem(DEFAULT_IMAGE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getGalary() throws Exception {
        // Initialize the database
        galaryRepository.saveAndFlush(galary);

        // Get the galary
        restGalaryMockMvc.perform(get("/api/galaries/{id}", galary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(galary.getId().intValue()))
            .andExpect(jsonPath("$.imageName").value(DEFAULT_IMAGE_NAME.toString()))
            .andExpect(jsonPath("$.imageFileContentType").value(DEFAULT_IMAGE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageFile").value(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGalary() throws Exception {
        // Get the galary
        restGalaryMockMvc.perform(get("/api/galaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGalary() throws Exception {
        // Initialize the database
        galaryService.save(galary);

        int databaseSizeBeforeUpdate = galaryRepository.findAll().size();

        // Update the galary
        Galary updatedGalary = galaryRepository.findOne(galary.getId());
        // Disconnect from session so that the updates on updatedGalary are not directly saved in db
        em.detach(updatedGalary);
        updatedGalary
            .imageName(UPDATED_IMAGE_NAME)
            .imageFile(UPDATED_IMAGE_FILE)
            .imageFileContentType(UPDATED_IMAGE_FILE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restGalaryMockMvc.perform(put("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGalary)))
            .andExpect(status().isOk());

        // Validate the Galary in the database
        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeUpdate);
        Galary testGalary = galaryList.get(galaryList.size() - 1);
        assertThat(testGalary.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
        assertThat(testGalary.getImageFile()).isEqualTo(UPDATED_IMAGE_FILE);
        assertThat(testGalary.getImageFileContentType()).isEqualTo(UPDATED_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testGalary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGalary.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testGalary.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingGalary() throws Exception {
        int databaseSizeBeforeUpdate = galaryRepository.findAll().size();

        // Create the Galary

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGalaryMockMvc.perform(put("/api/galaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galary)))
            .andExpect(status().isCreated());

        // Validate the Galary in the database
        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGalary() throws Exception {
        // Initialize the database
        galaryService.save(galary);

        int databaseSizeBeforeDelete = galaryRepository.findAll().size();

        // Get the galary
        restGalaryMockMvc.perform(delete("/api/galaries/{id}", galary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Galary> galaryList = galaryRepository.findAll();
        assertThat(galaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Galary.class);
        Galary galary1 = new Galary();
        galary1.setId(1L);
        Galary galary2 = new Galary();
        galary2.setId(galary1.getId());
        assertThat(galary1).isEqualTo(galary2);
        galary2.setId(2L);
        assertThat(galary1).isNotEqualTo(galary2);
        galary1.setId(null);
        assertThat(galary1).isNotEqualTo(galary2);
    }
}
