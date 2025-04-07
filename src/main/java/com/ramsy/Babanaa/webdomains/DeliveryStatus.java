package com.ramsy.Babanaa.webdomains;

import com.ramsy.Babanaa.domains.good_delivery.RequestStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeliveryStatus {
    private Long riderId;
    private Long deliveryId;
    private RequestStatus status;
}
