package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.dto.userOrg.ChangePasswordRequestDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgProfileDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgProfileEditDTO;
import com.example.SpringTestGraalVM.exceptions.UserOrgNotUpdatedException;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserOrg> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public void updateUserData(UserOrgProfileEditDTO updatedProfile) {
        UserOrg userOrg = getUserOrgDetails();
        userOrg.setUsername(updatedProfile.getUsername());
        userOrg.setEmail(updatedProfile.getEmail());
        usersRepository.save(userOrg);
    }

    public void updatePassword(ChangePasswordRequestDTO obj) {
        if (!obj.getNewPassword().equals(obj.getNewPasswordReplay()))
            throw new UserOrgNotUpdatedException("New passwords don't match");

        if (!passwordEncoder.matches(obj.getOldPassword(), getUserOrgDetails().getPassword()))
            throw new UserOrgNotUpdatedException("Old password is incorrect");

        UserOrg userOrg = getUserOrgDetails();
        String encodedPassword = passwordEncoder.encode(obj.getNewPassword());
        userOrg.setPassword(encodedPassword);
        usersRepository.save(userOrg);
    }

    private UserOrg getUserOrgDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return userOrgDetails.getPerson();
    }

}
