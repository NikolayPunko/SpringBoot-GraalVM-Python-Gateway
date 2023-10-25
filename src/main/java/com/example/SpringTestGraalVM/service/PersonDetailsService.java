package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.exceptions.PersonNotFoundException;
import com.example.SpringTestGraalVM.model.Person;
import com.example.SpringTestGraalVM.repositories.PeopleRepository;
import com.example.SpringTestGraalVM.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);

        if (person.isEmpty()){
//            throw new UsernameNotFoundException("Username not found!");
            throw new PersonNotFoundException();
        }

        return new PersonDetails(person.get());
    }
}
