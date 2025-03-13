package com.bitsvalley.babanaa.domains.good_delivery;

public enum RequestStatus {
    Pending,//initiated but not yet completed
    Accepted, // a rider accepts
    PickedUp, // a rider has picked up the good
    InTransit, //on the way to destination
    Delivered, // delivered the good
    Completed,// the transaction is fully processed,and payment settled
    Cancelled, // request was cancelled
    Failed, // could not be completed (rider unavailability, incorrect address)
    Refunded, // in case something happens
}
