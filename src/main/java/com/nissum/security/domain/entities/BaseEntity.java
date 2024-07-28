package com.nissum.security.domain.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;

    @PrePersist
    public void prePersist() {
        this.setCreated(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdateDate() {
        this.setModified(LocalDateTime.now());
    }
}
