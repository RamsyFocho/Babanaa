package com.bitsvalley.babanaa.domains.good_delivery;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
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
    @ManyToOne
    @JoinColumn(name = "goodId")
//    @JsonBackReference
    private Goods goods;
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
    public DeliveryRequest(User user, Goods goods, String pickupLocation, String dropoffLocation, float fare) {
        this.user = user;
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
