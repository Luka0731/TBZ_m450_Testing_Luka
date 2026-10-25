package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class AddressControllerTest {
    @Mock private AddressService addressService;
    @InjectMocks private AddressController addressController;
    private Address anna;
    private Address peter;

    @BeforeEach
    void setUp() {
        anna = new Address(1, "Anna", "Muster", "0791234567", new Date());
        peter = new Address(2, "Peter", "Meier", "0761112233", new Date());
    }



    @Test
    void createAddressReturns201() {
        when(addressService.save(anna)).thenReturn(anna);

        ResponseEntity<Address> response = addressController.createAddress(anna);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(anna, response.getBody());
        verify(addressService, times(1)).save(anna);
    }

    @Test
    void getAddressesReturns200() {
        when(addressService.getAll()).thenReturn(List.of(peter, anna));

        ResponseEntity<List<Address>> response = addressController.getAddresses();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(peter, anna), response.getBody());
        verify(addressService, times(1)).getAll();
    }

    @Test
    void getAddressReturns200() {
        when(addressService.getAddress(1)).thenReturn(Optional.of(anna));

        ResponseEntity<Address> response = addressController.getAddress(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(anna, response.getBody());
        verify(addressService, times(1)).getAddress(1);
    }

    @Test
    void getAddressReturns404() {
        when(addressService.getAddress(99)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = addressController.getAddress(99);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(addressService, times(1)).getAddress(99);
    }
}