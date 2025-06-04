package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.AddressDto;
import com.jorami.eyeapp.dto.UserDto;
import com.jorami.eyeapp.model.Address;
import com.jorami.eyeapp.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        user.address( addressDtoToAddress( userDto.getAddress() ) );
        user.id( userDto.getId() );
        user.version( userDto.getVersion() );
        user.firstname( userDto.getFirstname() );
        user.lastname( userDto.getLastname() );
        user.birthDate( userDto.getBirthDate() );
        user.phone( userDto.getPhone() );
        user.email( userDto.getEmail() );
        user.verified( userDto.isVerified() );
        user.validEmail( userDto.isValidEmail() );
        user.hasReadTermsAndConditions( userDto.isHasReadTermsAndConditions() );
        user.role( roleMapper.toRole( userDto.getRole() ) );

        return user.build();
    }

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setAddress( addressToAddressDto( user.getAddress() ) );
        userDto.setBirthDate( user.getBirthDate() );
        if ( user.getId() != null ) {
            userDto.setId( user.getId() );
        }
        if ( user.getVersion() != null ) {
            userDto.setVersion( user.getVersion() );
        }
        userDto.setFirstname( user.getFirstname() );
        userDto.setLastname( user.getLastname() );
        userDto.setEmail( user.getEmail() );
        userDto.setPhone( user.getPhone() );
        userDto.setRole( roleMapper.toRoleDto( user.getRole() ) );
        userDto.setHasReadTermsAndConditions( user.isHasReadTermsAndConditions() );
        userDto.setVerified( user.isVerified() );
        userDto.setValidEmail( user.isValidEmail() );

        return userDto;
    }

    @Override
    public List<User> toUsers(List<UserDto> userDtos) {
        if ( userDtos == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userDtos.size() );
        for ( UserDto userDto : userDtos ) {
            list.add( toUser( userDto ) );
        }

        return list;
    }

    @Override
    public List<UserDto> toUserDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toUserDto( user ) );
        }

        return list;
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
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

    protected AddressDto addressToAddressDto(Address address) {
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
