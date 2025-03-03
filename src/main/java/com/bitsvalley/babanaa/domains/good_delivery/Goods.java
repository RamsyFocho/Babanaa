package com.bitsvalley.babanaa.domains.good_delivery;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "goodId"
//)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long goodId;
    private String goodName;
    private String description;
    private Float weight;// in Kg
//    TODO: include Image
    private int quantity;
//    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<DeliveryRequest> deliveryRequests;


    //    constructors
    public Goods() {
    }
//request
    public Goods(String goodName, String description, Float weight, int quantity) {
        this.goodName = goodName;
        this.description = description;
        this.weight = weight;
        this.quantity = quantity;
    }

    public Goods(Long goodId, String goodName, String description, Float weight, int quantity) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.description = description;
        this.weight = weight;
        this.quantity = quantity;
    }
}
