package com.example.accounting_app.service;

import com.example.accounting_app.shared.dto.AddressDto;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddress(String userId);
    AddressDto findByAddressId(String addressId);
}
