package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserOrg userOrg){
        userOrg.setRole("ROLE_USER");
        userOrg.setGln(1111111111111L);
        String encodedPassword = passwordEncoder.encode(userOrg.getPassword()); //шифруем
        userOrg.setPassword(encodedPassword);
        usersRepository.save(userOrg);
    }
}
