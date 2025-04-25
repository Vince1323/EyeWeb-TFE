package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.Address;
import com.jorami.eyeapp.repository.AddressRepository;
import com.jorami.eyeapp.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }
}
