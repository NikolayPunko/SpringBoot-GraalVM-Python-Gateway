package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.LoginRequestDto;
import com.example.SpringTestGraalVM.dto.LoginResponseDto;
import com.example.SpringTestGraalVM.dto.PersonDTO;
import com.example.SpringTestGraalVM.exceptions.PersonNotCreatedException;
import com.example.SpringTestGraalVM.model.Person;
import com.example.SpringTestGraalVM.security.JWTUtil;
import com.example.SpringTestGraalVM.service.PersonDetailsService;
import com.example.SpringTestGraalVM.service.RegistrationService;
import com.example.SpringTestGraalVM.util.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthenticationController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, PersonDetailsService personDetailsService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

        this.personDetailsService = personDetailsService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponseDto> performAuthentication(@RequestBody LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());

        authenticationManager.authenticate(authToken);

        String token = jwtUtil.generateToken(loginRequestDto.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }




    @PostMapping("/registration")
    public ResponseEntity<LoginResponseDto> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                                BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors){
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }

        registrationService.register(person);

        String token = jwtUtil.generateToken(person.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return new ModelMapper().map(personDTO, Person.class);
    }


}
