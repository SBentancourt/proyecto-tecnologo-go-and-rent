package com.tecnologo.grupo3.goandrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class PaypalPayment {
    @Id
    @Column(name = "order_id")
    private String orderId;
    private String authorization;
    private String confirmation_id;
    private Integer invoice_id;
}
