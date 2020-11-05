package com.example.springtraining.springlibrary.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ISBN;
    private LocalDate givenDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Reader reader;
}
