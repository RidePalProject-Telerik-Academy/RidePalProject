package com.example.ridepalapplication.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "synchronizations")
public class SynchronizationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "sync_time")
    private LocalDateTime syncTime;

    public SynchronizationDetails() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(LocalDateTime syncTime) {
        this.syncTime = syncTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SynchronizationDetails synchronizationDetails = (SynchronizationDetails) obj;
        return id == synchronizationDetails.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

}

