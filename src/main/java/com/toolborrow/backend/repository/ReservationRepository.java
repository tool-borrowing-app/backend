package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long > {
    List<Reservation> findByUserIdBorrow(User userIdBorrow);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        WHERE r.tool.id = :toolId
          AND r.dateFrom <= :to
          AND r.dateTo >= :from
        """)
    boolean existsOverlappingActiveReservation(
        @Param("toolId") Long toolId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );
}
