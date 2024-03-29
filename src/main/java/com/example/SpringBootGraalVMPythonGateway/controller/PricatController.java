package com.example.SpringBootGraalVMPythonGateway.controller;

import com.example.SpringBootGraalVMPythonGateway.dto.EdocFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.EdocImportResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.dto.EdocResponseDTO;
import com.example.SpringBootGraalVMPythonGateway.model.Edoc;
import com.example.SpringBootGraalVMPythonGateway.service.PricatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PricatController {

    private final PricatService pricatService;

    @Autowired
    public PricatController(PricatService pricatService) {
        this.pricatService = pricatService;
    }

    @GetMapping("/pricats")
    public List<EdocResponseDTO> getPricats(){
        return pricatService.findAll().stream().map(x -> convertPricatToPricatResponseDTO(x)).collect(Collectors.toList());
    }


    @PostMapping(value = "/import/PRICAT/Xml")
    public ResponseEntity<EdocImportResponseDTO> importPricat(@RequestParam("xml") MultipartFile file){
        long id = pricatService.importPricat(file);
        return ResponseEntity.ok(new EdocImportResponseDTO(id));
    }

    @GetMapping("/PRICAT/send/{id}")
    public ResponseEntity<Map> sendPricat(@PathVariable("id") long id){
        pricatService.sendPricat(id);
        return ResponseEntity.ok(Map.of("successful","true"));
    }

    @PostMapping("/PRICAT/{documentState}/list")
    public ResponseEntity<List<EdocResponseDTO>> getPricatByState(@PathVariable String documentState,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size,
                                                                  @RequestBody @Valid EdocFilterRequestDTO filterDTO){
        return ResponseEntity.ok(pricatService.findPricatByState(documentState, filterDTO, page, size)
                .stream().map(x -> convertPricatToPricatResponseDTO(x)).collect(Collectors.toList()));
    }

    @GetMapping("/PRICAT/{documentId}")
    public ResponseEntity<String> getPricatById(@PathVariable int documentId){
        return ResponseEntity.ok(pricatService.findPricatById(documentId));
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
