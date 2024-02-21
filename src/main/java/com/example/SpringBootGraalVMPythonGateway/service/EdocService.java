package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.dto.PricatFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.PricatNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.UserOrgNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.repositories.PricatRepository;
import com.example.SpringBootGraalVMPythonGateway.repositories.UsersRepository;
import com.example.SpringBootGraalVMPythonGateway.security.UserOrgDetails;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
public class EdocService {

    private final PricatRepository pricatRepository;
    private final UsersRepository usersRepository;

    private final EntityManager entityManager;

    @Autowired
    public EdocService(PricatRepository pricatRepository, UsersRepository usersRepository, EntityManager entityManager) {
        this.pricatRepository = pricatRepository;
        this.usersRepository = usersRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public long importEdoc(Pricat pricat) {

        pricat.setUSERID(getUserOrgDetails().getId());
        pricat.setFTM(LocalDateTime.now());
//        pricat.setF_DEL(0);
        pricat.setEDI("001");

        pricat.setPST("IMPORTED");
        pricat.setDT(LocalDateTime.now());
        pricat.setDTINS(LocalDateTime.now());
        pricat.setDTUPD(LocalDateTime.now());

        save(pricat);
        entityManager.refresh(pricat);

        pricat.setDOC(assignUNBAndUNZ(pricat.getDOC(), pricat.getFID())); //решить проблему
        save(pricat);
        return pricat.getFID();
    }

    @Transactional
    public void sendEdoc(String tp, long id) {
        Pricat pricat = pricatRepository.findByTPAndFIDAndUSERIDAndSENDERAndPST(tp, id, getUserOrgDetails().getId(), getUserOrgDetails().getGln(), "IMPORTED")
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

    public String findEdocById(String tp, long id) {
        Optional<Pricat> findPricat = pricatRepository.findByTPAndFID(tp, id);
        Pricat pricat = findPricat.orElseThrow(PricatNotFoundException::new);
        return pricat.getDOC();
    }

    public List<Pricat> findEdocByState(String tp, String state, PricatFilterRequestDTO filterDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("FGUID").descending());
        return pricatRepository.findByTPAndUSERIDAndPSTAndDTBetweenAndNDEStartingWith(tp, getUserOrgDetails().getId(), state, filterDTO.getDocumentDateStart(),
                filterDTO.getDocumentDateEnd(), filterDTO.getDocumentNumber(), pageable);
    }

    @Transactional
    public void save(Pricat pricat) {
        pricatRepository.save(pricat);
    }

    private String assignUNBAndUNZ(String xml, long value) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            if(document.getDocumentElement().getElementsByTagName("UNB").getLength()==0){
                document.createElement("UNB");
            }

            Node nodeUNB = document.getDocumentElement().getElementsByTagName("UNB").item(0);
            for (int i = 0; i < nodeUNB.getChildNodes().getLength(); i++) {
                if(nodeUNB.getChildNodes().item(i).getNodeName().equals("E0020")){
                    removeChildNode(nodeUNB, "E0020");
                }
            }


            if(document.getDocumentElement().getElementsByTagName("UNZ").getLength()==0){
                document.getDocumentElement().appendChild(document.createElement("UNZ"));
            }

            Node nodeUNZ = document.getDocumentElement().getElementsByTagName("UNZ").item(0);
            for (int i = 0; i < nodeUNZ.getChildNodes().getLength(); i++) {
                if(nodeUNZ.getChildNodes().item(i).getNodeName().equals("E0020")){
                    removeChildNode(nodeUNZ, "E0020");
                }
            }

            Element newElement = document.createElement("E0020");
            newElement.setTextContent(String.valueOf(value));

            nodeUNB.appendChild(newElement.cloneNode(true));
            nodeUNZ.appendChild(newElement.cloneNode(true));


            return EdocService.convertDOMXMLtoString(document);
        } catch (Exception e) {
            throw new XMLParsingException();
        }
    }

    private void removeChildNode(Node node, String child) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName().equals(child)) {
                node.removeChild(node.getChildNodes().item(i));
            }
        }
    }


    private UserOrg getUserOrgDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return userOrgDetails.getPerson();
    }

    public static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd[ [HH][:mm][:ss][.SSS]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public static String trimXML(String input) {
        BufferedReader reader = new BufferedReader(new StringReader(input));
        StringBuffer result = new StringBuffer();
        try {
            String line;
            while ((line = reader.readLine()) != null)
                result.append(line.trim());
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertDOMXMLtoString(Document doc) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//            transformer.setOutputProperty( OutputKeys.INDENT, "yes" ); //выравнивание
            transformer.setOutputProperty("encoding", "UTF-8");

            DOMSource source = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            StreamResult _result = new StreamResult(sw);
            transformer.transform(source, _result);
            return sw.toString();
        } catch (TransformerException e) {
            throw new XMLParsingException();
        }
    }

    public static String convertXMLtoString(MultipartFile file) {
        String str = null;
        try {
            str = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
    }














}
