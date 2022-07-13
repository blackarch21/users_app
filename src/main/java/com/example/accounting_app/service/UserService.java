package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.response.UserRest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    Iterable<UserDto> findAll();
    UserDto getUserByUserId(String id);
}
