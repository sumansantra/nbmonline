package org.nbm.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Tender.
 */
@Entity
@Table(name = "tender")
public class Tender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tender_name", nullable = false)
    private String tenderName;

    @NotNull
    @Column(name = "publish_date", nullable = false)
    private ZonedDateTime publishDate;

    @NotNull
    @Column(name = "submit_date", nullable = false)
    private ZonedDateTime submitDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "is_single")
    private Boolean isSingle;

    @NotNull
    @Column(name = "tender_file_path", nullable = false)
    private String tenderFilePath;

    @NotNull
    @Lob
    @Column(name = "tender_file", nullable = false)
    private byte[] tenderFile;

    @Column(name = "tender_file_content_type", nullable = false)
    private String tenderFileContentType;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenderName() {
        return tenderName;
    }

    public Tender tenderName(String tenderName) {
        this.tenderName = tenderName;
        return this;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public Tender publishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public ZonedDateTime getSubmitDate() {
        return submitDate;
    }

    public Tender submitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
        return this;
    }

    public void setSubmitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Tender endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isIsSingle() {
        return isSingle;
    }

    public Tender isSingle(Boolean isSingle) {
        this.isSingle = isSingle;
        return this;
    }

    public void setIsSingle(Boolean isSingle) {
        this.isSingle = isSingle;
    }

    public String getTenderFilePath() {
        return tenderFilePath;
    }

    public Tender tenderFilePath(String tenderFilePath) {
        this.tenderFilePath = tenderFilePath;
        return this;
    }

    public void setTenderFilePath(String tenderFilePath) {
        this.tenderFilePath = tenderFilePath;
    }

    public byte[] getTenderFile() {
        return tenderFile;
    }

    public Tender tenderFile(byte[] tenderFile) {
        this.tenderFile = tenderFile;
        return this;
    }

    public void setTenderFile(byte[] tenderFile) {
        this.tenderFile = tenderFile;
    }

    public String getTenderFileContentType() {
        return tenderFileContentType;
    }

    public Tender tenderFileContentType(String tenderFileContentType) {
        this.tenderFileContentType = tenderFileContentType;
        return this;
    }

    public void setTenderFileContentType(String tenderFileContentType) {
        this.tenderFileContentType = tenderFileContentType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Tender isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public Tender isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
        Tender tender = (Tender) o;
        if (tender.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tender.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tender{" +
            "id=" + getId() +
            ", tenderName='" + getTenderName() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", submitDate='" + getSubmitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isSingle='" + isIsSingle() + "'" +
            ", tenderFilePath='" + getTenderFilePath() + "'" +
            ", tenderFile='" + getTenderFile() + "'" +
            ", tenderFileContentType='" + getTenderFileContentType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            "}";
    }
}
