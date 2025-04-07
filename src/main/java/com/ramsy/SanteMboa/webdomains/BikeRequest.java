package com.ramsy.SanteMboa.webdomains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BikeRequest {
    String pickup;
    String dropOff;
    String bikeType;
//    float fare;

//    public String getPickup() {
//        return pickup;
//    }
//
//    public void setPickup(String pickup) {
//        this.pickup = pickup;
//    }
//
//    public String getDropOff() {
//        return dropOff;
//    }
//
//    public void setDropOff(String dropOff) {
//        this.dropOff = dropOff;
//    }
//
//    public String getBikeType() {
//        return bikeType;
//    }
//
//    public void setBikeType(String bikeType) {
//        this.bikeType = bikeType;
//    }
}
