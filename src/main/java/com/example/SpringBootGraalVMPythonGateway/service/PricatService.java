package com.example.SpringBootGraalVMPythonGateway.service;

import com.example.SpringBootGraalVMPythonGateway.dto.PricatFilterRequestDTO;
import com.example.SpringBootGraalVMPythonGateway.exceptions.UserOrgNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.PricatNotFoundException;
import com.example.SpringBootGraalVMPythonGateway.exceptions.XMLParsingException;
import com.example.SpringBootGraalVMPythonGateway.model.Pricat;
import com.example.SpringBootGraalVMPythonGateway.model.UserOrg;
import com.example.SpringBootGraalVMPythonGateway.repositories.PricatRepository;
import com.example.SpringBootGraalVMPythonGateway.repositories.UsersRepository;
import com.example.SpringBootGraalVMPythonGateway.security.UserOrgDetails;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Service
@Transactional(readOnly = true)
public class PricatService {

    private final PricatRepository pricatRepository;
    private final UsersRepository usersRepository;

    private final EntityManager entityManager;

    @Autowired
    public PricatService(PricatRepository pricatRepository, UsersRepository usersRepository, EntityManager entityManager) {
        this.pricatRepository = pricatRepository;
        this.usersRepository = usersRepository;
        this.entityManager = entityManager;
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
        entityManager.refresh(pricat);

        pricat.setDOC(assignUNBAndUNZ(pricat.getDOC(), pricat.getFID()));
        save(pricat);
        return pricat.getFID();
    }


    @Transactional
    public void save(Pricat pricat) {
        pricatRepository.save(pricat);
    }

    public String findPricatById(long id) {
        Optional<Pricat> findPricat = pricatRepository.findByTPAndFID("RECADV", id); //узнать про валидацию владельца документа
        Pricat pricat = findPricat.orElseThrow(PricatNotFoundException::new);
        return pricat.getDOC();
    }

    public List<Pricat> findPricatByState(String state, PricatFilterRequestDTO filterDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("FGUID").descending());
        return pricatRepository.findByTPAndUSERIDAndPSTAndDTBetweenAndNDEStartingWith("RECADV", getUserOrgDetails().getId(), state, filterDTO.getDocumentDateStart(),
                filterDTO.getDocumentDateEnd(), filterDTO.getDocumentNumber(), pageable);
    }

    @Transactional
    public void sendPricat(long id) {
        Pricat pricat = pricatRepository.findByTPAndFIDAndUSERIDAndSENDERAndPST("RECADV", id, getUserOrgDetails().getId(), getUserOrgDetails().getGln(), "IMPORTED")
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


//    public String test() {
//        File file = new File("C:\\Users\\User\\Desktop\\XML_test\\PRICAT_Final2_shot.xml");
//        String result;
//        try {
//            result = Files.readString(file.toPath(), Charset.defaultCharset());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        result = assignUNBAndUNZ(trimXML(result), 66666);
//        return result;
//    }

    private String assignUNBAndUNZ(String xml, long value) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

            Node nodeUNZ = document.getDocumentElement().getElementsByTagName("UNZ").item(0);
            removeChildNode(nodeUNZ, "E0020");

            Node nodeUNB = document.getDocumentElement().getElementsByTagName("UNB").item(0);
            removeChildNode(nodeUNB, "E0020");

            Element newElement = document.createElement("E0020");
            newElement.setTextContent(String.valueOf(value));

            nodeUNZ.appendChild(newElement.cloneNode(true));
            nodeUNB.appendChild(newElement.cloneNode(true));

            return convertDOMXMLtoString(document);
        } catch (Exception e) {
            throw new XMLParsingException();
        }
    }

    private void removeChildNode(Node node, String child){
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName().equals(child)) {
                node.removeChild(node.getChildNodes().item(i));
            }
        }
    }

    public Pricat parsingXMLtoPricat(String xml, Pricat pricat) {
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
                if (node.getNodeName().equals("E3035") && node.getTextContent().equals("BY")) {
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
            while ((line = reader.readLine()) != null)
                result.append(line.trim());
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertDOMXMLtoString(Document doc) {
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

    private String convertXMLtoString(MultipartFile file) {
        String str = null;
        try {
            str = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    private UserOrg getUserOrgDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOrgDetails userOrgDetails = (UserOrgDetails) authentication.getPrincipal();
        return userOrgDetails.getPerson();
    }

    private static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd[ [HH][:mm][:ss][.SSS]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();


}
