package com.example.SpringTestGraalVM.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String uuid;

    public LoginResponseDto(String uuid) {
        this.uuid = uuid;
    }
}
