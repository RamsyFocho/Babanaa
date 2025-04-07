package com.ramsy.SanteMboa.domains.good_delivery;

import com.ramsy.SanteMboa.domains.User;
import com.fasterxml.jackson.annotation.*;
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
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    @JsonBackReference // Prevent serialization of the user in this object
    private DeliveryRequest deliveryRequest;
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
