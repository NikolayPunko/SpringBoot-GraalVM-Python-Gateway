package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.UserOrgProfileDTO;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
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
    public ResponseEntity<UserOrgProfileDTO> getUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return ResponseEntity.ok(convertToUserProfileDTO(userOrgDetails.getPerson()));
    }

    private UserOrgProfileDTO convertToUserProfileDTO(UserOrg userOrg) {
        return new ModelMapper().map(userOrg, UserOrgProfileDTO.class);
    }

}
