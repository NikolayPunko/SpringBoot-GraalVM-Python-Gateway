package com.example.SpringTestGraalVM.util;

import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserOrgValidator implements Validator {

    private final UsersService usersService;

    @Autowired
    public UserOrgValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserOrg.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserOrg userOrg = (UserOrg) target;

        if (usersService.findByUsername(userOrg.getUsername()).isPresent())
            errors.rejectValue("username", "", "A user with the same name already exists");
    }
}
