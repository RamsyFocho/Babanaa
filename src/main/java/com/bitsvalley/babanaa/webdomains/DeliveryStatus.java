package com.bitsvalley.babanaa.webdomains;

import com.bitsvalley.babanaa.domains.good_delivery.RequestStatus;
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
