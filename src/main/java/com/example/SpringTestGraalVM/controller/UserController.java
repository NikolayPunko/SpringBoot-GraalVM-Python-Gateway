package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.userOrg.ChangePasswordRequestDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgProfileDTO;
import com.example.SpringTestGraalVM.dto.userOrg.UserOrgProfileEditDTO;
import com.example.SpringTestGraalVM.exceptions.UserOrgNotUpdatedException;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import com.example.SpringTestGraalVM.service.UserService;
import com.example.SpringTestGraalVM.util.UserOrgValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserOrgValidator userOrgValidator;

    @Autowired
    public UserController(UserService userService, UserOrgValidator userOrgValidator) {
        this.userService = userService;
        this.userOrgValidator = userOrgValidator;
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserOrgProfileDTO> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return ResponseEntity.ok(convertToUserProfileDTO(userOrgDetails.getPerson()));
    }

    @PutMapping("/user/profile/edit")
    public ResponseEntity<?> updateUserData(@RequestBody @Valid UserOrgProfileEditDTO profileDTO,
                                            BindingResult bindingResult){

        userOrgValidator.validate(convertToUserOrg(profileDTO), bindingResult);
        checkBindingResult(bindingResult);

        userService.updateUserData(profileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody @Valid ChangePasswordRequestDTO passwordDTO,
                                            BindingResult bindingResult){

        checkBindingResult(bindingResult);

        userService.updatePassword(passwordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkBindingResult(BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors){
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new UserOrgNotUpdatedException(errorMessage.toString());
        }
    }

    private UserOrgProfileDTO convertToUserProfileDTO(UserOrg userOrg) {
        return new ModelMapper().map(userOrg, UserOrgProfileDTO.class);
    }

    private UserOrg convertToUserOrg(UserOrgProfileEditDTO userOrgProfileEditDTO) {
        return new ModelMapper().map(userOrgProfileEditDTO, UserOrg.class);
    }

}
