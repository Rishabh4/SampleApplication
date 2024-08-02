package com.rishabh.SampleApplication.util;

import com.rishabh.SampleApplication.model.dto.SignUpDto;
import com.rishabh.SampleApplication.model.dto.UserDto;
import com.rishabh.SampleApplication.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "roles", source = "roles")
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLogin(user.getLogin());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    @Mapping(target = "password", ignore = true)
    public abstract User signUpToUser(SignUpDto signUpDto);

}
