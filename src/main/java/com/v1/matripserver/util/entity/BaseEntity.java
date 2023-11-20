package com.v1.matripserver.util.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "create_dt", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "update_dt", nullable = false, updatable = false)
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        status = Status.ACTIVE;
        onUpdate();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
