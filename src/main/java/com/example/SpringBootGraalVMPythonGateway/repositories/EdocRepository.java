package com.example.SpringBootGraalVMPythonGateway.repositories;

import com.example.SpringBootGraalVMPythonGateway.model.Edoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Repository
public interface EdocRepository extends JpaRepository<Edoc, Long> {

    List<Edoc> findByTPAndUSERIDAndPSTAndDTBetweenAndNDEStartingWith(String tp, int userId, String state, LocalDateTime start, LocalDateTime end, String nde, Pageable pageable);

    Optional<Edoc> findByTPAndFIDAndUSERIDAndSENDERAndPST(String tp, long id, int userId, long sender, String pst);

    Optional<Edoc> findByTPAndFID(String tp, long id);

}
