package com.example.SpringBootGraalVMPythonGateway.controller;

import com.example.SpringBootGraalVMPythonGateway.dto.EdocFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.EdocImportResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.EdocResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.model.Edoc;
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
    public ResponseEntity<EdocImportResponseDTO> importRecadv(@RequestParam("xml") MultipartFile file){
        long id = recadvService.importRecadv(file);
        return ResponseEntity.ok(new EdocImportResponseDTO(id));
    }

    @GetMapping("/RECADV/send/{id}")
    public ResponseEntity<Map> sendPricat(@PathVariable("id") long id){
        recadvService.sendRecadv(id);
        return ResponseEntity.ok(Map.of("successful","true"));
    }

    @PostMapping("/RECADV/{documentState}/list")
    public ResponseEntity<List<EdocResponseDTO>> getPricatByState(@PathVariable String documentState,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size,
                                                                  @RequestBody @Valid EdocFilterRequestDTO filterDTO){
        return ResponseEntity.ok(recadvService.findRecadvByState(documentState, filterDTO, page, size)
                .stream().map(x -> convertPricatToPricatResponseDTO(x)).collect(Collectors.toList()));
    }

    @GetMapping("/RECADV/{documentId}")
    public ResponseEntity<String> getPricatById(@PathVariable int documentId){
        return ResponseEntity.ok(recadvService.findRecadvById(documentId));
    }


    private static EdocResponseDTO convertPricatToPricatResponseDTO(Edoc edoc){
        EdocResponseDTO responseDTO = new EdocResponseDTO();
        responseDTO.setDocumentId(edoc.getFID());
        responseDTO.setDateTime(edoc.getDT());
        responseDTO.setDateTimeInsert(edoc.getDTINS());
        responseDTO.setDateTimeUpdate(edoc.getDTUPD());
        responseDTO.setDocumentDate(edoc.getDTDOC());
        responseDTO.setEdi(edoc.getEDI());
        responseDTO.setDocumentType(edoc.getTP());
        responseDTO.setDocumentStatus(edoc.getPST());
        responseDTO.setDocumentNumber(edoc.getNDE());
        responseDTO.setSenderId(edoc.getSENDER());
        responseDTO.setReceiverId(edoc.getRECEIVER());
        return responseDTO;
    }

}
