package com.demo.service;

import com.demo.service.dto.StaffDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Staff.
 */
public interface StaffService {

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    StaffDTO save(StaffDTO staffDTO);

    /**
     *  Get all the staff.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StaffDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" staff.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StaffDTO findOne(Long id);

    /**
     *  Delete the "id" staff.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
