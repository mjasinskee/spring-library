package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.exceptions.BookRentalLimitMetException;
import com.example.springtraining.springlibrary.exceptions.NoSuchBookException;
import com.example.springtraining.springlibrary.exceptions.NoSuchReaderException;
import com.example.springtraining.springlibrary.exceptions.NoSuchRentalException;
import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Reader;
import com.example.springtraining.springlibrary.model.Rental;
import com.example.springtraining.springlibrary.repository.BookRepository;
import com.example.springtraining.springlibrary.repository.ReaderRepository;
import com.example.springtraining.springlibrary.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class RentalService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final RentalRepository rentalRepository;

    RentalService(BookRepository bookRepository, ReaderRepository readerRepository, RentalRepository rentalRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.rentalRepository = rentalRepository;
    }

    void rent(Long accountId, String ISBN, LocalDate dateOfRenting) {
        Reader reader = readerRepository.findByAccountId(accountId).orElseThrow(() -> new NoSuchReaderException(accountId));
        Book book = bookRepository.findBookByISBN(ISBN).orElseThrow(() -> new NoSuchBookException(ISBN));
        if (reader.getRentals().size() >= 4) {
            throw new BookRentalLimitMetException(accountId);
        }
        Rental rental = new Rental(dateOfRenting, book);
        rental.setReader(reader);
        reader.getRentals().add(rental);
        readerRepository.saveAndFlush(reader);
    }

    void takeBack(Long accountId, String ISBN, LocalDate dateOfTakingBack) {
        Reader reader = readerRepository.findByAccountId(accountId).orElseThrow(() -> new NoSuchReaderException(accountId));
        Rental rental = reader.getRentals().stream().filter(o -> o.getBook().getISBN().equals(ISBN)).findFirst().orElseThrow(() -> new NoSuchRentalException(accountId, ISBN));
        reader.getRentals().remove(rental);
        rental.setReader(null);
        readerRepository.saveAndFlush(reader);
    }
}
