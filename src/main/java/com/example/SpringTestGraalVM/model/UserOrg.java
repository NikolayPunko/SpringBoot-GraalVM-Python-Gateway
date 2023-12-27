package com.example.SpringTestGraalVM.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "User_org")
public class UserOrg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Must not be empty")
    @Size(min = 4, max = 100, message = "Must be between 4 and 100 characters long")
    @Column(name = "username")
    private String username;

    @Email
    @NotEmpty(message = "Must not be empty")
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;


    @Column(name = "gln")
    private long gln;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pricat> pricatList;
    public UserOrg() {

    }

    public UserOrg(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserOrg(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserOrg(String username, String email, String password, long gln) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gln = gln;
    }

    public UserOrg(String username, String email, String password, long gln, List<Pricat> pricatList) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gln = gln;
        this.pricatList = pricatList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", gln=" + gln +
                '}';
    }
}
