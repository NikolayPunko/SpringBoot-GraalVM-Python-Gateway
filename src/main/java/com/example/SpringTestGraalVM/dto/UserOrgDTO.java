package com.example.SpringTestGraalVM.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrgDTO {

    @NotEmpty(message = "Must not be empty")
    @Size(min = 2, max = 100, message = "Must be between 2 and 100 characters long")
    private String username;

    @Email
    private String email;

    private String password;

}
