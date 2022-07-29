package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<Host, String> {
    Optional<Host> findHostByAliasOrEmail(String alias, String email);
    Optional<Host> findHostByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Accommodation a set a.status = :status where a.host.alias = :aliasHost")
    void updateAccommodationStatus(@Param("status") AccommodationStatus status, @Param("aliasHost") String aliasHost);

    @Query(value = "select distinct(u.alias) from user u inner join accommodation a on " +
            "a.host_alias = u.alias inner join booking b on " +
            "b.accommodation_id = a.id where b.booking_status = 0 and b.end_date <= :today and b.guest_id = :alias and u.user_status <> 3", nativeQuery = true)
    List<String> getHostsByGuestAlias(@Param("alias") String alias, @Param("today") Date today);

}
