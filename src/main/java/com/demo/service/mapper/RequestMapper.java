package com.demo.service.mapper;

import com.demo.domain.*;
import com.demo.service.dto.RequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Request and its DTO RequestDTO.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, StaffMapper.class, StudentMapper.class})
public interface RequestMapper extends EntityMapper<RequestDTO, Request> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.staffName", target = "staffStaffName")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.studentName", target = "studentStudentName")
    RequestDTO toDto(Request request);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "staffId", target = "staff")
    @Mapping(source = "studentId", target = "student")
    Request toEntity(RequestDTO requestDTO);

    default Request fromId(Long id) {
        if (id == null) {
            return null;
        }
        Request request = new Request();
        request.setId(id);
        return request;
    }
}
