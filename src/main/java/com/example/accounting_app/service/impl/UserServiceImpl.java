package com.example.accounting_app.service.impl;

import com.example.accounting_app.io.entity.UserEntity;
import com.example.accounting_app.repository.UserRepository;
import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.Utils;
import com.example.accounting_app.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Alredy exists");
        UserDto returnValue = new UserDto();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId =utils.generateUserId(30);
        userEntity.setEncryptedPassword("test");
        userEntity.setUserid("LE-"+publicUserId);
        userEntity.setEmailVerificationStatus(false);

        UserEntity createdValue=userRepository.save(userEntity);
        BeanUtils.copyProperties(createdValue, returnValue);
        return returnValue;
    }
}
