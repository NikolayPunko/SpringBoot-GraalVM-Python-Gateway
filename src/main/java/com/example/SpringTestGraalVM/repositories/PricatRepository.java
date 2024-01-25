package com.example.SpringTestGraalVM.repositories;

import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.model.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Repository
public interface PricatRepository extends JpaRepository<Pricat, Long> {

    List<Pricat> findByUSERIDAndPSTAndDTBetweenAndNDEStartingWith(int userId, String state, LocalDateTime start, LocalDateTime end, String nde, Pageable pageable);

    Optional<Pricat> findByFIDAndUSERIDAndSENDERAndPST( long id, int userId, long sender, String pst);

    Optional<Pricat> findByFID(long id);

}
