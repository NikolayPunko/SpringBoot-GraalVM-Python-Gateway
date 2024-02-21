package com.example.SpringBootGraalVMPythonGateway.repositories;

import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Repository
public interface PricatRepository extends JpaRepository<Pricat, Long> {

    List<Pricat> findByTPAndUSERIDAndPSTAndDTBetweenAndNDEStartingWith(String tp, int userId, String state, LocalDateTime start, LocalDateTime end, String nde, Pageable pageable);

    Optional<Pricat> findByTPAndFIDAndUSERIDAndSENDERAndPST(String tp, long id, int userId, long sender, String pst);

    Optional<Pricat> findByTPAndFID(String tp, long id);

}
