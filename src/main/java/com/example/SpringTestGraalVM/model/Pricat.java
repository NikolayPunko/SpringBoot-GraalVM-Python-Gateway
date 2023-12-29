package com.example.SpringTestGraalVM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BD_EDOC")
public class Pricat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_GUID")
    private long F_GUID;

    @ManyToOne()
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    private UserOrg owner;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private long F_ID;

    @Column(name = "F_TM")
    private Date F_TM;

    @Column(name = "F_DEL")
    private int F_DEL;

    @Column(name = "EDI")
    private String EDI;

    @Column(name = "TP")
    private String TP;

    @Column(name = "PST")
    private String PST;

    @Column(name = "NDE")
    private String NDE;

    @Column(name = "DT")
    private Date DT;

    @Column(name = "DTDOC")
    private LocalDate DTDOC;

    @Column(name = "RECEIVER")
    private long RECEIVER;

    @Column(name = "SENDER")
    private long SENDER;

    @Column(name = "DOC")
    private String DOC;

    @Column(name = "ID")
    private long ID;

    @Column(name = "DTINS")
    private Date DTINS;

    @Column(name = "DTUPD")
    private Date DTUPD;


    @Override
    public String toString() {
        return "Pricat{" +
                "F_GUID=" + F_GUID +
                ", owner=" + owner +
                ", F_ID=" + F_ID +
                ", F_TM=" + F_TM +
                ", F_DEL=" + F_DEL +
                ", EDI='" + EDI + '\'' +
                ", TP='" + TP + '\'' +
                ", PST='" + PST + '\'' +
                ", NDE='" + NDE + '\'' +
                ", DT=" + DT +
                ", DTDOC=" + DTDOC +
                ", RECEIVER=" + RECEIVER +
                ", SENDER=" + SENDER +
                ", DOC='" + DOC + '\'' +
                ", ID=" + ID +
                ", DTINS=" + DTINS +
                ", DTUPD=" + DTUPD +
                '}';
    }
}
