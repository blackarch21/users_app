package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    List<UserDto> getUsers(int page, int limit);
    UserDto getUserByUserId(String id);
    UserDto updateUser(UserDto user,String userid);
    void deleteUser(String id);
}
