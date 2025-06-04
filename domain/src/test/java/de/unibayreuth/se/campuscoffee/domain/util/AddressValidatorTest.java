package de.unibayreuth.se.campuscoffee.domain.util;

import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidPostalCodeException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AddressValidatorTest {
    private static final int POSTAL_CODE_TOO_SMALL = 123;

    private AddressValidator addressValidator;

    @BeforeEach
    void beforeEach() {
        addressValidator = new AddressValidator();
    }

    @Test
    void testValidateAddress_postalCodeTooSmall() {
        Pos pos = mock(Pos.class);
        when(pos.getPostalCode()).thenReturn(POSTAL_CODE_TOO_SMALL);
        assertThrows(InvalidPostalCodeException.class, () -> addressValidator.validateAddress(pos));
        //verify(pos, times(1)).getPostalCode(); // equivalent to below statement, kept for demo purposes
        verify(pos).getPostalCode();
    }

    @Test
    void testValidatePostalCode_tooSmall() {
        assertThrows(InvalidPostalCodeException.class, () -> addressValidator.validatePostalCode(POSTAL_CODE_TOO_SMALL));
    }
}
