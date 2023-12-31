package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.dto.PricatFilterRequestDTO;
import com.example.SpringTestGraalVM.dto.PricatResponseDTO;
import com.example.SpringTestGraalVM.exceptions.PricatNotFoundException;
import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.model.pricatXML.PricatXML;
import com.example.SpringTestGraalVM.repositories.PricatRepository;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PricatService {

    private final PricatRepository pricatRepository;

    @Autowired
    public PricatService(PricatRepository pricatRepository) {
        this.pricatRepository = pricatRepository;
    }

    public List<Pricat> findAll() {
        return pricatRepository.findAll();
    }

    @Transactional
    public long importPricat(MultipartFile file) {
        XmlMapper xmlMapper = new XmlMapper();

        PricatXML pricatXML = null;
        try {
            pricatXML = xmlMapper.readValue(file.getBytes(), PricatXML.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Pricat pricat = new Pricat();

        pricat.setOwner(getUserOrgDetails());
        pricat.setF_TM(LocalDateTime.now());
//        pricat.setF_DEL(0);
        pricat.setEDI("001");
        pricat.setTP("PRICAT");
        pricat.setPST("IMPORTED");
        pricat.setNDE(pricatXML.getPr_bgm().getPr_s106().getPr_e1004());
        pricat.setDT(LocalDateTime.now());
        pricat.setDTDOC(LocalDateTime.parse(pricatXML.getPr_dtm().getPr_c507().getPr_e2380(), DATE_FORMAT));
        pricat.setRECEIVER(Long.parseLong(pricatXML.getPr_SG2_list().get(0).getPr_nad().getPr_c082().getPr_e3039()));
        pricat.setSENDER(Long.parseLong(pricatXML.getPr_SG2_list().get(1).getPr_nad().getPr_c082().getPr_e3039()));
        pricat.setDOC(convertXMLtoString(file));
//        pricat.setDTINS(LocalDateTime.now());
//        pricat.setDTUPD(LocalDateTime.now());

        save(pricat);
        return pricat.getF_GUID();
    }

    @Transactional
    public void save(Pricat pricat) {
        pricatRepository.save(pricat);
    }

    public String findPricatById(int id){
        Optional<Pricat> findPricat = pricatRepository.findById(id);
        Pricat pricat = findPricat.orElseThrow(PricatNotFoundException::new);
        return pricat.getDOC();
    }

    public List<Pricat> findPricatByState(String state, PricatFilterRequestDTO filterDTO, int page, int size){
//        System.out.println(filterDTO);
        Pageable pageable = PageRequest.of(page-1, size);
        return pricatRepository.findByPSTAndDTDOCBetweenAndNDE(state, filterDTO.getDocumentDateStart(),
                filterDTO.getDocumentDateEnd(), filterDTO.getDocumentNumber(), pageable);
    }


    private String convertXMLtoString(MultipartFile file) {
        String str = null;
        try {
            str = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str.replaceAll("\\s+", "");
    }

    private UserOrg getUserOrgDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return userOrgDetails.getPerson();
    }

    private static DateTimeFormatter DATE_FORMAT =   new DateTimeFormatterBuilder().appendPattern("yyyyMMdd[ [HH][:mm][:ss][.SSS]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();


}
