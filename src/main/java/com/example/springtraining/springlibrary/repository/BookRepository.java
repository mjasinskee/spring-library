package com.example.springtraining.springlibrary.repository;

import com.example.springtraining.springlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByISBN(String isbn);
}
