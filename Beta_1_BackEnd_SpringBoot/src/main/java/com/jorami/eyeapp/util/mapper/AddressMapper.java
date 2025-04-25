package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.AddressDto;
import com.jorami.eyeapp.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(source = "boxNumber", target = "boxNumber")
    Address toAddress(AddressDto addressDto);

    @Mapping(source = "boxNumber", target = "boxNumber")
    AddressDto toAddressDto(Address address);
}
