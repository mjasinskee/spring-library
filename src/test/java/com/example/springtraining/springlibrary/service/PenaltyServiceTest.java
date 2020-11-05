package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Penalty;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PenaltyServiceTest {

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
    PenaltyService penaltyService;

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
    public void shouldGivePeanltyToAllOutdatedRentals() {
        //given
        LocalDate rentalDate = LocalDate.now();
        rentalService.rent(1L, "ISBN1",rentalDate);
        rentalService.rent(1L, "ISBN2",rentalDate);
        rentalService.rent(1L, "ISBN3",rentalDate);

        //when
        penaltyService.givePenaltyToAllOutdatedRentals(rentalDate.plusDays(31));

        //then
        List<Penalty> penalties = penaltyRepository.findAll();
        assertThat(penalties.size()).isEqualTo(3);
    }

    private void thereIsABook(String isbn, String title) {
        bookRepository.save(new Book(isbn, title));
    }

    private void thereIsAReader(Long accountId, String name, String lastName) {
        readerRepository.save(new Reader(accountId, name, lastName));
    }
}
