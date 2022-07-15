package com.example.accounting_app.service.impl;

import com.example.accounting_app.io.entity.UserEntity;
import com.example.accounting_app.repository.UserRepository;
import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.Utils;
import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException(ErrorMessages.RECORD_ALREDY_EXISTS.getErrorMessage());
        UserDto returnValue = new UserDto();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId ="LE-"+ utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserid(publicUserId);
        userEntity.setEmailVerificationStatus(false);

        UserEntity createdValue=userRepository.save(userEntity);
        BeanUtils.copyProperties(createdValue, returnValue);
        return returnValue;
    }

    @Override
    public Iterable<UserDto> findAll() {
        Iterable<UserEntity> entities =userRepository.findAll();
        List<UserDto> returnValue = new ArrayList<>();
        UserDto tempUsers = new UserDto();
        for(UserEntity temp: entities){
            BeanUtils.copyProperties(temp,tempUsers);
            returnValue.add(tempUsers);
        }

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String id) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.findByUserid(id);
        if(userEntity == null)throw new UsernameNotFoundException(id);
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
