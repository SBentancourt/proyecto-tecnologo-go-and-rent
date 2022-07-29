package com.tecnologo.grupo3.goandrent.services;

public interface PaypalPaymentService {
    void savePaypalPayment(String id, String auth, String confirmation_id, int idBooking, int invoiceId);
    Integer getLastInvoiceId();
    void updateConfirmationIdInvoiceId(String orderId, String confirmationId, int invoiceId);
    String getConfirmationId(String orderId);
    Integer getInvoiceId(String orderId);
}
