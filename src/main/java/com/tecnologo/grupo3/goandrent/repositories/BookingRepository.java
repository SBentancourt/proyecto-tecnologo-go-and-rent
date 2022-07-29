package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // ** RESERVA PENDIENTE O CONFIRMADA EN UN RANGO DE FECHAS ** //
    // -- 1° caso: hay una reserva en dentro del rango de fechas elegido
    // -- 2° caso: hay una reserva que comienza antes de mi fecha de inicio y que termina después de mi fecha de inicio
    // -- 3° caso: hay una reserva que comienza antes de mi fecha de fin y que termina después de mi fecha de fin
    @Query("select case when count(b) > 0 then true else false end from Booking b" +
            " where b.accommodation.id = :idAccommodation and b.bookingStatus in (:activeStatus, :pendingStatus) and" +
            " ((b.startDate >= :startDate and b.endDate <= :endDate) " +
            "       Or (b.startDate <= :startDate and b.endDate > :startDate) " +
            "       Or (b.startDate < :endDate and b.endDate >= :endDate)) and b.paypalPayment.orderId <> ''")
    Boolean existsBookingsInDateRange(@Param("idAccommodation") int accommodation, @Param("startDate") Date startDate,
                                      @Param("endDate") Date endDate, @Param("activeStatus") BookingStatus activeStatus,
                                      @Param("pendingStatus") BookingStatus pendingStatus);

    // ** UN HUÉSPED TIENE AL MENOS UNA RESERVA ACTIVA O PENDIENTE ** //
    @Query("select case when count(b) > 0 then true else false end from Booking b where b.guest.alias= :guestAlias" +
            " and b.bookingStatus in (:activeStatus, :pendingStatus) and b.endDate > :today and b.paypalPayment.orderId <> ''")
    Boolean guestWithBookingsInProgress(@Param("guestAlias") String alias, @Param("today") Date today,
                                        @Param("activeStatus") BookingStatus activeStatus, @Param("pendingStatus") BookingStatus pendingStatus);

    // ** UN ANFITRIÓN TIENE AL MENOS UNA RESERVA ACTIVA O PENDIENTE ** //
    @Query("select case when count(b) > 0 then true else false end from Booking b where b.accommodation.host.alias = :aliasHost" +
            " and b.bookingStatus in (:activeStatus, :pendingStatus) and b.endDate >= :today and b.paypalPayment.orderId <> ''")
    Boolean hostWithBookingsInProgress(@Param("aliasHost") String alias, @Param("today") Date today,
                                       @Param("activeStatus") BookingStatus activeStatus, @Param("pendingStatus") BookingStatus pendingStatus);

    List<Booking> findBookingsByAccommodation_Id(int id);

    // -- Reservas pendientes o confirmadas de un determinado alojamiento
    @Query("select case when count(b) > 0 then true else false end from Booking b where b.accommodation.id =:accommodationId " +
            "and b.bookingStatus in (:activeStatus, :pendingStatus) and b.endDate > :today and b.paypalPayment.orderId <> ''")
    Boolean accommodationWithBookings(@Param("accommodationId") int accommodationId, @Param("today") Date today,
                                        @Param("activeStatus") BookingStatus activeStatus, @Param("pendingStatus") BookingStatus pendingStatus);

    // ** HAY RESERVA EN CURSO PARA DETERMINADO ALOJAMIENTO EN LA FECHA ACTUAL ** //
    @Query(value = "select case when count(b) > 0 then true else false end from Booking b where b.accommodation.id = :accommodationId " +
            "and b.bookingStatus = :activeBookingStatus and b.startDate <= :today and b.endDate >= :today and b.paypalPayment.orderId <> ''")
    Boolean reservationInProgress(@Param("accommodationId") int accommodationId, @Param("activeBookingStatus") BookingStatus activeBookingStatus,
                                  @Param("today") Date today);

    // ** Reservas de un huesped ordenadas por fecha de inicio de mayor a menor ** //
    @Query(value = "select b from Booking b where b.guest.alias = :guestAlias and b.paypalPayment.orderId <> '' order by b.startDate desc")
    List<Booking> guestBookingsStartDateOrderDesc(@Param("guestAlias") String guestAlias);


    @Query(value = "select b from Booking b where b.guest.alias = :guestAlias and b.accommodation.host.alias = :hostAlias " +
            "and b.bookingStatus = :status and b.endDate <= :today and b.paypalPayment.orderId <> '' order by b.endDate")
    List<Booking> guestLastBooking(@Param("guestAlias") String guestAlias, @Param("hostAlias") String hostAlias,
                                   @Param("status") BookingStatus status, @Param("today") Date today);

}





