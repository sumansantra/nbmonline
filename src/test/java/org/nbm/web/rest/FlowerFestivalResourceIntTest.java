package org.nbm.web.rest;

import org.nbm.NbmonlineApp;

import org.nbm.domain.FlowerFestival;
import org.nbm.repository.FlowerFestivalRepository;
import org.nbm.service.FlowerFestivalService;
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
 * Test class for the FlowerFestivalResource REST controller.
 *
 * @see FlowerFestivalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NbmonlineApp.class)
public class FlowerFestivalResourceIntTest {

    private static final String DEFAULT_FESTIVAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FESTIVAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Autowired
    private FlowerFestivalRepository flowerFestivalRepository;

    @Autowired
    private FlowerFestivalService flowerFestivalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFlowerFestivalMockMvc;

    private FlowerFestival flowerFestival;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FlowerFestivalResource flowerFestivalResource = new FlowerFestivalResource(flowerFestivalService);
        this.restFlowerFestivalMockMvc = MockMvcBuilders.standaloneSetup(flowerFestivalResource)
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
    public static FlowerFestival createEntity(EntityManager em) {
        FlowerFestival flowerFestival = new FlowerFestival()
            .festivalName(DEFAULT_FESTIVAL_NAME)
            .year(DEFAULT_YEAR)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeleted(DEFAULT_IS_DELETED);
        return flowerFestival;
    }

    @Before
    public void initTest() {
        flowerFestival = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlowerFestival() throws Exception {
        int databaseSizeBeforeCreate = flowerFestivalRepository.findAll().size();

        // Create the FlowerFestival
        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isCreated());

        // Validate the FlowerFestival in the database
        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeCreate + 1);
        FlowerFestival testFlowerFestival = flowerFestivalList.get(flowerFestivalList.size() - 1);
        assertThat(testFlowerFestival.getFestivalName()).isEqualTo(DEFAULT_FESTIVAL_NAME);
        assertThat(testFlowerFestival.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testFlowerFestival.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlowerFestival.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testFlowerFestival.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createFlowerFestivalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flowerFestivalRepository.findAll().size();

        // Create the FlowerFestival with an existing ID
        flowerFestival.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isBadRequest());

        // Validate the FlowerFestival in the database
        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFestivalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalRepository.findAll().size();
        // set the field null
        flowerFestival.setFestivalName(null);

        // Create the FlowerFestival, which fails.

        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isBadRequest());

        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalRepository.findAll().size();
        // set the field null
        flowerFestival.setYear(null);

        // Create the FlowerFestival, which fails.

        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isBadRequest());

        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalRepository.findAll().size();
        // set the field null
        flowerFestival.setIsActive(null);

        // Create the FlowerFestival, which fails.

        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isBadRequest());

        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerFestivalRepository.findAll().size();
        // set the field null
        flowerFestival.setIsDeleted(null);

        // Create the FlowerFestival, which fails.

        restFlowerFestivalMockMvc.perform(post("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isBadRequest());

        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlowerFestivals() throws Exception {
        // Initialize the database
        flowerFestivalRepository.saveAndFlush(flowerFestival);

        // Get all the flowerFestivalList
        restFlowerFestivalMockMvc.perform(get("/api/flower-festivals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowerFestival.getId().intValue())))
            .andExpect(jsonPath("$.[*].festivalName").value(hasItem(DEFAULT_FESTIVAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFlowerFestival() throws Exception {
        // Initialize the database
        flowerFestivalRepository.saveAndFlush(flowerFestival);

        // Get the flowerFestival
        restFlowerFestivalMockMvc.perform(get("/api/flower-festivals/{id}", flowerFestival.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flowerFestival.getId().intValue()))
            .andExpect(jsonPath("$.festivalName").value(DEFAULT_FESTIVAL_NAME.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFlowerFestival() throws Exception {
        // Get the flowerFestival
        restFlowerFestivalMockMvc.perform(get("/api/flower-festivals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlowerFestival() throws Exception {
        // Initialize the database
        flowerFestivalService.save(flowerFestival);

        int databaseSizeBeforeUpdate = flowerFestivalRepository.findAll().size();

        // Update the flowerFestival
        FlowerFestival updatedFlowerFestival = flowerFestivalRepository.findOne(flowerFestival.getId());
        // Disconnect from session so that the updates on updatedFlowerFestival are not directly saved in db
        em.detach(updatedFlowerFestival);
        updatedFlowerFestival
            .festivalName(UPDATED_FESTIVAL_NAME)
            .year(UPDATED_YEAR)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeleted(UPDATED_IS_DELETED);

        restFlowerFestivalMockMvc.perform(put("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlowerFestival)))
            .andExpect(status().isOk());

        // Validate the FlowerFestival in the database
        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeUpdate);
        FlowerFestival testFlowerFestival = flowerFestivalList.get(flowerFestivalList.size() - 1);
        assertThat(testFlowerFestival.getFestivalName()).isEqualTo(UPDATED_FESTIVAL_NAME);
        assertThat(testFlowerFestival.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testFlowerFestival.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlowerFestival.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testFlowerFestival.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingFlowerFestival() throws Exception {
        int databaseSizeBeforeUpdate = flowerFestivalRepository.findAll().size();

        // Create the FlowerFestival

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFlowerFestivalMockMvc.perform(put("/api/flower-festivals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flowerFestival)))
            .andExpect(status().isCreated());

        // Validate the FlowerFestival in the database
        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFlowerFestival() throws Exception {
        // Initialize the database
        flowerFestivalService.save(flowerFestival);

        int databaseSizeBeforeDelete = flowerFestivalRepository.findAll().size();

        // Get the flowerFestival
        restFlowerFestivalMockMvc.perform(delete("/api/flower-festivals/{id}", flowerFestival.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FlowerFestival> flowerFestivalList = flowerFestivalRepository.findAll();
        assertThat(flowerFestivalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowerFestival.class);
        FlowerFestival flowerFestival1 = new FlowerFestival();
        flowerFestival1.setId(1L);
        FlowerFestival flowerFestival2 = new FlowerFestival();
        flowerFestival2.setId(flowerFestival1.getId());
        assertThat(flowerFestival1).isEqualTo(flowerFestival2);
        flowerFestival2.setId(2L);
        assertThat(flowerFestival1).isNotEqualTo(flowerFestival2);
        flowerFestival1.setId(null);
        assertThat(flowerFestival1).isNotEqualTo(flowerFestival2);
    }
}
