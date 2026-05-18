package com.example.libraryapp.repository;

import com.example.libraryapp.entity.Reservation;
import com.example.libraryapp.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(AppUser user);
}

