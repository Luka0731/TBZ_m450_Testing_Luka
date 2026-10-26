package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock private AddressRepository addressRepository;
    @InjectMocks private AddressService addressService;
    private Address anna;
    private Address peter;

    @BeforeEach
    void setUp() {
        anna = new Address(1, "Anna", "Muster", "0791234567", new Date());
        peter = new Address(2, "Peter", "Meier", "0761112233", new Date());
    }



    @Test
    void saveAddress() {
        when(addressRepository.save(anna)).thenReturn(anna);

        Address result = addressService.save(anna);

        assertEquals(anna, result);
        verify(addressRepository, times(1)).save(anna);
    }

    @Test
    void getAllReturnsSortedAddresses() {
        when(addressRepository.findAll()).thenReturn(List.of(anna, peter));

        List<Address> result = addressService.getAll();

        assertEquals(List.of(peter, anna), result);
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void getAllReturnsEmptyList() {
        when(addressRepository.findAll()).thenReturn(List.of());

        List<Address> result = addressService.getAll();

        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void getAddressById() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(anna));

        Optional<Address> result = addressService.getAddress(1);

        assertTrue(result.isPresent());
        assertEquals(anna, result.get());
        verify(addressRepository, times(1)).findById(1);
    }

    @Test
    void getAddressByIdNotFound() {
        when(addressRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Address> result = addressService.getAddress(99);

        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findById(99);
    }
}