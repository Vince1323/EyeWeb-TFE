package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.AddressDto;
import com.jorami.eyeapp.model.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setBoxNumber( addressDto.getBoxNumber() );
        address.setId( addressDto.getId() );
        address.setVersion( addressDto.getVersion() );
        address.setStreet( addressDto.getStreet() );
        address.setStreetNumber( addressDto.getStreetNumber() );
        address.setZipCode( addressDto.getZipCode() );
        address.setCity( addressDto.getCity() );
        address.setCountry( addressDto.getCountry() );

        return address;
    }

    @Override
    public AddressDto toAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setBoxNumber( address.getBoxNumber() );
        addressDto.setId( address.getId() );
        addressDto.setVersion( address.getVersion() );
        addressDto.setStreet( address.getStreet() );
        addressDto.setStreetNumber( address.getStreetNumber() );
        addressDto.setZipCode( address.getZipCode() );
        addressDto.setCity( address.getCity() );
        addressDto.setCountry( address.getCountry() );

        return addressDto;
    }
}
