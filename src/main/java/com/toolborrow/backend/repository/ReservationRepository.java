package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long > {
    List<Reservation> findByUserIdBorrow(User userIdBorrow);
}
