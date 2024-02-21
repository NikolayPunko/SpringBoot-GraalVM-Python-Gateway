package com.example.SpringBootGraalVMPythonGateway.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdocImportResponseDTO {

    private long documentId;

    public EdocImportResponseDTO(long documentId) {
        this.documentId = documentId;
    }
}
