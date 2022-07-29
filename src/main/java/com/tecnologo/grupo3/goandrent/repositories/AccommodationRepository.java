package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {

    @Query(value = "select a from Accommodation a where a.status = :statusAcc and " +
            "a.location in (select l from Location l where l.country = :country and l.province = :province and l.city = :city)")
    List<Accommodation> accommodationBySearchNoDates(@Param("country") String country, @Param("province") String province,
                                                     @Param("city") String city, @Param("statusAcc") AccommodationStatus statusAcc);

    @Query(value = "select a from Accommodation a where a.status = :statusAcc and " +
            "a.location in (select l from Location l where l.country = :country and l.province = :province and l.city = :city) and " +
            "a.id not in (select distinct(b.accommodation.id) from Booking b where b.bookingStatus in (:activeStatus, :pendStatus) and " +
            "( ((b.startDate >= :startDate and b.endDate <= :endDate) or (b.startDate <= :startDate and b.endDate > :startDate) or (b.startDate < :endDate and b.endDate >= :endDate) )))")
    List<Accommodation> accommodationBySearchWithDates(@Param("country") String country, @Param("province") String province,
                                                 @Param("city") String city, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                                 @Param("activeStatus") BookingStatus activeStatus, @Param("pendStatus") BookingStatus pendStatus, @Param("statusAcc") AccommodationStatus statusAcc);


    @Query(value = "select AVG(r.qualification) from review r where r.id in (select b.review_id from booking b where b.accommodation_id = :idAccommodation)", nativeQuery = true)
    Optional<Float> getAverageReviewAccommodation(@Param("idAccommodation") int idAccommodation);

    @Query(value = "select a from Accommodation a where a.host.alias = :alias and a.status = :activeStatus")
    List<Accommodation> getActiveAccommodationByHost(@Param("alias") String alias, @Param("activeStatus") AccommodationStatus activeStatus);

    Optional<Accommodation> findAccommodationById(int id);

    Integer countAccommodationsByHost(Host host);

    Optional<Accommodation> findAccommodationByHost_Alias(String alias);

    List<Accommodation> findAccommodationsByHost_AliasAndStatus(String alias, AccommodationStatus status);
    List<Accommodation> findAccommodationsByHost_Alias(String alias);
}
