package com.bitsvalley.babanaa.domains.Agent;

import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.webdomains.Location;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Customer extends User {
    private String address;
    private String contactInfo;
    private List<Collection> collections;

    @Embedded
    private Location location;
}
