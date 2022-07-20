package com.example.accounting_app.service.impl;

import com.example.accounting_app.io.entity.AddressEntity;
import com.example.accounting_app.io.entity.UserEntity;
import com.example.accounting_app.repository.AddressRepository;
import com.example.accounting_app.repository.UserRepository;
import com.example.accounting_app.service.AddressService;
import com.example.accounting_app.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddress(String userId) {
        ModelMapper modelMapper = new ModelMapper();
        List<AddressDto> returnValue= new ArrayList<>();
        UserEntity userEntity = userRepository.findByUserid(userId);
        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(userEntity);
        for (AddressEntity address : addressEntities){
            returnValue.add(modelMapper.map(address, AddressDto.class));
        }

        return returnValue;
    }

    @Override
    public AddressDto findByAddressId(String addressId) {
        AddressDto returnValue= null;
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if(addressEntity != null) {
            return new ModelMapper().map(addressEntity, AddressDto.class);
        }
        return returnValue;
    }
}
