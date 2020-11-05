package com.example.springtraining.springlibrary.repository;

import com.example.springtraining.springlibrary.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
