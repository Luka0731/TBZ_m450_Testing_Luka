package ch.tbz.m450.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;



class AddressTest {
    private Address address;
    private Date registrationDate;

    @BeforeEach
    void setUp() {
        registrationDate = new Date(1700000000000L);
        address = new Address(1, "Anna", "Muster", "0791234567", registrationDate);
    }



    @Test
    void createAddressWithAllArgsConstructor() {
        assertEquals(1, address.getId());
        assertEquals("Anna", address.getFirstname());
        assertEquals("Muster", address.getLastname());
        assertEquals("0791234567", address.getPhonenumber());
        assertEquals(registrationDate, address.getRegistrationDate());
    }

    @Test
    void createAddressWithNoArgsConstructor() {
        Address emptyAddress = new Address();

        assertEquals(0, emptyAddress.getId());
        assertNull(emptyAddress.getFirstname());
        assertNull(emptyAddress.getLastname());
        assertNull(emptyAddress.getPhonenumber());
        assertNull(emptyAddress.getRegistrationDate());
    }

    @Test
    void settersChangeValues() {
        Date newDate = new Date(1800000000000L);

        address.setId(2);
        address.setFirstname("Peter");
        address.setLastname("Meier");
        address.setPhonenumber("0761112233");
        address.setRegistrationDate(newDate);

        assertEquals(2, address.getId());
        assertEquals("Peter", address.getFirstname());
        assertEquals("Meier", address.getLastname());
        assertEquals("0761112233", address.getPhonenumber());
        assertEquals(newDate, address.getRegistrationDate());
    }
}