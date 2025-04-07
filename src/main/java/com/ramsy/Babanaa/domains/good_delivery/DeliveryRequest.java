package com.ramsy.Babanaa.domains.good_delivery;

import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "deliveryId"
//)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class DeliveryRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference  // Matches `user-deliveryRequests` in `User`
    private User user;

    @ManyToOne
    @JoinColumn(name = "riderId")
    private BikeRider bikeRider;

    @OneToMany(mappedBy = "deliveryRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Ensures correct serialization
    private List<Goods> goods;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime requestTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completionTime;

    private String pickupLocation;
    private String dropoffLocation;
    private float fare;
    private RequestStatus status; // pending, accepted, completed, cancelled

    public DeliveryRequest() {
    }

    public DeliveryRequest(User user, BikeRider bikeRider, LocalDateTime requestTime, LocalDateTime completionTime, String pickupLocation, String dropoffLocation, float fare, RequestStatus status) {
        this.user = user;
        this.bikeRider = bikeRider;
        this.requestTime = requestTime;
        this.completionTime = completionTime;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
        this.status = status;
    }
        //----delivery request----
    public DeliveryRequest(User user, List<Goods> goods, String pickupLocation, String dropoffLocation, float fare) {
        this.user = user;
        this.goods = goods;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
    }
    public DeliveryRequest( List<Goods> goods, String pickupLocation, String dropoffLocation, float fare) {
        this.goods = goods;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
    }


    public DeliveryRequest(Long deliveryRequestId, User user, BikeRider bikeRider, LocalDateTime requestTime, LocalDateTime completionTime, String pickupLocation, String dropoffLocation, float fare, RequestStatus status) {
        this.deliveryId = deliveryRequestId;
        this.user = user;
        this.bikeRider = bikeRider;
        this.requestTime = requestTime;
        this.completionTime = completionTime;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
        this.status = status;
    }
}
