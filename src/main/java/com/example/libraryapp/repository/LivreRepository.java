package com.example.libraryapp.repository;

import com.example.libraryapp.entity.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LivreRepository extends JpaRepository<Livre, Long> {
    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurId(Long auteurId);
    List<Livre> findByDatePublication(String datePublication);

    Page<Livre> findByTitreContainingIgnoreCase(String titre, Pageable pageable);
    Page<Livre> findByAuteurId(Long auteurId, Pageable pageable);
    Page<Livre> findByDatePublication(String datePublication, Pageable pageable);

    Optional<Livre> findFirstByOrderByIdDesc();
}