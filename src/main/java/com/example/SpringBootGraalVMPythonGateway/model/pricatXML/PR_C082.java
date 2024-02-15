package com.example.SpringBootGraalVMPythonGateway.model.pricatXML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PR_C082 {

    @JacksonXmlProperty(localName = "E3039")
    private String pr_e3039;

    @Override
    public String toString() {
        return "PR_C082{" +
                "pr_e3039='" + pr_e3039 + '\'' +
                '}';
    }
}
