package com.example.springtraining.springlibrary.repository;

import com.example.springtraining.springlibrary.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    public Optional<Reader> findByAccountId(Long accountId);
}
