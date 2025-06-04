package de.unibayreuth.se.campuscoffee.domain.util;

import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidHouseNumberException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.InvalidPostalCodeException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AddressValidator {
    private static final int MIN_POSTAL_CODE = 1067;
    private static final int MAX_POSTAL_CODE = 99998;
    private static final Pattern HOUSE_NUMBER_PATTERN = Pattern.compile("\\d+[ \\-]?[a-zA-Z]?");

    public void validateAddress(Pos pos) throws InvalidPostalCodeException, InvalidHouseNumberException {
        validatePostalCode(pos.getPostalCode());
        validateHouseNumber(pos.getHouseNumber());
        // street validation: https://www.openplzapi.org/en/germany/
    }

    void validatePostalCode(int postalCode) throws InvalidPostalCodeException {
        // see https://github.com/zauberware/postal-codes-json-xml-csv/blob/master/data/DE.zip
        if (postalCode < MIN_POSTAL_CODE || postalCode > MAX_POSTAL_CODE) {
            throw new InvalidPostalCodeException("Invalid postal code: " + postalCode);
        }
    }

    void validateHouseNumber(String houseNumber) throws InvalidHouseNumberException {
        // https://de.wikipedia.org/wiki/Hausnummer#Hausnummernerg%C3%A4nzungen
        if (!HOUSE_NUMBER_PATTERN.matcher(houseNumber).matches()) {
            throw new InvalidHouseNumberException("Invalid house number: " + houseNumber);
        }
    }
}
