package org.nbm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FlowerFestival.
 */
@Entity
@Table(name = "flower_festival")
public class FlowerFestival implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "festival_name", nullable = false)
    private String festivalName;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private String year;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "flowerFestival")
    @JsonIgnore
    private Set<FlowerFestivalImage> flowerFestivalImages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public FlowerFestival festivalName(String festivalName) {
        this.festivalName = festivalName;
        return this;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public String getYear() {
        return year;
    }

    public FlowerFestival year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public FlowerFestival description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public FlowerFestival isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public FlowerFestival isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<FlowerFestivalImage> getFlowerFestivalImages() {
        return flowerFestivalImages;
    }

    public FlowerFestival flowerFestivalImages(Set<FlowerFestivalImage> flowerFestivalImages) {
        this.flowerFestivalImages = flowerFestivalImages;
        return this;
    }

    public FlowerFestival addFlowerFestivalImages(FlowerFestivalImage flowerFestivalImage) {
        this.flowerFestivalImages.add(flowerFestivalImage);
        flowerFestivalImage.setFlowerFestival(this);
        return this;
    }

    public FlowerFestival removeFlowerFestivalImages(FlowerFestivalImage flowerFestivalImage) {
        this.flowerFestivalImages.remove(flowerFestivalImage);
        flowerFestivalImage.setFlowerFestival(null);
        return this;
    }

    public void setFlowerFestivalImages(Set<FlowerFestivalImage> flowerFestivalImages) {
        this.flowerFestivalImages = flowerFestivalImages;
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
        FlowerFestival flowerFestival = (FlowerFestival) o;
        if (flowerFestival.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), flowerFestival.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FlowerFestival{" +
            "id=" + getId() +
            ", festivalName='" + getFestivalName() + "'" +
            ", year='" + getYear() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isDeleted='" + isIsDeleted() + "'" +
            "}";
    }
}
