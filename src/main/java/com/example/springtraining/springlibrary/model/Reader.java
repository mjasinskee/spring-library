package com.example.springtraining.springlibrary.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private Long accountId;
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rental> rentals = new HashSet<>();
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Penalty> penalties = new HashSet<>();

    public Reader() {
    }

    public Reader(Long accountId, String firstName, String lastName) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public Set<Penalty> getPenalties() {
        return penalties;
    }

    public void setPenalties(Set<Penalty> penalties) {
        this.penalties = penalties;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", rentals=" + rentals +
                ", penalties=" + penalties +
                '}';
    }
}
