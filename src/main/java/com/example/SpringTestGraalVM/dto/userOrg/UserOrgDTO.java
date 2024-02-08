package com.example.SpringTestGraalVM.dto.userOrg;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrgDTO {

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 2, max = 100, message = "Должно быть в диапазоне от 2 до 100 символов")
    private String username;

    @Email
    private String email;

    private String password;

}
