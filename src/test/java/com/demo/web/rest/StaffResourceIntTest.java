package com.demo.web.rest;

import com.demo.EntityDemoApp;

import com.demo.domain.Staff;
import com.demo.domain.Department;
import com.demo.repository.StaffRepository;
import com.demo.service.StaffService;
import com.demo.service.dto.StaffDTO;
import com.demo.service.mapper.StaffMapper;
import com.demo.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.demo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StaffResource REST controller.
 *
 * @see StaffResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntityDemoApp.class)
public class StaffResourceIntTest {

    private static final String DEFAULT_STAFF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private StaffService staffService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStaffMockMvc;

    private Staff staff;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StaffResource staffResource = new StaffResource(staffService);
        this.restStaffMockMvc = MockMvcBuilders.standaloneSetup(staffResource)
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
    public static Staff createEntity(EntityManager em) {
        Staff staff = new Staff()
            .staffName(DEFAULT_STAFF_NAME)
            .password(DEFAULT_PASSWORD);
        // Add required entity
        Department department = DepartmentResourceIntTest.createEntity(em);
        em.persist(department);
        em.flush();
        staff.setDepartment(department);
        return staff;
    }

    @Before
    public void initTest() {
        staff = createEntity(em);
    }

    @Test
    @Transactional
    public void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);
        restStaffMockMvc.perform(post("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate + 1);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getStaffName()).isEqualTo(DEFAULT_STAFF_NAME);
        assertThat(testStaff.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createStaffWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // Create the Staff with an existing ID
        staff.setId(1L);
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffMockMvc.perform(post("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStaffNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setStaffName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc.perform(post("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setPassword(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc.perform(post("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList
        restStaffMockMvc.perform(get("/api/staff?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffName").value(hasItem(DEFAULT_STAFF_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", staff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(staff.getId().intValue()))
            .andExpect(jsonPath("$.staffName").value(DEFAULT_STAFF_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff
        Staff updatedStaff = staffRepository.findOne(staff.getId());
        updatedStaff
            .staffName(UPDATED_STAFF_NAME)
            .password(UPDATED_PASSWORD);
        StaffDTO staffDTO = staffMapper.toDto(updatedStaff);

        restStaffMockMvc.perform(put("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getStaffName()).isEqualTo(UPDATED_STAFF_NAME);
        assertThat(testStaff.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStaffMockMvc.perform(put("/api/staff")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
            .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Get the staff
        restStaffMockMvc.perform(delete("/api/staff/{id}", staff.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Staff.class);
        Staff staff1 = new Staff();
        staff1.setId(1L);
        Staff staff2 = new Staff();
        staff2.setId(staff1.getId());
        assertThat(staff1).isEqualTo(staff2);
        staff2.setId(2L);
        assertThat(staff1).isNotEqualTo(staff2);
        staff1.setId(null);
        assertThat(staff1).isNotEqualTo(staff2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffDTO.class);
        StaffDTO staffDTO1 = new StaffDTO();
        staffDTO1.setId(1L);
        StaffDTO staffDTO2 = new StaffDTO();
        assertThat(staffDTO1).isNotEqualTo(staffDTO2);
        staffDTO2.setId(staffDTO1.getId());
        assertThat(staffDTO1).isEqualTo(staffDTO2);
        staffDTO2.setId(2L);
        assertThat(staffDTO1).isNotEqualTo(staffDTO2);
        staffDTO1.setId(null);
        assertThat(staffDTO1).isNotEqualTo(staffDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(staffMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(staffMapper.fromId(null)).isNull();
    }
}
