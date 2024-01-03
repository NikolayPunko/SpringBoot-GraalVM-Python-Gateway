package com.example.SpringTestGraalVM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private long FGUID;

    @Column(name = "User_id")
    private int USERID;

    @Column(name = "F_ID")
    private long FID;

    @Column(name = "F_TM")
    private LocalDateTime FTM;

    @Column(name = "F_DEL")
    private int FDEL;

    @Column(name = "EDI")
    private String EDI;

    @Column(name = "TP")
    private String TP;

    @Column(name = "PST")
    private String PST;

    @Column(name = "NDE")
    private String NDE;

    @DateTimeFormat(pattern = "yyyyMMdd[ [HH][:mm][:ss][.SSS]]")  //разобраться с преобразованием из БД
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT")
    private LocalDateTime DT;

    @Column(name = "DTDOC")
    private LocalDateTime DTDOC;

    @Column(name = "RECEIVER")
    private long RECEIVER;

    @Column(name = "SENDER")
    private long SENDER;

    @Column(name = "DOC")
    private String DOC;

    @Column(name = "ID")
    private long ID;

    @Column(name = "DTINS")
    private LocalDateTime DTINS;

    @Column(name = "DTUPD")
    private LocalDateTime DTUPD;

    public Pricat(Pricat pricat) {
//        this.FGUID = pricat.getFGUID();
//        this.USERID = pricat.getUSERID();
        this.FID = pricat.getFID();
        this.FTM = pricat.getFTM();
        this.FDEL = pricat.getFDEL();
        this.EDI = pricat.getEDI();
        this.TP = pricat.getTP();
        this.PST = pricat.getPST();
        this.NDE = pricat.getNDE();
        this.DT = pricat.getDT();
        this.DTDOC = pricat.getDTDOC();
        this.RECEIVER = pricat.getRECEIVER();
        this.SENDER = pricat.getSENDER();
        this.DOC = pricat.getDOC();
        this.ID = pricat.getID();
        this.DTINS = pricat.getDTINS();
        this.DTUPD = pricat.getDTUPD();
    }

    @Override
    public String toString() {
        return "Pricat{" +
                "F_GUID=" + FGUID +
                ", User_id=" + USERID +
                ", F_ID=" + FID +
                ", F_TM=" + FTM +
                ", F_DEL=" + FDEL +
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
