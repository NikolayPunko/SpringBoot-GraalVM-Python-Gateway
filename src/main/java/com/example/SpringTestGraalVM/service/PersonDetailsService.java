package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.exceptions.PersonNotFoundException;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public PersonDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserOrg> person = usersRepository.findByUsername(username);

        if (person.isEmpty()){
//            throw new UsernameNotFoundException("Username not found!");
            throw new PersonNotFoundException();
        }

        return new UserOrgDetails(person.get());
    }
}
