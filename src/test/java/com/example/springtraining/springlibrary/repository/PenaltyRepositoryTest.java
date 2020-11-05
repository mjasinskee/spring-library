package com.example.springtraining.springlibrary.repository;

import com.example.springtraining.springlibrary.model.Book;
import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PenaltyRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    PenaltyRepository penaltyRepository;

    @Test
    public void shouldFindPenaltyByISBNAndReader() {
        //given
        thereIsABook("ISBN1", "title1");
        thereIsAReader(1L,"name1", "lastName1");
        Reader reader = thereIsPenaltyForReader();

        //when
        Optional<Penalty> foundPenalty = penaltyRepository.findByISBNAndReader("ISBN1", reader);

        //then
        assertThat(foundPenalty.get().getISBN()).isEqualTo("ISBN1");
    }

    private void thereIsABook(String isbn, String title) {
        bookRepository.save(new Book(isbn, title));
    }

    private void thereIsAReader(Long accountId, String name, String lastName) {
        readerRepository.save(new Reader(accountId, name, lastName));
    }

    private Reader thereIsPenaltyForReader() {
        Reader reader = readerRepository.findByAccountId(1L).get();
        Penalty penalty = new Penalty("ISBN1", LocalDate.now(), reader);
        reader.getPenalties().add(penalty);
        return readerRepository.saveAndFlush(reader);
    }
}
