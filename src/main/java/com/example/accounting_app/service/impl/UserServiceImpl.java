package com.example.accounting_app.service.impl;

import com.example.accounting_app.exceptions.UserServiceExceptions;
import com.example.accounting_app.io.entity.UserEntity;
import com.example.accounting_app.repository.UserRepository;
import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.Utils;
import com.example.accounting_app.shared.dto.AddressDto;
import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
        ModelMapper modelMapper = new ModelMapper();

        for (int i = 0; i < user.getAddress().size(); i++) {
            AddressDto addressDto = user.getAddress().get(i);
            addressDto.setUserDetails(user);
            addressDto.setAddressId(utils.generateAddressId(20));
            user.getAddress().set(i, addressDto);

        }

        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException(ErrorMessages.RECORD_ALREDY_EXISTS.getErrorMessage());
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
//        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = "LE-" + utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserid(publicUserId);
        userEntity.setEmailVerificationStatus(false);

        UserEntity createdValue = userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(createdValue, UserDto.class);
        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        org.springframework.data.domain.Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageable);

        List<UserEntity> userEntities = usersPage.getContent();

        for (UserEntity tempUser : userEntities) {
            UserDto tempUserDto = modelMapper.map(tempUser, UserDto.class);
            returnValue.add(tempUserDto);
        }
        return returnValue;
    }


    @Override
    public UserDto getUserByUserId(String id) {
        ModelMapper modelMapper =new ModelMapper();

        UserEntity userEntity = userRepository.findByUserid(id);
        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto updateUser(UserDto user, String userid) {

        UserEntity userEntity = userRepository.findByUserid(userid);
        ModelMapper modelMapper = new ModelMapper();

        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setFirstname(user.getFirstname());
        userEntity.setLastname(user.getLastname());

        UserEntity updatedUserDetails = userRepository.save(userEntity);
        UserDto returnValue=modelMapper.map(updatedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findByUserid(id);
        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
