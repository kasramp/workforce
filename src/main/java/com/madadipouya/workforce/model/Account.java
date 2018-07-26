package com.madadipouya.workforce.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account",
        indexes = {
                @Index(columnList = "id", name = "idx_account_id"),
                @Index(columnList = "username", name = "idx_employee_username")},
        uniqueConstraints = @UniqueConstraint(name = "uc_employee_username", columnNames = {"username"}))
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    @NotBlank
    @Size(min = 3, max = 256)
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8, max = 1024)

    private String password;

    public Account() {

    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
