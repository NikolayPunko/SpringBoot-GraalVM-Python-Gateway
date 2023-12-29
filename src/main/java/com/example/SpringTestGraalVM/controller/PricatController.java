package com.example.SpringTestGraalVM.controller;

import com.example.SpringTestGraalVM.dto.PricatImportResponseDTO;
import com.example.SpringTestGraalVM.dto.PricatResponseDTO;
import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.service.PricatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    public List<PricatResponseDTO> getPricats(){
        return pricatService.findAll().stream().map(x -> convertToPricatResponseDTO(x)).collect(Collectors.toList());
    }


    @PostMapping(value = "/import/PRICAT/Xml")
    public ResponseEntity<PricatImportResponseDTO> importPricat(@RequestParam("xml") MultipartFile file){
        long id = pricatService.importPricat(file);
        return ResponseEntity.ok(new PricatImportResponseDTO(id));
    }

    @GetMapping("/PRICAT/send/ID")
    public void sendPricat(){

    }

    @PostMapping("/PRICAT/{documentState}/list") //Чтение списка документов
    public void getPricatByState(){

    }

    @GetMapping("/PRICAT/{documentId}")
    public ResponseEntity<String> getPricatById(@PathVariable int documentId){
        return ResponseEntity.ok(pricatService.findPricatById(documentId));
    }


    private PricatResponseDTO convertToPricatResponseDTO(Pricat pricat) {
        return new ModelMapper().map(pricat, PricatResponseDTO.class);
    }
}
