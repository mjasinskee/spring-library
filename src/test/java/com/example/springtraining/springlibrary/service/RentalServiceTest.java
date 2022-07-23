package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.exceptions.BookRentalLimitMetException;
import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import com.example.springtraining.springlibrary.model.Rental;
import com.example.springtraining.springlibrary.repository.BookRepository;
import com.example.springtraining.springlibrary.repository.PenaltyRepository;
import com.example.springtraining.springlibrary.repository.ReaderRepository;
import com.example.springtraining.springlibrary.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RentalServiceTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    PenaltyRepository penaltyRepository;

    @Autowired
    RentalService rentalService;

    @BeforeEach
    public void init() {
        thereIsABook("ISBN1", "title1");
        thereIsABook("ISBN2", "title1");
        thereIsABook("ISBN3", "title2");
        thereIsABook("ISBN4", "title2");
        thereIsABook("ISBN5", "title2");
        thereIsAReader(1L,"name1", "lastName1");
    }

    @Test
    public void shouldAllowToRentABook() {
        //when
        rentalService.rent(1L, "ISBN1", LocalDate.now());

        //then
        readerHasOneBookRented(1L);
        readerHasRentedBookWithISBN(1L, "ISBN1");
    }

    @Test
    public void shouldGiveBackBookSoRentalIsRemovedFromReader() {
        //when
        rentalService.rent(1L, "ISBN1", LocalDate.now());
        rentalService.takeBack(1L, "ISBN1", LocalDate.now());

        //then
        Iterable<Rental> rentals = rentalRepository.findAll();
        assertThat(rentals).isEmpty();
    }

    @Test
    public void shouldNotDeleteBookItselfAfterGivingBackRented() {
        //when
        rentalService.rent(1L, "ISBN1", LocalDate.now());
        rentalService.takeBack(1L, "ISBN1", LocalDate.now());

        //then
        List<Book> all = bookRepository.findAll();
        assertThat(all).hasSize(5);
    }

//    @Test
//    public void shouldGivePenaltyIfBookReturnedAfterMoreThan30Days() {
//        //when
//        LocalDate rentalDate = LocalDate.now();
//        rentalService.rent(1L, "ISBN1", rentalDate);
//        rentalService.takeBack(1L, "ISBN1", rentalDate.plusDays(31));
//
//        //then
//        List<Penalty> penalties = penaltyRepository.findAll();
//        assertThat(penalties.size()).isEqualTo(1);
//        assertThat(penalties.get(0).getISBN()).isEqualTo("ISBN1");
//    }

    @Test
    public void shouldBeAbleToRentMax4BooksAtOnce() {
        //when
        assertThatThrownBy(() -> {
            rentalService.rent(1L, "ISBN1", LocalDate.now());
            rentalService.rent(1L, "ISBN2", LocalDate.now());
            rentalService.rent(1L, "ISBN3", LocalDate.now());
            rentalService.rent(1L, "ISBN4", LocalDate.now());
            rentalService.rent(1L, "ISBN5", LocalDate.now());
        }).isInstanceOf(BookRentalLimitMetException.class);
    }

    private void readerHasRentedBookWithISBN(long accountId, String isbn) {
        Optional<Reader> reader = readerRepository.findByAccountId(accountId);
        List<Rental> rentals = new ArrayList<>(reader.get().getRentals());
        assertThat(rentals.get(0).getBook().getISBN()).isEqualTo(isbn);
    }

    private void readerHasOneBookRented(long accountId) {
        Optional<Reader> reader = readerRepository.findByAccountId(accountId);
        Reader reader1 = reader.orElseThrow();
        assertThat(reader.get().getRentals()).hasSize(1);
    }

    private void thereIsABook(String isbn, String title) {
        bookRepository.save(new Book(isbn, title));
    }

    private void thereIsAReader(Long accountId, String name, String lastName) {
        readerRepository.save(new Reader(accountId, name, lastName));
    }
}
