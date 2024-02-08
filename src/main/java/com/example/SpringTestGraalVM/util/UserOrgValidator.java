package com.example.SpringTestGraalVM.util;

import com.example.SpringTestGraalVM.dto.userOrg.UserOrgDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgProfileEditDTO;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import com.example.SpringTestGraalVM.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserOrgValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserOrgValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserOrg.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserOrg userOrg = (UserOrg) target;

        if (userService.findByUsername(userOrg.getUsername()).isPresent() && !getUserOrgDetails().getUsername().equals(userOrg.getUsername()))
            errors.rejectValue("username", "", "Пользователь с таким именем уже существует.");
    }

    private UserOrg getUserOrgDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return userOrgDetails.getPerson();
    }

}
