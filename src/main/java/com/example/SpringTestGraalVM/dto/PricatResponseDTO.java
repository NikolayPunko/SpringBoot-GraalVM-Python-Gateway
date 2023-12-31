package com.example.SpringTestGraalVM.dto;

import com.example.SpringTestGraalVM.model.UserOrg;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PricatResponseDTO {

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
