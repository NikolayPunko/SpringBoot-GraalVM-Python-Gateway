package com.example.SpringTestGraalVM.service;

import com.example.SpringTestGraalVM.dto.PricatFilterRequestDTO;
import com.example.SpringTestGraalVM.exceptions.UserOrgNotFoundException;
import com.example.SpringTestGraalVM.exceptions.PricatNotFoundException;
import com.example.SpringTestGraalVM.exceptions.XMLParsingException;
import com.example.SpringTestGraalVM.model.Pricat;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.model.pricatXML.PricatXML;
import com.example.SpringTestGraalVM.repositories.PricatRepository;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.postgresql.util.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
@Transactional(readOnly = true)
public class PricatService {

    private final PricatRepository pricatRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public PricatService(PricatRepository pricatRepository, UsersRepository usersRepository) {
        this.pricatRepository = pricatRepository;
        this.usersRepository = usersRepository;
    }

    public List<Pricat> findAll() {
        return pricatRepository.findAll();
    }

    @Transactional
    public long importPricat(MultipartFile file) {
        Pricat pricat = parsingXMLtoPricat(trimXML(convertXMLtoString(file)), new Pricat());

        pricat.setUSERID(getUserOrgDetails().getId());
        pricat.setFTM(LocalDateTime.now());
//        pricat.setF_DEL(0);
        pricat.setEDI("001");
        pricat.setTP("PRICAT");
        pricat.setPST("IMPORTED");
        pricat.setDT(LocalDateTime.now());
        pricat.setDTINS(LocalDateTime.now());
        pricat.setDTUPD(LocalDateTime.now());

        save(pricat);
        return pricat.getFGUID();
    }

    @Transactional
    public void save(Pricat pricat) {
        pricatRepository.save(pricat);
    }

    public String findPricatById(long id){
        Optional<Pricat> findPricat = pricatRepository.findById(id);
        Pricat pricat = findPricat.orElseThrow(PricatNotFoundException::new);
        System.out.println(pricat.getFID());
        return pricat.getDOC();
    }

    public List<Pricat> findPricatByState(String state, PricatFilterRequestDTO filterDTO, int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return pricatRepository.findByPSTAndDTDOCBetweenAndNDE(state, filterDTO.getDocumentDateStart(),
                filterDTO.getDocumentDateEnd(), filterDTO.getDocumentNumber(), pageable);
    }

    @Transactional
    public void sendPricat(long id){
        Pricat pricat = pricatRepository.findByFGUIDAndUSERIDAndSENDER(id, getUserOrgDetails().getId(), getUserOrgDetails().getGln())
                .orElseThrow(PricatNotFoundException::new);

        UserOrg userOrgOpt = usersRepository.findByGln(pricat.getRECEIVER())
                .orElseThrow(UserOrgNotFoundException::new);

        Pricat copyPricat = new Pricat(pricat);

        copyPricat.setUSERID(userOrgOpt.getId());
        copyPricat.setPST("TRANSFERRED");
        copyPricat.setDTINS(LocalDateTime.now());
        copyPricat.setDTUPD(LocalDateTime.now());

        pricat.setPST("TRANSFERRED");
        pricat.setDTUPD(LocalDateTime.now());

        save(pricat);
        save(copyPricat);
    }

    public Pricat parsingXMLtoPricat(String xml, Pricat pricat){
        try {
            String NDE = "", DTDOC = "", RECEIVER = "", SENDER = "";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            NDE = document.getDocumentElement().getElementsByTagName("BGM").item(0).getChildNodes().item(1)
                    .getChildNodes().item(0).getTextContent();

            DTDOC = document.getDocumentElement().getElementsByTagName("DTM").item(0).getChildNodes().item(0)
                    .getChildNodes().item(1).getTextContent();

            NodeList SG2Elements = document.getDocumentElement().getElementsByTagName("SG2");
            for (int i = 0; i < SG2Elements.getLength(); i++) {
                Node node = SG2Elements.item(i).getChildNodes().item(0)
                        .getChildNodes().item(0);
                if(node.getNodeName().equals("E3035") && node.getTextContent().equals("BY")){
                    RECEIVER = node.getParentNode().getChildNodes().item(1).getFirstChild().getTextContent();
                } else if (node.getNodeName().equals("E3035") && node.getTextContent().equals("SU")) {
                    SENDER = node.getParentNode().getChildNodes().item(1).getFirstChild().getTextContent();
                }
            }

            pricat.setNDE(NDE);
            pricat.setDTDOC(LocalDateTime.parse(DTDOC, DATE_FORMAT));
            pricat.setRECEIVER(Long.parseLong(RECEIVER));
            pricat.setSENDER(Long.parseLong(SENDER));
            pricat.setDOC(xml);

            return pricat;
        } catch (Exception e) {
            throw new XMLParsingException();
        }
    }

    public static String trimXML(String input) {
        BufferedReader reader = new BufferedReader(new StringReader(input));
        StringBuffer result = new StringBuffer();
        try {
            String line;
            while ( (line = reader.readLine() ) != null)
                result.append(line.trim());
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertXMLtoString(MultipartFile file) {
        String str = null;
        try {
            str = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
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
