package com.example.SpringTestGraalVM.dto.userOrg;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {

    private String oldPassword;

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 4, max = 30, message = "Должно быть в диапазоне от 4 до 30 символов")
    private String newPassword;

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 4, max = 30, message = "Должно быть в диапазоне от 4 до 30 символов")
    private String newPasswordReplay;

}
