package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
}
