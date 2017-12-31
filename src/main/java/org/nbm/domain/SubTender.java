package org.nbm.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SubTender.
 */
@Entity
@Table(name = "sub_tender")
public class SubTender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sub_tender_name", nullable = false)
    private String subTenderName;

    @NotNull
    @Column(name = "publish_date", nullable = false)
    private ZonedDateTime publishDate;

    @NotNull
    @Column(name = "submit_date", nullable = false)
    private ZonedDateTime submitDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "sub_tender_file_path")
    private String subTenderFilePath;

    @NotNull
    @Lob
    @Column(name = "sub_tender_file", nullable = false)
    private byte[] subTenderFile;

    @Column(name = "sub_tender_file_content_type", nullable = false)
    private String subTenderFileContentType;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(optional = false)
    @NotNull
    private Tender tender;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubTenderName() {
        return subTenderName;
    }

    public SubTender subTenderName(String subTenderName) {
        this.subTenderName = subTenderName;
        return this;
    }

    public void setSubTenderName(String subTenderName) {
        this.subTenderName = subTenderName;
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public SubTender publishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public ZonedDateTime getSubmitDate() {
        return submitDate;
    }

    public SubTender submitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
        return this;
    }

    public void setSubmitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public SubTender endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getSubTenderFilePath() {
        return subTenderFilePath;
    }

    public SubTender subTenderFilePath(String subTenderFilePath) {
        this.subTenderFilePath = subTenderFilePath;
        return this;
    }

    public void setSubTenderFilePath(String subTenderFilePath) {
        this.subTenderFilePath = subTenderFilePath;
    }

    public byte[] getSubTenderFile() {
        return subTenderFile;
    }

    public SubTender subTenderFile(byte[] subTenderFile) {
        this.subTenderFile = subTenderFile;
        return this;
    }

    public void setSubTenderFile(byte[] subTenderFile) {
        this.subTenderFile = subTenderFile;
    }

    public String getSubTenderFileContentType() {
        return subTenderFileContentType;
    }

    public SubTender subTenderFileContentType(String subTenderFileContentType) {
        this.subTenderFileContentType = subTenderFileContentType;
        return this;
    }

    public void setSubTenderFileContentType(String subTenderFileContentType) {
        this.subTenderFileContentType = subTenderFileContentType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public SubTender isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public SubTender isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Tender getTender() {
        return tender;
    }

    public SubTender tender(Tender tender) {
        this.tender = tender;
        return this;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
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
        SubTender subTender = (SubTender) o;
        if (subTender.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subTender.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubTender{" +
            "id=" + getId() +
            ", subTenderName='" + getSubTenderName() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", submitDate='" + getSubmitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subTenderFilePath='" + getSubTenderFilePath() + "'" +
            ", subTenderFile='" + getSubTenderFile() + "'" +
            ", subTenderFileContentType='" + getSubTenderFileContentType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            "}";
    }
}
