package com.example.SpringTestGraalVM.dto.userOrg;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserOrgProfileDTO {

    private String username;

    private String email;

    private String role;

    private long gln;

    private String lastName;
    private String firstName;
    private String middleName;
    private String phone;
    private LocalDateTime profileUpdate;
    private LocalDateTime lastLogin;

}
