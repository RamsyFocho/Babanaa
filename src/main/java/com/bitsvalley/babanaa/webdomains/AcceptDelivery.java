package com.bitsvalley.babanaa.webdomains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AcceptDelivery {
    private Long riderId;
    private Long deliveryId;

}
