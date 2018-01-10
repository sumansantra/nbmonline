package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.FlowerFestivalImage;
import org.nbm.domain.FlowerFestival;
import org.nbm.repository.FlowerFestivalImageRepository;
import org.nbm.service.FlowerFestivalImageService;
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
 * Test class for the FlowerFestivalImageResource REST controller.
 *
 * @see FlowerFestivalImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class FlowerFestivalImageResourceIntTest {

    private static final String DEFAULT_IMAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private FlowerFestivalImageRepository flowerFestivalImageRepository;

    @Autowired
    private FlowerFestivalImageService flowerFestivalImageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFlowerFestivalImageMockMvc;

    private FlowerFestivalImage flowerFestivalImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FlowerFestivalImageResource flowerFestivalImageResource = new FlowerFestivalImageResource(flowerFestivalImageService);
        this.restFlowerFestivalImageMockMvc = MockMvcBuilders.standaloneSetup(flowerFestivalImageResource)
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
    public static FlowerFestivalImage createEntity(EntityManager em) {
        FlowerFestivalImage flowerFestivalImage = new FlowerFestivalImage()
            .imageName(DEFAULT_IMAGE_NAME)
            .imageFile(DEFAULT_IMAGE_FILE)
            .imageFileContentType(DEFAULT_IMAGE_FILE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        FlowerFestival flowerFestival = FlowerFestivalResourceIntTest.createEntity(em);
        em.persist(flowerFestival);
        em.flush();
        flowerFestivalImage.setFlowerFestival(flowerFestival);
        return flowerFestivalImage;
    }

    @Before
    public void initTest() {
        flowerFestivalImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlowerFestivalImage() throws Exception {
        int databaseSizeBeforeCreate = flowerFestivalImageRepository.findAll().size();

        // Create the FlowerFestivalImage
        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isCreated());

        // Validate the FlowerFestivalImage in the database
        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeCreate + 1);
        FlowerFestivalImage testFlowerFestivalImage = flowerFestivalImageList.get(flowerFestivalImageList.size() - 1);
        assertThat(testFlowerFestivalImage.getImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
        assertThat(testFlowerFestivalImage.getImageFile()).isEqualTo(DEFAULT_IMAGE_FILE);
        assertThat(testFlowerFestivalImage.getImageFileContentType()).isEqualTo(DEFAULT_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testFlowerFestivalImage.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testFlowerFestivalImage.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createFlowerFestivalImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flowerFestivalImageRepository.findAll().size();

        // Create the FlowerFestivalImage with an existing ID
        flowerFestivalImage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isBadRequest());

        // Validate the FlowerFestivalImage in the database
        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkImageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalImageRepository.findAll().size();
        // set the field null
        flowerFestivalImage.setImageName(null);

        // Create the FlowerFestivalImage, which fails.

        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isBadRequest());

        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalImageRepository.findAll().size();
        // set the field null
        flowerFestivalImage.setImageFile(null);

        // Create the FlowerFestivalImage, which fails.

        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isBadRequest());

        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalImageRepository.findAll().size();
        // set the field null
        flowerFestivalImage.setIsActive(null);

        // Create the FlowerFestivalImage, which fails.

        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isBadRequest());

        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalImageRepository.findAll().size();
        // set the field null
        flowerFestivalImage.setIsDeleted(null);

        // Create the FlowerFestivalImage, which fails.

        restFlowerFestivalImageMockMvc.perform(post("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isBadRequest());

        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlowerFestivalImages() throws Exception {
        // Initialize the database
        flowerFestivalImageRepository.saveAndFlush(flowerFestivalImage);

        // Get all the flowerFestivalImageList
        restFlowerFestivalImageMockMvc.perform(get("/api/flower-festival-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowerFestivalImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageName").value(hasItem(DEFAULT_IMAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageFileContentType").value(hasItem(DEFAULT_IMAGE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFlowerFestivalImage() throws Exception {
        // Initialize the database
        flowerFestivalImageRepository.saveAndFlush(flowerFestivalImage);

        // Get the flowerFestivalImage
        restFlowerFestivalImageMockMvc.perform(get("/api/flower-festival-images/{id}", flowerFestivalImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flowerFestivalImage.getId().intValue()))
            .andExpect(jsonPath("$.imageName").value(DEFAULT_IMAGE_NAME.toString()))
            .andExpect(jsonPath("$.imageFileContentType").value(DEFAULT_IMAGE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageFile").value(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFlowerFestivalImage() throws Exception {
        // Get the flowerFestivalImage
        restFlowerFestivalImageMockMvc.perform(get("/api/flower-festival-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlowerFestivalImage() throws Exception {
        // Initialize the database
        flowerFestivalImageService.save(flowerFestivalImage);

        int databaseSizeBeforeUpdate = flowerFestivalImageRepository.findAll().size();

        // Update the flowerFestivalImage
        FlowerFestivalImage updatedFlowerFestivalImage = flowerFestivalImageRepository.findOne(flowerFestivalImage.getId());
        // Disconnect from session so that the updates on updatedFlowerFestivalImage are not directly saved in db
        em.detach(updatedFlowerFestivalImage);
        updatedFlowerFestivalImage
            .imageName(UPDATED_IMAGE_NAME)
            .imageFile(UPDATED_IMAGE_FILE)
            .imageFileContentType(UPDATED_IMAGE_FILE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restFlowerFestivalImageMockMvc.perform(put("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlowerFestivalImage)))
            .andExpect(status().isOk());

        // Validate the FlowerFestivalImage in the database
        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeUpdate);
        FlowerFestivalImage testFlowerFestivalImage = flowerFestivalImageList.get(flowerFestivalImageList.size() - 1);
        assertThat(testFlowerFestivalImage.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
        assertThat(testFlowerFestivalImage.getImageFile()).isEqualTo(UPDATED_IMAGE_FILE);
        assertThat(testFlowerFestivalImage.getImageFileContentType()).isEqualTo(UPDATED_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testFlowerFestivalImage.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testFlowerFestivalImage.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingFlowerFestivalImage() throws Exception {
        int databaseSizeBeforeUpdate = flowerFestivalImageRepository.findAll().size();

        // Create the FlowerFestivalImage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFlowerFestivalImageMockMvc.perform(put("/api/flower-festival-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestivalImage)))
            .andExpect(status().isCreated());

        // Validate the FlowerFestivalImage in the database
        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFlowerFestivalImage() throws Exception {
        // Initialize the database
        flowerFestivalImageService.save(flowerFestivalImage);

        int databaseSizeBeforeDelete = flowerFestivalImageRepository.findAll().size();

        // Get the flowerFestivalImage
        restFlowerFestivalImageMockMvc.perform(delete("/api/flower-festival-images/{id}", flowerFestivalImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FlowerFestivalImage> flowerFestivalImageList = flowerFestivalImageRepository.findAll();
        assertThat(flowerFestivalImageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowerFestivalImage.class);
        FlowerFestivalImage flowerFestivalImage1 = new FlowerFestivalImage();
        flowerFestivalImage1.setId(1L);
        FlowerFestivalImage flowerFestivalImage2 = new FlowerFestivalImage();
        flowerFestivalImage2.setId(flowerFestivalImage1.getId());
        assertThat(flowerFestivalImage1).isEqualTo(flowerFestivalImage2);
        flowerFestivalImage2.setId(2L);
        assertThat(flowerFestivalImage1).isNotEqualTo(flowerFestivalImage2);
        flowerFestivalImage1.setId(null);
        assertThat(flowerFestivalImage1).isNotEqualTo(flowerFestivalImage2);
    }
}
