package com.rishabh.SampleApplication.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rishabh.SampleApplication.model.entity.Role;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;

    @JsonIgnore
    private String roles;

    public void setRoles(Set<Role> roles) {
        this.roles = roles.stream().map(role -> role.getRoleName()).collect(Collectors.joining(", "));
    }
}
