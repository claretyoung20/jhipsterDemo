package com.demo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "request_title", nullable = false)
    private String requestTitle;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_closed")
    private Instant dateClosed;

    @Column(name = "request_content")
    private String requestContent;

    @Column(name = "request_status")
    private Boolean requestStatus;

    @Column(name = "request_solution")
    private String requestSolution;

    @Column(name = "attachment_name")
    private String attachmentName;

    @ManyToOne(optional = false)
    @NotNull
    private Department department;

    @ManyToOne
    private Staff staff;

    @ManyToOne(optional = false)
    @NotNull
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public Request requestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
        return this;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Request dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateClosed() {
        return dateClosed;
    }

    public Request dateClosed(Instant dateClosed) {
        this.dateClosed = dateClosed;
        return this;
    }

    public void setDateClosed(Instant dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public Request requestContent(String requestContent) {
        this.requestContent = requestContent;
        return this;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Boolean isRequestStatus() {
        return requestStatus;
    }

    public Request requestStatus(Boolean requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public void setRequestStatus(Boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestSolution() {
        return requestSolution;
    }

    public Request requestSolution(String requestSolution) {
        this.requestSolution = requestSolution;
        return this;
    }

    public void setRequestSolution(String requestSolution) {
        this.requestSolution = requestSolution;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public Request attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Department getDepartment() {
        return department;
    }

    public Request department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Staff getStaff() {
        return staff;
    }

    public Request staff(Staff staff) {
        this.staff = staff;
        return this;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Student getStudent() {
        return student;
    }

    public Request student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        if (request.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), request.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Request{" +
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
