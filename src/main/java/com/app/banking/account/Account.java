package com.app.banking.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentNumber;

    public Account(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setId(long l) {
        this.id = l;
    }
}
