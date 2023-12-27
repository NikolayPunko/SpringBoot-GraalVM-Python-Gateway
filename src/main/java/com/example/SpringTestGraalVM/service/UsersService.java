package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<UserOrg> findByUsername(String username){
        return usersRepository.findByUsername(username);
    }
}
