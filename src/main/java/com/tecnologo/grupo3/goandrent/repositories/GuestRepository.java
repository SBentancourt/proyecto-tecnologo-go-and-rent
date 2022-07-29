package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, String> {
    Optional<Guest> findGuestByAliasOrEmail(String alias, String email);
    Optional<Guest> findGuestByEmail(String email);
    Optional<Guest> findGuestByAlias(String alias);

    @Query(value = "select distinct(g.alias) from Guest g inner join Booking b on " +
            "g.alias = b.guest.alias inner join Accommodation a on " +
            "b.accommodation.id = a.id where " +
            "b.bookingStatus = :status and b.endDate <= :today and a.host.alias = :alias and g.userStatus <> :userStatus")
    List<String> getGuestWithBookingsByHostAlias(@Param("alias") String alias, @Param("status") BookingStatus status,
                                                @Param("today") Date today, @Param("userStatus") UserStatus userStatus);
}
