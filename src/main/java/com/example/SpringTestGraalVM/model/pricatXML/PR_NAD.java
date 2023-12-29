package com.example.SpringTestGraalVM.model.pricatXML;

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
public class PR_NAD {

    @JacksonXmlProperty(localName = "C082")
    private PR_C082 pr_c082;


    @Override
    public String toString() {
        return "PR_NAD{" +
                "pr_c082=" + pr_c082 +
                '}';
    }
}
