package com.demo.service.mapper;

import com.demo.domain.*;
import com.demo.service.dto.StudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentDepartmentName")
    StudentDTO toDto(Student student); 

    @Mapping(source = "departmentId", target = "department")
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
