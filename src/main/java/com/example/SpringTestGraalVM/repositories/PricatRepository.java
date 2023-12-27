package com.example.SpringTestGraalVM.repositories;

import com.example.SpringTestGraalVM.model.Pricat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricatRepository extends JpaRepository<Pricat, Integer> {


}
