package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ch.tbz.m450.util.SortField.*;



class AddressComparatorTest {
    private Comparator<Address> comparator;
    private Address anna;
    private Address peter;
    private Address lisa;

    @BeforeEach
    void setUp() {
        comparator = new AddressComparator();
        anna = new Address(3, "Anna", "Muster", "0791234567", new Date(1700000000000L));
        peter = new Address(1, "Peter", "Meier", "0761112233", new Date(1600000000000L));
        lisa = new Address(2, "Lisa", "Muster", "0445556677", new Date(1800000000000L));
    }



    @Test
    void compareSameAddressReturnsZero() {
        assertEquals(0, comparator.compare(anna, anna));
    }

    @Test
    void compareByLastnameByDefault() {
        assertTrue(comparator.compare(peter, anna) < 0);
        assertTrue(comparator.compare(anna, peter) > 0);
    }

    @Test
    void compareByFirstnameWhenLastnameIsEqual() {
        assertTrue(comparator.compare(anna, lisa) < 0);
        assertTrue(comparator.compare(lisa, anna) > 0);
    }

    @Test
    void compareIgnoresCase() {
        Address lowerCase = new Address(4, "anna", "muster", "0791234567", null);

        assertEquals(0, comparator.compare(anna, lowerCase));
    }

    @Test
    void nullValuesAreSortedLast() {
        Address noLastname = new Address(5, "Max", null, null, null);

        assertTrue(comparator.compare(anna, noLastname) < 0);
        assertTrue(comparator.compare(noLastname, anna) > 0);
    }

    @Test
    void sortListByDefault() {
        List<Address> addresses = new ArrayList<>(List.of(lisa, anna, peter));

        addresses.sort(comparator);

        assertEquals(List.of(peter, anna, lisa), addresses);
    }

    @Test
    void compareById() {
        comparator = new AddressComparator(ID);

        assertTrue(comparator.compare(peter, anna) < 0);
        assertTrue(comparator.compare(anna, lisa) > 0);
    }

    @Test
    void compareByFirstname() {
        comparator = new AddressComparator(FIRSTNAME);

        assertTrue(comparator.compare(anna, peter) < 0);
        assertTrue(comparator.compare(peter, lisa) > 0);
    }

    @Test
    void compareByPhonenumber() {
        comparator = new AddressComparator(PHONENUMBER);

        assertTrue(comparator.compare(lisa, peter) < 0);
        assertTrue(comparator.compare(anna, peter) > 0);
    }

    @Test
    void compareByRegistrationDate() {
        comparator = new AddressComparator(REGISTRATION_DATE);

        assertTrue(comparator.compare(peter, anna) < 0);
        assertTrue(comparator.compare(lisa, anna) > 0);
    }

    @Test
    void sortListByRegistrationDate() {
        comparator = new AddressComparator(REGISTRATION_DATE);
        List<Address> addresses = new ArrayList<>(List.of(lisa, anna, peter));

        addresses.sort(comparator);

        assertEquals(List.of(peter, anna, lisa), addresses);
    }
}