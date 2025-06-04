package de.unibayreuth.se.campuscoffee.domain.impl;

import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidPostalCodeException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidHouseNumberException;
import de.unibayreuth.se.campuscoffee.domain.util.AddressValidator;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.ports.PosDataService;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosServiceImpl implements PosService {
    private final PosDataService posDataService;
    private final AddressValidator addressValidator;

    @Override
    public void clear() {
        posDataService.clear();
    }

    @Override
    @NonNull
    public List<Pos> getAll() {
        return posDataService.getAll();
    }

    @Override
    @NonNull
    public Pos getById(@NonNull Long id) throws PosNotFoundException {
        return posDataService.getById(id);
    }

    @Override
    @NonNull
    public Pos getByName(@NonNull String name) throws PosNotFoundException {
        return posDataService.getByName(name);
    }

    @Override
    @NonNull
    public Pos upsert(@NonNull Pos pos) throws PosNotFoundException, InvalidPostalCodeException, InvalidHouseNumberException {
        if (pos.getId() != null) {
            posDataService.getById(pos.getId()); // throws exception if POS does not exist
        }
        addressValidator.validateAddress(pos);
        return posDataService.upsert(pos);
    }
}
