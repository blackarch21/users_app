package com.example.accounting_app.ui.controller;

import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.request.UserDetailsRequestModel;
import com.example.accounting_app.ui.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping
    public Iterable<UserRest> getUsers(){
        Iterable<UserDto> userDtos = userService.findAll();
        UserRest tempUser= new UserRest();
        List<UserRest> returnValue = new ArrayList<>();
        for(UserDto temp: userDtos){
            BeanUtils.copyProperties(temp,tempUser);
            returnValue.add(tempUser);
        }
        return returnValue;
    }

    @GetMapping(path = "/{userid}")
    public UserRest getUserByUserId(@PathVariable String userid){
        UserRest returnValue = new UserRest();
        UserDto userDto= userService.getUserByUserId(userid);
        BeanUtils.copyProperties(userDto,returnValue);
        return returnValue;

    }


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);
        return returnValue;
    }
}
