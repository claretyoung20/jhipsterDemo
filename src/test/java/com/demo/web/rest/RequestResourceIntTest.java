package com.demo.web.rest;

import com.demo.EntityDemoApp;

import com.demo.domain.Request;
import com.demo.domain.Department;
import com.demo.domain.Student;
import com.demo.repository.RequestRepository;
import com.demo.service.RequestService;
import com.demo.service.dto.RequestDTO;
import com.demo.service.mapper.RequestMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.demo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RequestResource REST controller.
 *
 * @see RequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntityDemoApp.class)
public class RequestResourceIntTest {

    private static final String DEFAULT_REQUEST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REQUEST_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUEST_STATUS = false;
    private static final Boolean UPDATED_REQUEST_STATUS = true;

    private static final String DEFAULT_REQUEST_SOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_SOLUTION = "BBBBBBBBBB";

    private static final String DEFAULT_ATTACHMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_NAME = "BBBBBBBBBB";

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRequestMockMvc;

    private Request request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequestResource requestResource = new RequestResource(requestService);
        this.restRequestMockMvc = MockMvcBuilders.standaloneSetup(requestResource)
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
    public static Request createEntity(EntityManager em) {
        Request request = new Request()
            .requestTitle(DEFAULT_REQUEST_TITLE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateClosed(DEFAULT_DATE_CLOSED)
            .requestContent(DEFAULT_REQUEST_CONTENT)
            .requestStatus(DEFAULT_REQUEST_STATUS)
            .requestSolution(DEFAULT_REQUEST_SOLUTION)
            .attachmentName(DEFAULT_ATTACHMENT_NAME);
        // Add required entity
        Department department = DepartmentResourceIntTest.createEntity(em);
        em.persist(department);
        em.flush();
        request.setDepartment(department);
        // Add required entity
        Student student = StudentResourceIntTest.createEntity(em);
        em.persist(student);
        em.flush();
        request.setStudent(student);
        return request;
    }

    @Before
    public void initTest() {
        request = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);
        restRequestMockMvc.perform(post("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestTitle()).isEqualTo(DEFAULT_REQUEST_TITLE);
        assertThat(testRequest.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testRequest.getDateClosed()).isEqualTo(DEFAULT_DATE_CLOSED);
        assertThat(testRequest.getRequestContent()).isEqualTo(DEFAULT_REQUEST_CONTENT);
        assertThat(testRequest.isRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);
        assertThat(testRequest.getRequestSolution()).isEqualTo(DEFAULT_REQUEST_SOLUTION);
        assertThat(testRequest.getAttachmentName()).isEqualTo(DEFAULT_ATTACHMENT_NAME);
    }

    @Test
    @Transactional
    public void createRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // Create the Request with an existing ID
        request.setId(1L);
        RequestDTO requestDTO = requestMapper.toDto(request);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc.perform(post("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRequestTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setRequestTitle(null);

        // Create the Request, which fails.
        RequestDTO requestDTO = requestMapper.toDto(request);

        restRequestMockMvc.perform(post("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList
        restRequestMockMvc.perform(get("/api/requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestTitle").value(hasItem(DEFAULT_REQUEST_TITLE.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateClosed").value(hasItem(DEFAULT_DATE_CLOSED.toString())))
            .andExpect(jsonPath("$.[*].requestContent").value(hasItem(DEFAULT_REQUEST_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].requestStatus").value(hasItem(DEFAULT_REQUEST_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].requestSolution").value(hasItem(DEFAULT_REQUEST_SOLUTION.toString())))
            .andExpect(jsonPath("$.[*].attachmentName").value(hasItem(DEFAULT_ATTACHMENT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.requestTitle").value(DEFAULT_REQUEST_TITLE.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateClosed").value(DEFAULT_DATE_CLOSED.toString()))
            .andExpect(jsonPath("$.requestContent").value(DEFAULT_REQUEST_CONTENT.toString()))
            .andExpect(jsonPath("$.requestStatus").value(DEFAULT_REQUEST_STATUS.booleanValue()))
            .andExpect(jsonPath("$.requestSolution").value(DEFAULT_REQUEST_SOLUTION.toString()))
            .andExpect(jsonPath("$.attachmentName").value(DEFAULT_ATTACHMENT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        Request updatedRequest = requestRepository.findOne(request.getId());
        updatedRequest
            .requestTitle(UPDATED_REQUEST_TITLE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateClosed(UPDATED_DATE_CLOSED)
            .requestContent(UPDATED_REQUEST_CONTENT)
            .requestStatus(UPDATED_REQUEST_STATUS)
            .requestSolution(UPDATED_REQUEST_SOLUTION)
            .attachmentName(UPDATED_ATTACHMENT_NAME);
        RequestDTO requestDTO = requestMapper.toDto(updatedRequest);

        restRequestMockMvc.perform(put("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestTitle()).isEqualTo(UPDATED_REQUEST_TITLE);
        assertThat(testRequest.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testRequest.getDateClosed()).isEqualTo(UPDATED_DATE_CLOSED);
        assertThat(testRequest.getRequestContent()).isEqualTo(UPDATED_REQUEST_CONTENT);
        assertThat(testRequest.isRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);
        assertThat(testRequest.getRequestSolution()).isEqualTo(UPDATED_REQUEST_SOLUTION);
        assertThat(testRequest.getAttachmentName()).isEqualTo(UPDATED_ATTACHMENT_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequestMockMvc.perform(put("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Get the request
        restRequestMockMvc.perform(delete("/api/requests/{id}", request.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Request.class);
        Request request1 = new Request();
        request1.setId(1L);
        Request request2 = new Request();
        request2.setId(request1.getId());
        assertThat(request1).isEqualTo(request2);
        request2.setId(2L);
        assertThat(request1).isNotEqualTo(request2);
        request1.setId(null);
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestDTO.class);
        RequestDTO requestDTO1 = new RequestDTO();
        requestDTO1.setId(1L);
        RequestDTO requestDTO2 = new RequestDTO();
        assertThat(requestDTO1).isNotEqualTo(requestDTO2);
        requestDTO2.setId(requestDTO1.getId());
        assertThat(requestDTO1).isEqualTo(requestDTO2);
        requestDTO2.setId(2L);
        assertThat(requestDTO1).isNotEqualTo(requestDTO2);
        requestDTO1.setId(null);
        assertThat(requestDTO1).isNotEqualTo(requestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(requestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(requestMapper.fromId(null)).isNull();
    }
}
