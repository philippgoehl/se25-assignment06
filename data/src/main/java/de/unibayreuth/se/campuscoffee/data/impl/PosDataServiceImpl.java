package de.unibayreuth.se.campuscoffee.data.impl;

import de.unibayreuth.se.campuscoffee.data.mapper.PosEntityMapper;
import de.unibayreuth.se.campuscoffee.data.persistence.AddressEntity;
import de.unibayreuth.se.campuscoffee.data.persistence.PosEntity;
import de.unibayreuth.se.campuscoffee.data.persistence.PosRepository;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.ports.PosDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Implementation of the POS data service that the domain layer provides as a port.
 */
@Service
@RequiredArgsConstructor
class PosDataServiceImpl implements PosDataService {
    private final PosRepository posRepository;
    private final PosEntityMapper posEntityMapper;

    @Override
    public void clear() {
        posRepository.deleteAllInBatch();
        posRepository.flush();
        posRepository.resetSequence();
    }

    @Override
    @NonNull
    public List<Pos> getAll() {
        return posRepository.findAll().stream()
                .map(posEntityMapper::fromEntity)
                .toList();
    }

    @Override
    @NonNull
    public Pos getById(@NonNull Long id) throws PosNotFoundException {
        return posRepository.findById(id)
                .map(posEntityMapper::fromEntity)
                .orElseThrow(() -> new PosNotFoundException("POS with ID " + id + " does not exist."));
    }

    @Override
    @NonNull
    public Pos getByName(@NonNull String name) throws PosNotFoundException {
        return posRepository.findByName(name)
                .map(posEntityMapper::fromEntity)
                .orElseThrow(() -> new PosNotFoundException("POS with name '" + name + "' does not exist."));
    }

    @Override
    @NonNull
    public Pos upsert(@NonNull Pos pos) throws PosNotFoundException {
        if (pos.getId() == null) {
            // create new POS
            pos.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            pos.setUpdatedAt(pos.getCreatedAt());
            return posEntityMapper.fromEntity(posRepository.saveAndFlush(posEntityMapper.toEntity(pos)));
        }

        // update existing task
        PosEntity posEntity = posRepository.findById(pos.getId())
                .orElseThrow(() -> new PosNotFoundException("POS with ID " + pos.getId() + " does not exist."));
        // use mapper to be able to use the house number conversion logic
        PosEntity mappedPosEntity = posEntityMapper.toEntity(pos);
        // update fields
        posEntity.setName(mappedPosEntity.getName());
        posEntity.setDescription(mappedPosEntity.getDescription());
        posEntity.setType(mappedPosEntity.getType());
        posEntity.setCampus(mappedPosEntity.getCampus());
        AddressEntity addressEntity = posEntity.getAddress();
        addressEntity.setStreet(mappedPosEntity.getAddress().getStreet());
        addressEntity.setHouseNumber(mappedPosEntity.getAddress().getHouseNumber());
        addressEntity.setHouseNumberSuffix(mappedPosEntity.getAddress().getHouseNumberSuffix());
        addressEntity.setPostalCode(mappedPosEntity.getAddress().getPostalCode());
        addressEntity.setCity(mappedPosEntity.getAddress().getCity());
        posEntity.setUpdatedAt(LocalDateTime.now());

        return posEntityMapper.fromEntity(posRepository.saveAndFlush(posEntity));
    }
}
