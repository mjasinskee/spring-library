package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.exceptions.BookRentalLimitMetException;
import com.example.springtraining.springlibrary.exceptions.NoSuchBookException;
import com.example.springtraining.springlibrary.exceptions.NoSuchReaderException;
import com.example.springtraining.springlibrary.exceptions.NoSuchRentalException;
import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import com.example.springtraining.springlibrary.model.Rental;
import com.example.springtraining.springlibrary.repository.BookRepository;
import com.example.springtraining.springlibrary.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
class RentalService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    RentalService(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    void rent(Long accountId, String ISBN, LocalDate dateOfRenting) {
        Reader reader = readerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchReaderException(accountId));
        Book book = bookRepository.findBookByISBN(ISBN)
                .orElseThrow(() -> new NoSuchBookException(ISBN));
        if (bookLimitReached(reader)) {
            throw new BookRentalLimitMetException(accountId);
        }
        Rental rental = new Rental(dateOfRenting, book);
        rental.setReader(reader);
        reader.getRentals().add(rental);
        readerRepository.saveAndFlush(reader);
    }

    private boolean bookLimitReached(Reader reader) {
        return reader.getRentals().size() >= 4;
    }

    void takeBack(Long accountId, String ISBN, LocalDate dateOfTakingBack) {
        Reader reader = readerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchReaderException(accountId));
        Rental rental = reader.getRentals().stream()
                .filter(o -> o.getBook().getISBN().equals(ISBN))
                .findFirst()
                .orElseThrow(() -> new NoSuchRentalException(accountId, ISBN));
        if (shouldGivePenalty(rental.getDateOfRental().atStartOfDay(), dateOfTakingBack.atStartOfDay())) {
            Penalty penalty = new Penalty(rental.getBook().getISBN(),dateOfTakingBack, reader);
            reader.getPenalties().add(penalty);
        }
        reader.getRentals().remove(rental);
        rental.setReader(null);
        readerRepository.saveAndFlush(reader);
    }

    private boolean shouldGivePenalty(LocalDateTime dayOfRental, LocalDateTime dayOfGivingBack) {
        return Duration.between(dayOfRental, dayOfGivingBack).toDays() > 30;
    }
}
