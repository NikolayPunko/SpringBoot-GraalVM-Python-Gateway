package com.example.SpringBootGraalVMPythonGateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EdocResponseDTO {

    private long documentId;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeInsert;
    private LocalDateTime dateTimeUpdate;
    private LocalDateTime documentDate;
    private String edi;
    private String documentType;
    private String documentStatus;
    private String documentNumber;
    private long senderId;
    private long receiverId;

}
