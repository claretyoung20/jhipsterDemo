package com.demo.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Request entity.
 */
public class RequestDTO implements Serializable {

    private Long id;

    @NotNull
    private String requestTitle;

    private Instant dateCreated;

    private Instant dateClosed;

    private String requestContent;

    private Boolean requestStatus;

    private String requestSolution;

    private String attachmentName;

    private Long departmentId;

    private String departmentName;

    private Long staffId;

    private String staffStaffName;

    private Long studentId;

    private String studentStudentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Instant dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestSolution() {
        return requestSolution;
    }

    public void setRequestSolution(String requestSolution) {
        this.requestSolution = requestSolution;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getRequestStatus() {
        return requestStatus;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffStaffName() {
        return staffStaffName;
    }

    public void setStaffStaffName(String staffStaffName) {
        this.staffStaffName = staffStaffName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentStudentName() {
        return studentStudentName;
    }

    public void setStudentStudentName(String studentStudentName) {
        this.studentStudentName = studentStudentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequestDTO requestDTO = (RequestDTO) o;
        if(requestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
            "id=" + getId() +
            ", requestTitle='" + getRequestTitle() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateClosed='" + getDateClosed() + "'" +
            ", requestContent='" + getRequestContent() + "'" +
            ", requestStatus='" + isRequestStatus() + "'" +
            ", requestSolution='" + getRequestSolution() + "'" +
            ", attachmentName='" + getAttachmentName() + "'" +
            "}";
    }
}
