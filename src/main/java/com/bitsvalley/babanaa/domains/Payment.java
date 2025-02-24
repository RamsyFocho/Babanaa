package com.bitsvalley.babanaa.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private String paymentMethod;
    private double amount;
    @Enumerated(EnumType.STRING)
//    Stores enum values as strings in the db
    private TransactionType transaction;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime paymentDate;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;//

    public Payment() {
    }

    public Payment(User user, String paymentMethod, double amount, TransactionType transaction, LocalDateTime paymentDate, TransactionStatus transactionStatus) {
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transaction = transaction;
        this.paymentDate = paymentDate;
        this.transactionStatus = transactionStatus;
    }

    public Payment(Long id, User user, String paymentMethod, double amount, TransactionType transaction, LocalDateTime paymentDate, TransactionStatus transactionStatus) {
        Id = id;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transaction = transaction;
        this.paymentDate = paymentDate;
        this.transactionStatus = transactionStatus;
    }
}
