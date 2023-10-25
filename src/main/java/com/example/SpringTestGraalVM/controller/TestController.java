package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/showUserInfo")
    public String showUserInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            return personDetails.getPerson().toString();
        } catch (Exception e){
            return "Ошибка в контролере /showUserInfo";
        }

    }


}
