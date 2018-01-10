package org.nbm.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FlowerFestivalImage.
 */
@Entity
@Table(name = "flower_festival_image")
public class FlowerFestivalImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_name", nullable = false)
    private String imageName;

    @NotNull
    @Lob
    @Column(name = "image_file", nullable = false)
    private byte[] imageFile;

    @Column(name = "image_file_content_type", nullable = false)
    private String imageFileContentType;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(optional = false)
    @NotNull
    private FlowerFestival flowerFestival;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public FlowerFestivalImage imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public FlowerFestivalImage imageFile(byte[] imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageFileContentType() {
        return imageFileContentType;
    }

    public FlowerFestivalImage imageFileContentType(String imageFileContentType) {
        this.imageFileContentType = imageFileContentType;
        return this;
    }

    public void setImageFileContentType(String imageFileContentType) {
        this.imageFileContentType = imageFileContentType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public FlowerFestivalImage isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public FlowerFestivalImage isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public FlowerFestival getFlowerFestival() {
        return flowerFestival;
    }

    public FlowerFestivalImage flowerFestival(FlowerFestival flowerFestival) {
        this.flowerFestival = flowerFestival;
        return this;
    }

    public void setFlowerFestival(FlowerFestival flowerFestival) {
        this.flowerFestival = flowerFestival;
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
        FlowerFestivalImage flowerFestivalImage = (FlowerFestivalImage) o;
        if (flowerFestivalImage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), flowerFestivalImage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FlowerFestivalImage{" +
            "id=" + getId() +
            ", imageName='" + getImageName() + "'" +
            ", imageFile='" + getImageFile() + "'" +
            ", imageFileContentType='" + getImageFileContentType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            "}";
    }
}
