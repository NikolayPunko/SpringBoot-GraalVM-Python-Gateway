package com.example.SpringTestGraalVM.repositories;

import com.example.SpringTestGraalVM.model.Pricat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

@Repository
public interface PricatRepository extends JpaRepository<Pricat, Integer> {

    List<Pricat> findByPSTAndDTDOCBetweenAndNDE(String state, LocalDateTime start, LocalDateTime end, String nde, Pageable pageable);
}
