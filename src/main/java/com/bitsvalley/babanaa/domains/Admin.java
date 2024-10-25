package com.bitsvalley.babanaa.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Admin {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String email;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime lastUpdated;
    @Getter
    private String createdBy;

    public Admin() {
    }

    public Admin(String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email= email;
    }
    public Admin(Long Id,String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email= email;
        this.Id= Id;
    }
    public void setCreated() {
        this.createdAt = LocalDateTime.now();
    }

    public void setLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
