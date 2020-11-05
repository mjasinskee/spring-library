package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Reader;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserClaimServiceTest {

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
    @Autowired
    UserClaimService userClaimService;

    @BeforeEach
    public void init() {
        thereIsABook("ISBN1", "title1");
        thereIsABook("ISBN2", "title1");
        thereIsABook("ISBN3", "title2");
        thereIsABook("ISBN4", "title2");
        thereIsABook("ISBN5", "title2");
        thereIsAReader(1L, "name1", "lastName1");
    }

    @Test
    public void shouldRemovePenaltyAfterUserPaysIt() {
        //given
        thereIsPenaltyForGivenAccountAndBook(1L, "ISBN1");

        //when
        userClaimService.payPenalty(1L, "ISBN1");

        //then
        assertThereIsNoPenaltyForGivenAccountAndBook(1L);
    }

    void assertThereIsNoPenaltyForGivenAccountAndBook(Long accountId) {
        assertThat(readerRepository.findByAccountId(accountId).get().getPenalties()).isEmpty();
        assertThat(penaltyRepository.findAll()).isEmpty();
    }

    private void thereIsPenaltyForGivenAccountAndBook(Long accountId, String ISBN) {
        LocalDate rentalDate = LocalDate.now();
        rentalService.rent(accountId, ISBN, rentalDate);
        rentalService.takeBack(accountId, ISBN, rentalDate.plusDays(31));
    }

    private void thereIsABook(String isbn, String title) {
        bookRepository.save(new Book(isbn, title));
    }

    private void thereIsAReader(Long accountId, String name, String lastName) {
        readerRepository.save(new Reader(accountId, name, lastName));
    }
}
