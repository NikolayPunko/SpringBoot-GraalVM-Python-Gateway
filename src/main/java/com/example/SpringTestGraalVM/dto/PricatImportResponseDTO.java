package com.example.SpringTestGraalVM.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricatImportResponseDTO {

    private long documentId;

    public PricatImportResponseDTO(long documentId) {
        this.documentId = documentId;
    }
}
