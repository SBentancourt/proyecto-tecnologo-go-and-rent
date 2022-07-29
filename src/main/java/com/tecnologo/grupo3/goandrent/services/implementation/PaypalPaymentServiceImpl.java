package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.PaypalPayment;
import com.tecnologo.grupo3.goandrent.repositories.PaypalPaymentRepository;
import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.services.PaypalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentServiceImpl implements PaypalPaymentService {

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    @Autowired
    private BookingService bookingService;

    @Override
    public void savePaypalPayment(String id, String auth, String confirmation_id, int idBooking, int invoice_id) {
        PaypalPayment paypalPayment = paypalPaymentRepository.save(new PaypalPayment(id, auth, confirmation_id, invoice_id));
        Booking booking = bookingService.getBookingObject(idBooking);
        booking.setPaypalPayment(paypalPayment);
        bookingService.updateBooking(booking);
    }

    @Override
    public Integer getLastInvoiceId() {
        return paypalPaymentRepository.getLastInvoiceId();
    }

    @Override
    public void updateConfirmationIdInvoiceId(String orderId, String confirmationId, int invoiceId) {
        PaypalPayment paypalPayment = paypalPaymentRepository.getById(orderId);
        paypalPayment.setConfirmation_id(confirmationId);
        paypalPayment.setInvoice_id(invoiceId);
        paypalPaymentRepository.save(paypalPayment);
    }

    @Override
    public String getConfirmationId(String orderId) {
        return paypalPaymentRepository.getById(orderId).getConfirmation_id();
    }

    @Override
    public Integer getInvoiceId(String orderId) {
        return paypalPaymentRepository.getById(orderId).getInvoice_id();
    }
}
