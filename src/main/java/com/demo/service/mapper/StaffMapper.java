package com.demo.service.mapper;

import com.demo.domain.*;
import com.demo.service.dto.StaffDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Staff and its DTO StaffDTO.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    StaffDTO toDto(Staff staff);

    @Mapping(source = "departmentId", target = "department")
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(Long id) {
        if (id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return staff;
    }
}
