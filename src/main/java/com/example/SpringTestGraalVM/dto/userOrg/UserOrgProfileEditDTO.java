package com.example.SpringTestGraalVM.dto.userOrg;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrgProfileEditDTO {

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 4, max = 20, message = "Должно быть в диапазоне от 4 до 20 символов")
    private String username;

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 1, max = 30, message = "Должно быть в диапазоне от 1 до 30 символов")
    @Email
    private String email;
}
