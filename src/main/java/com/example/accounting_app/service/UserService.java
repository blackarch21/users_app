package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.UserDto;
import org.springframework.stereotype.Service;

public interface UserService {
    UserDto createUser(UserDto user);
}
