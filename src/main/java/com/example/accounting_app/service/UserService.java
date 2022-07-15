package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    Iterable<UserDto> findAll();
    UserDto getUserByUserId(String id);
}
