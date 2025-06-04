package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidPostalCodeException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.tests.TestFixtures;
import de.unibayreuth.se.campuscoffee.domain.impl.PosServiceImpl;
import de.unibayreuth.se.campuscoffee.domain.util.AddressValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PosServiceTest {

    @Mock
    private PosDataService posDataService;

    @Mock
    private AddressValidator addressValidator;

    @InjectMocks
    private PosServiceImpl posService;

    @Test
    void getAllPosRetrievesExpectedPos() {
        // given
        List<Pos> testFixtures = TestFixtures.getPosList();
        when(posDataService.getAll()).thenReturn(testFixtures);

        // when
        List<Pos> retrievedPos = posService.getAll();

        // then
        verify(posDataService).getAll();
        assertEquals(testFixtures.size(), retrievedPos.size());
        assertThat(retrievedPos)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(testFixtures);
    }

    @Test
    void getPosByIdNotFound() {
        // given
        when(posDataService.getById(anyLong())).thenThrow(new PosNotFoundException("Pos not found."));

        // when, then
        assertThrows(PosNotFoundException.class, () -> posService.getById(anyLong()));
        verify(posDataService).getById(anyLong());

    }

    @Test
    void getPosByIdFound() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        when(posDataService.getById(pos.getId())).thenReturn(pos);

        // when
        Pos retrievedPos = posService.getById(pos.getId());

        // then
        verify(posDataService).getById(pos.getId());
        assertThat(retrievedPos)
                .usingRecursiveComparison()
                .isEqualTo(pos);
    }

    @Test
    void upsertPosNotFound() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        when(posDataService.getById(pos.getId())).thenThrow(new PosNotFoundException("Pos not found."));

        // when, then
        assertThrows(PosNotFoundException.class, () -> posService.upsert(pos));
        verify(posDataService).getById(pos.getId());
    }

    @Test
    void upsertPosInvalidPostalCode() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        when(posDataService.getById(pos.getId())).thenReturn(pos);
        doThrow(new InvalidPostalCodeException("Postal code invalid.")).when(addressValidator).validateAddress(pos);

        // when, then
        assertThrows(InvalidPostalCodeException.class, () -> posService.upsert(pos));
        verify(posDataService).getById(pos.getId());
        verify(addressValidator).validateAddress(pos);
    }

    @Test
    void upsertNewPos() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        pos.setId(null);

        // when, then
        posService.upsert(pos);

        verify(addressValidator).validateAddress(pos);
    }

    @Test
    void upsertPosSuccess() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        when(posDataService.getById(pos.getId())).thenReturn(pos);

        // when
        posService.upsert(pos);

        // then
        verify(posDataService).getById(pos.getId());
        verify(addressValidator).validateAddress(pos);
    }

    @Test
    void getPosByName() {
        // given
        Pos pos = TestFixtures.getPosList().getFirst();
        when(posDataService.getByName(pos.getName())).thenReturn(pos);

        // when
        Pos retrievedPos = posService.getByName(pos.getName());

        // then
        assertThat(retrievedPos)
                .usingRecursiveComparison()
                .isEqualTo(pos);
        verify(posDataService).getByName(pos.getName());
    }
}

