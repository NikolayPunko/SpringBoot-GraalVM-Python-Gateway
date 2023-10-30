package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.UserProfileDTO;
import com.example.SpringTestGraalVM.model.Person;
import com.example.SpringTestGraalVM.security.PersonDetails;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return ResponseEntity.ok(convertToUserProfileDTO(personDetails.getPerson()));
    }

    private UserProfileDTO convertToUserProfileDTO(Person person) {
        return new ModelMapper().map(person, UserProfileDTO.class);
    }

}
