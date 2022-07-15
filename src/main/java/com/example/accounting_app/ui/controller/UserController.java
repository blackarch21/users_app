package com.example.accounting_app.ui.controller;

import com.example.accounting_app.exceptions.UserServiceExceptions;
import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.request.UserDetailsRequestModel;
import com.example.accounting_app.ui.response.ErrorMessages;
import com.example.accounting_app.ui.response.OperationStatusModel;
import com.example.accounting_app.ui.response.RequestOperationStatus;
import com.example.accounting_app.ui.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit ) {

        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> userDtos = userService.getUsers(page,limit);
        for (UserDto user : userDtos) {
            UserRest tempUser = new UserRest();
            BeanUtils.copyProperties(user, tempUser);
            returnValue.add(tempUser);
        }
        return returnValue;
    }

    @GetMapping(path = "/{userid}")
    public UserRest getUserByUserId(@PathVariable String userid) {
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(userid);
        BeanUtils.copyProperties(userDto, returnValue);
        return returnValue;

    }


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstname().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);
        return returnValue;
    }

    @PutMapping(path = "{userid}")
    public UserRest updateUser(@PathVariable String userid, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstname().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.updateUser(userDto, userid);
        BeanUtils.copyProperties(createdUser, returnValue);
        return returnValue;
    }

    @DeleteMapping(path = "/{userid}")
    public OperationStatusModel deleteUser(@PathVariable String userid){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(userid);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;

    }
}
