package com.tecnologo.grupo3.goandrent.entities;

import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date startDate;
    private Date endDate;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private float finalPrice;

    @OneToOne
    @JoinColumn(name = "paypal_order_id", referencedColumnName = "order_id",
            foreignKey = @ForeignKey(name = "fk_Booking_PaypalPayment_orderId"))
    private PaypalPayment paypalPayment;

    @OneToOne
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(name = "fk_Booking_Review_id"))
    private Review review;

    @ManyToOne
    @JoinColumn(name = "accommodation_id", foreignKey = @ForeignKey(name = "fk_Booking_Accommodation_id"))
    private Accommodation accommodation;

    @ManyToOne
    @JoinColumn(name = "guest_id", foreignKey = @ForeignKey(name = "fk_Booking_Guest_id"))
    private Guest guest;

    public Booking(Date startDate, Date endDate, BookingStatus bookingStatus, PaymentStatus paymentStatus, float finalPrice, Accommodation accommodation, Guest guest) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
        this.finalPrice = finalPrice;
        this.accommodation = accommodation;
        this.guest = guest;
    }
}
