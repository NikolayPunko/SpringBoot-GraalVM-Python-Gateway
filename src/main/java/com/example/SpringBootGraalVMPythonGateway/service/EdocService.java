package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.dto.EdocFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.EdocNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.UserOrgNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Edoc;
import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.repositories.EdocRepository;
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

    private final EdocRepository edocRepository;
    private final UsersRepository usersRepository;

    private final EntityManager entityManager;

    @Autowired
    public EdocService(EdocRepository edocRepository, UsersRepository usersRepository, EntityManager entityManager) {
        this.edocRepository = edocRepository;
        this.usersRepository = usersRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public long importEdoc(Edoc edoc) {

        edoc.setUSERID(getUserOrgDetails().getId());
        edoc.setFTM(LocalDateTime.now());
//        edoc.setF_DEL(0);
        edoc.setEDI("001");

        edoc.setPST("IMPORTED");
        edoc.setDT(LocalDateTime.now());
        edoc.setDTINS(LocalDateTime.now());
        edoc.setDTUPD(LocalDateTime.now());

        save(edoc);
        entityManager.refresh(edoc);

        edoc.setDOC(assignUNBAndUNZ(edoc.getDOC(), edoc.getFID()));
        save(edoc);
        return edoc.getFID();
    }

    @Transactional
    public void sendEdoc(String tp, long id) {
        Edoc edoc = edocRepository.findByTPAndFIDAndUSERIDAndSENDERAndPST(tp, id, getUserOrgDetails().getId(), getUserOrgDetails().getGln(), "IMPORTED")
                .orElseThrow(() -> new EdocNotFoundException("Документ для отправления не найден!"));

        UserOrg userOrgOpt = usersRepository.findByGln(edoc.getRECEIVER())
                .orElseThrow(UserOrgNotFoundException::new);

        Edoc copyEdoc = new Edoc(edoc);

        copyEdoc.setUSERID(userOrgOpt.getId());
        copyEdoc.setPST("TRANSFERRED");
        copyEdoc.setDTINS(LocalDateTime.now());
        copyEdoc.setDTUPD(LocalDateTime.now());

        edoc.setPST("TRANSFERRED");
        edoc.setDTUPD(LocalDateTime.now());

        save(edoc);
        save(copyEdoc);
    }

    public String findEdocById(String tp, long id) {
        Optional<Edoc> findEdoc = edocRepository.findByTPAndFID(tp, id);
        Edoc edoc = findEdoc.orElseThrow(() -> new EdocNotFoundException("Документ не найден!"));
        return edoc.getDOC();
    }

    public List<Edoc> findEdocByState(String tp, String state, EdocFilterRequestDTO filterDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("FGUID").descending());
        return edocRepository.findByTPAndUSERIDAndPSTAndDTBetweenAndNDEStartingWith(tp, getUserOrgDetails().getId(), state, filterDTO.getDocumentDateStart(),
                filterDTO.getDocumentDateEnd(), filterDTO.getDocumentNumber(), pageable);
    }

    @Transactional
    public void save(Edoc edoc) {
        edocRepository.save(edoc);
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
            throw new XMLParsingException("Не удалось распарсить xml файл!");
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
            throw new XMLParsingException("Не удалось распарсить xml файл!");
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
