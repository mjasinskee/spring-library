package com.example.springtraining.springlibrary.service;

import com.example.springtraining.springlibrary.model.Penalty;
import com.example.springtraining.springlibrary.model.Reader;
import com.example.springtraining.springlibrary.model.Rental;
import com.example.springtraining.springlibrary.repository.PenaltyRepository;
import com.example.springtraining.springlibrary.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class PenaltyService {

    private final ReaderRepository readerRepository;
    private final PenaltyRepository penaltyRepository;

    PenaltyService(ReaderRepository readerRepository, PenaltyRepository penaltyRepository) {
        this.readerRepository = readerRepository;
        this.penaltyRepository = penaltyRepository;
    }

    void givePenaltyToAllOutdatedRentals(LocalDate examineDate) {
        List<Reader> readers = readerRepository.findAll();
        readers.forEach(reader -> {
                    Set<Rental> outdatedRentals = reader.getRentals().stream()
                            .filter(rental -> isRentalOutdated(rental.getDateOfRental(), examineDate))
                            .filter(rental -> thereIsNoPenaltyFor(rental.getBook().getISBN(), reader))
                            .collect(Collectors.toSet());
                    givePenaltyTo(outdatedRentals, examineDate);
                }
        );
    }

    private boolean thereIsNoPenaltyFor(String isbn, Reader reader) {
        Optional<Penalty> penalty = penaltyRepository.findByISBNAndReader(isbn, reader);
        return penalty.isEmpty();
    }

    private void givePenaltyTo(Set<Rental> outdatedRentals, LocalDate examineDate) {
        outdatedRentals.forEach(rental -> {
            Penalty penalty = new Penalty(rental.getBook().getISBN(), examineDate, rental.getReader());
            rental.getReader().getPenalties().add(penalty);
            readerRepository.saveAndFlush(rental.getReader());
        });
    }

    boolean isRentalOutdated(LocalDate rentalDate, LocalDate examineDate) {
        return Duration.between(rentalDate.atStartOfDay(), examineDate.atStartOfDay()).toDays() > 30;
    }
}
