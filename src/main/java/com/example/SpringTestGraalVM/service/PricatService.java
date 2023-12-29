package com.example.SpringTestGraalVM.service;

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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

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
        pricat.setF_TM(new Date());
//        pricat.setF_DEL(0);
        pricat.setEDI("001");
        pricat.setTP("PRICAT");
        pricat.setPST("IMPORTED");
        pricat.setNDE(pricatXML.getPr_bgm().getPr_s106().getPr_e1004());
        pricat.setDT(new Date());
        pricat.setDTDOC(LocalDate.parse(pricatXML.getPr_dtm().getPr_c507().getPr_e2380(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        pricat.setRECEIVER(Long.parseLong(pricatXML.getPr_SG2_list().get(0).getPr_nad().getPr_c082().getPr_e3039()));
        pricat.setSENDER(Long.parseLong(pricatXML.getPr_SG2_list().get(1).getPr_nad().getPr_c082().getPr_e3039()));
        pricat.setDOC(convertXMLtoString(file));
//        pricat.setDTINS(new Date());
//        pricat.setDTUPD(new Date());

        save(pricat);
        return pricat.getF_GUID();
    }

    @Transactional
    public void save(Pricat pricat) {
        pricatRepository.save(pricat);
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

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(new File(file.getOriginalFilename()));
        outputStream.write(file.getBytes());
        outputStream.close();
        return convFile;
    }

}
