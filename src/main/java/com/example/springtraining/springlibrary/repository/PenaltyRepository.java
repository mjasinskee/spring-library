package com.example.springtraining.springlibrary.repository;

import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    Optional<Penalty> findByISBNAndReader(String ISBN, Reader reader);
}
