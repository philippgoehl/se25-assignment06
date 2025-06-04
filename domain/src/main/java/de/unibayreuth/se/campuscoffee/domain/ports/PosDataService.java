package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the POS data service that the domain layer provides as a port.
 */
public interface PosDataService {
    void clear();
    @NonNull
    List<Pos> getAll();
    Pos getById(@NonNull Long id) throws PosNotFoundException;
    Pos getByName(@NonNull String name) throws PosNotFoundException;
    @NonNull
    Pos upsert(@NonNull Pos pos) throws PosNotFoundException;
}
