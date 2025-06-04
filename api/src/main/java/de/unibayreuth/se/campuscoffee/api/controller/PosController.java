package de.unibayreuth.se.campuscoffee.api.controller;

import de.unibayreuth.se.campuscoffee.api.dtos.PosDto;
import de.unibayreuth.se.campuscoffee.api.mapper.PosDtoMapper;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "CampusCoffee",
                version = "0.0.1"
        )
)
@Tag(name = "POS")
@Controller
@RequestMapping("/api/pos")
@RequiredArgsConstructor
public class PosController {
    private final PosService posService;
    private final PosDtoMapper posDtoMapper;

    @Operation(
            summary = "Get all POS.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = PosDto.class)
                            ),
                            description = "All POS as a JSON array."
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<List<PosDto>> getAll() {
        return ResponseEntity.ok(
                posService.getAll().stream()
                        .map(posDtoMapper::fromDomain)
                        .toList()
        );
    }

    @Operation(
            summary = "Get POS by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PosDto.class)
                            ),
                            description = "The POS with the provided ID as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No POS with the provided ID could be found."
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PosDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    posDtoMapper.fromDomain(posService.getById(id))
            );
        } catch (PosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Operation(
            summary = "Get POS by name.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PosDto.class)
                            ),
                            description = "The POS with the provided name as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No POS with the provided name could be found."
                    )
            }
    )
    @GetMapping("/filter")
    public ResponseEntity<PosDto> getByName(@RequestParam("name") String name) {
        try {
            return ResponseEntity.ok(
                    posDtoMapper.fromDomain(posService.getByName(name))
            );
        } catch (PosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Operation(
            summary = "Creates a new POS.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PosDto.class)
                            ),
                            description = "The new POS as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID is not empty."
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<PosDto> create(@RequestBody @Valid PosDto posDto) {
        return upsert(posDto);
    }

    @Operation(
            summary = "Updates an existing POS.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PosDto.class)
                            ),
                            description = "The updated POS as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "IDs in path and body do not match or no POS with the provided ID could be found."
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PosDto> update(@PathVariable Long id, @RequestBody @Valid PosDto posDto) {
        if (!id.equals(posDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "POS ID in path and body do not match.");
        }

        try {
            return upsert(posDto);
        } catch (PosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    private ResponseEntity<PosDto> upsert(PosDto posDto) throws PosNotFoundException {
        return ResponseEntity.ok(
                posDtoMapper.fromDomain(
                        posService.upsert(
                                posDtoMapper.toDomain(posDto)
                        )
                )
        );
    }
}
