package com.rishabh.SampleApplication.service;

import com.rishabh.SampleApplication.util.Constants;
import com.rishabh.SampleApplication.model.dto.CredentialsDto;
import com.rishabh.SampleApplication.model.dto.SignUpDto;
import com.rishabh.SampleApplication.model.dto.UserDto;
import com.rishabh.SampleApplication.model.entity.Role;
import com.rishabh.SampleApplication.model.entity.User;
import com.rishabh.SampleApplication.exception.CustomException;
import com.rishabh.SampleApplication.util.UserMapper;
import com.rishabh.SampleApplication.repository.RoleRepository;
import com.rishabh.SampleApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto signIn(CredentialsDto credentialsDto) throws CustomException {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new CustomException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new CustomException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) throws CustomException {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

        if (optionalUser.isPresent()) {
            throw new CustomException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));
        user.setEnabled(Constants.ENABLED);

        Role role = roleRepository.findByRoleName("USER");
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) throws CustomException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new CustomException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

}
