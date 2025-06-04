package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidPostalCodeException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidHouseNumberException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the POS service that the domain layer provides as a port.
 */
public interface PosService {
    void clear();
    @NonNull
    List<Pos> getAll();
    @NonNull
    Pos getById(@NonNull Long id) throws PosNotFoundException;
    @NonNull
    Pos getByName(@NonNull String name) throws PosNotFoundException;
    @NonNull
    Pos upsert(@NonNull Pos pos) throws PosNotFoundException, InvalidPostalCodeException, InvalidHouseNumberException;
}
