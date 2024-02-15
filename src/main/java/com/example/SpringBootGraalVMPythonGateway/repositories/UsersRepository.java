package com.example.SpringBootGraalVMPythonGateway.repositories;

import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserOrg, Integer> {

    Optional<UserOrg> findByUsername(String username);

    Optional<UserOrg> findByGln(long gln);

}
