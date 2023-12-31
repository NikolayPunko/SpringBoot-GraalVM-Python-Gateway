package com.example.SpringTestGraalVM.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricatFilterRequestDTO {

    private LocalDateTime documentDateEnd;

    private LocalDateTime documentDateStart;

    private String documentNumber;

    @Override
    public String toString() {
        return "PricatFilterRequestDTO{" +
                "documentDateEnd=" + documentDateEnd +
                ", documentDateStart=" + documentDateStart +
                ", documentNumber='" + documentNumber + '\'' +
                '}';
    }
}
