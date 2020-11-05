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

    public Penalty() {
    }

    public Penalty(String ISBN, LocalDate givenDate, Reader reader) {
        this.ISBN = ISBN;
        this.givenDate = givenDate;
        this.reader = reader;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDate getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(LocalDate givenDate) {
        this.givenDate = givenDate;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

}
