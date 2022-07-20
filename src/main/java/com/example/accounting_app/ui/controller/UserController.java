package com.example.accounting_app.ui.controller;

import com.example.accounting_app.exceptions.UserServiceExceptions;
import com.example.accounting_app.service.AddressService;
import com.example.accounting_app.service.UserService;
import com.example.accounting_app.shared.dto.AddressDto;
import com.example.accounting_app.shared.dto.UserDto;
import com.example.accounting_app.ui.RequestOperationName;
import com.example.accounting_app.ui.request.UserDetailsRequestModel;
import com.example.accounting_app.ui.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;


    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "25") int limit) {

        ModelMapper modelMapper = new ModelMapper();
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> userDtos = userService.getUsers(page, limit);
        for (UserDto user : userDtos) {
            UserRest tempUser = modelMapper.map(user, UserRest.class);
            returnValue.add(tempUser);
        }
        return returnValue;
    }

    @GetMapping(path = "/{userid}")
    public UserRest getUserByUserId(@PathVariable String userid) {
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = userService.getUserByUserId(userid);
        UserRest returnValue = modelMapper.map(userDto, UserRest.class);
        return returnValue;

    }


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();

        if (userDetails.getFirstname().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
//        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
        return returnValue;
    }

    @PutMapping(path = "/{userid}")
    public UserRest updateUser(@PathVariable String userid, @RequestBody UserDetailsRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();

        if (userDetails.getFirstname().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.updateUser(userDto, userid);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
        return returnValue;
    }

    @DeleteMapping(path = "/{userid}")
    public OperationStatusModel deleteUser(@PathVariable String userid) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(userid);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;

    }

    @DeleteMapping(path = "{userid}/address/{addressId}")
    public OperationStatusModel deleteAddress(@PathVariable String addressId) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        addressService.deleteAddress(addressId);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;

    }

    @GetMapping(path = "/{userid}/address")
    public CollectionModel<AddressRest> getUserAddress(@PathVariable String userid) {
        ModelMapper modelMapper = new ModelMapper();
        List<AddressDto> addressDto = addressService.getAddress(userid);
        List<AddressRest> returnValue = new ArrayList<>();

        if (addressDto != null && !addressDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressRest>>() {
            }.getType();
            returnValue = modelMapper.map(addressDto, listType);
        }

        for(AddressRest addressRest:returnValue){
            Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                    .findByAddressId(userid ,addressRest.getAddressId())).withSelfRel();
            addressRest.add(addressLink);
        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userid).withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userid))
                .withSelfRel();

        return CollectionModel.of(returnValue, userLink,selfLink);

    }

    @GetMapping(path = "/{userid}/address/{addressId}")
    public EntityModel<AddressRest> findByAddressId(@PathVariable String userid, @PathVariable String addressId) {
        AddressDto addressDto = addressService.findByAddressId(addressId);
        ModelMapper modelMapper = new ModelMapper();
        AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userid).withRel("user");
        Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userid))
                .withRel("address");

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).findByAddressId(userid,addressId))
                .withSelfRel();

        return EntityModel.of(returnValue, Arrays.asList(userLink, addressLink, selfLink));

    }

}
