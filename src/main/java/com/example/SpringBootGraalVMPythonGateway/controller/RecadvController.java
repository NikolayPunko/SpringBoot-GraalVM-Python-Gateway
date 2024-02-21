package com.example.SpringBootGraalVMPythonGateway.controller;

import com.example.SpringBootGraalVMPythonGateway.dto.PricatFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.PricatImportResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.PricatResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import com.example.SpringBootGraalVMPythonGateway.service.RecadvService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RecadvController {

    private final RecadvService recadvService;

    public RecadvController(RecadvService recadvService) {
        this.recadvService = recadvService;
    }

    @PostMapping(value = "/import/RECADV/Xml")
    public ResponseEntity<PricatImportResponseDTO> importRecadv(@RequestParam("xml") MultipartFile file){
        long id = recadvService.importRecadv(file);
        return ResponseEntity.ok(new PricatImportResponseDTO(id));
    }

    @GetMapping("/RECADV/send/{id}")
    public ResponseEntity<Map> sendPricat(@PathVariable("id") long id){
        recadvService.sendRecadv(id);
        return ResponseEntity.ok(Map.of("successful","true"));
    }

    @PostMapping("/RECADV/{documentState}/list")
    public ResponseEntity<List<PricatResponseDTO>> getPricatByState(@PathVariable String documentState,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "size", defaultValue = "20") int size,
                                                                    @RequestBody @Valid PricatFilterRequestDTO filterDTO){
        return ResponseEntity.ok(recadvService.findRecadvByState(documentState, filterDTO, page, size)
                .stream().map(x -> convertPricatToPricatResponseDTO(x)).collect(Collectors.toList()));
    }

    @GetMapping("/RECADV/{documentId}")
    public ResponseEntity<String> getPricatById(@PathVariable int documentId){
        return ResponseEntity.ok(recadvService.findRecadvById(documentId));
    }


    private static PricatResponseDTO convertPricatToPricatResponseDTO(Pricat pricat){
        PricatResponseDTO responseDTO = new PricatResponseDTO();
        responseDTO.setDocumentId(pricat.getFID());
        responseDTO.setDateTime(pricat.getDT());
        responseDTO.setDateTimeInsert(pricat.getDTINS());
        responseDTO.setDateTimeUpdate(pricat.getDTUPD());
        responseDTO.setDocumentDate(pricat.getDTDOC());
        responseDTO.setEdi(pricat.getEDI());
        responseDTO.setDocumentType(pricat.getTP());
        responseDTO.setDocumentStatus(pricat.getPST());
        responseDTO.setDocumentNumber(pricat.getNDE());
        responseDTO.setSenderId(pricat.getSENDER());
        responseDTO.setReceiverId(pricat.getRECEIVER());
        return responseDTO;
    }

}
