package com.rishabh.SampleApplication.controller;

import com.rishabh.SampleApplication.model.dto.CredentialsDto;
import com.rishabh.SampleApplication.model.dto.SignUpDto;
import com.rishabh.SampleApplication.model.dto.UserDto;
import com.rishabh.SampleApplication.exception.CustomException;
import com.rishabh.SampleApplication.service.AuthService;
import com.rishabh.SampleApplication.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto user) throws CustomException, UnsupportedEncodingException {
        UserDto createdUser = authService.register(user);
        createdUser.setToken(jwtTokenUtil.generateToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@RequestBody CredentialsDto credentialsDto) throws CustomException, UnsupportedEncodingException {
        UserDto userDto = authService.signIn(credentialsDto);
        userDto.setToken(jwtTokenUtil.generateToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}