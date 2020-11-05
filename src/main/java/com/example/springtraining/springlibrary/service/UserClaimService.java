package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.exceptions.NoSuchReaderException;
import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import com.example.springtraining.springlibrary.repository.ReaderRepository;
import org.springframework.stereotype.Service;

@Service
class UserClaimService {

    private final ReaderRepository readerRepository;

    UserClaimService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    void payPenalty(Long accountId, String ISBN) {
        Reader reader = readerRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchReaderException(accountId));
        Penalty penalty = reader.getPenalties().stream()
                .filter(p -> p.getISBN().equals(ISBN))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No such penalty for account " + accountId));
        reader.getPenalties().remove(penalty);
        penalty.setReader(null);
        readerRepository.saveAndFlush(reader);
    }
}
