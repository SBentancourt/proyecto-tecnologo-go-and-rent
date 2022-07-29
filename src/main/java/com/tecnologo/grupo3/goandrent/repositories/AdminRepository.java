package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.dtos.BookingsByRegionDTO;
import com.tecnologo.grupo3.goandrent.entities.users_types.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findAdminByEmail(String email);
    
    @Query(value = "select " +
            "count(*) AS total, month(b.start_date) AS m, year(b.start_date) AS y, l.country AS country, l.province AS province, l.city AS city " +
            "from accommodation a inner join location l on a.location_id = l.id inner join booking b " +
            "on a.id = b.accommodation_id where b.booking_status = :acceptedStatus " +
            "group by l.country, l.province, l.city, year(b.start_date), month(b.start_date) " +
            "order by l.country, l.province, l.city, year(b.start_date), month(b.start_date)", nativeQuery = true)
    List<Object[]> cantidadReservasPorRegionMes(@Param(value = "acceptedStatus") int acceptedStatus);


    @Query(value = "select count(*) AS total, case when u.role = 'ROLE_GUEST' then 'GUEST' else 'HOST' end ,month(u.created_at) AS month, year(u.created_at) AS year from " +
            "user u where u.role in ('ROLE_GUEST', 'ROLE_HOST') group by u.role, year(u.created_at), month(u.created_at) " +
            "order by u.role, year(u.created_at), month(u.created_at)", nativeQuery = true)
    List<Object[]> cantidadUsuariosRegistradosPorMes();

    @Query(value = "select count(*) as total, case when r.qualification is null then 0 else r.qualification end as calificacion " +
            "from accommodation a inner join booking b on a.id = b.accommodation_id left join review r on b.review_id = r.id " +
            "where booking_status = 0 and end_date < now() group by r.qualification order by r.qualification desc", nativeQuery = true)
    List<Object[]> cantidadAlojamientosPorCalificacion();


    @Query(value = "select count(*) as total, l.country as country, l.province as province, l.city as city " +
            "from accommodation a inner join location l on a.location_id = l.id group by l.country, l.province, l.city", nativeQuery = true)
    List<Object[]> cantidadAlojamientosPorRegion();

    @Query(value = "select count(*) as total, month(a.created_at) as month, year(a.created_at) as year " +
            "from accommodation a where status <> 2 group by year(a.created_at), month(a.created_at) " +
            "order by year(a.created_at), month(a.created_at)", nativeQuery = true)
    List<Object[]> cantidadAlijamientosRegistradosPorMes();


    @Query(value = "select count(*) as total, u.user_status as estado, " +
            "month(u.created_at) as month, year(u.created_at) as year from user u where u.role = 'ROLE_HOST' and " +
            "u.user_status <> 1 group by u.user_status, month(u.created_at), year(u.created_at) order by " +
            "u.user_status, month(u.created_at), year(u.created_at)", nativeQuery = true)
    List<Object[]> cantidadAnfitrionesRechazadosConfirmados();

    @Query(value = "select u.alias as alias, b.id as id_reserva, b.final_price as total_reserva, b.start_date as fecha_inicio, u.account, u.bank from user u inner join accommodation a on " +
            "u.alias = a.host_alias inner join booking b on a.id = b.accommodation_id where " +
            "b.booking_status = 0 and b.payment_status = 0 and b.paypal_order_id <> '' and month(b.start_date) = :month and year(b.start_date) = :year " +
            "order by u.alias, b.id", nativeQuery = true)
    List<Object[]> listadoReservasAnfitriones(@Param(value = "month") int month, @Param(value = "year") int year);
}
