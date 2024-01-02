package com.example.SpringTestGraalVM.repositories;

import com.example.SpringTestGraalVM.model.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserOrg, Integer> {

    Optional<UserOrg> findByUsername(String username);

    Optional<UserOrg> findByGln(long gln);

}
