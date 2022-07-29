package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.PaypalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalPaymentRepository extends JpaRepository<PaypalPayment, String> {

    @Query(value = "select p.invoice_id from paypal_payment p order by p.invoice_id desc limit 1", nativeQuery = true)
    Integer getLastInvoiceId();
}
