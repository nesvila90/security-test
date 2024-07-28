package com.nissum.security.ms.domain.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;

    @PrePersist
    public void prePersist(){
        this.setCreated(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdateDate(){
        this.setModified(LocalDateTime.now());
    }
}
