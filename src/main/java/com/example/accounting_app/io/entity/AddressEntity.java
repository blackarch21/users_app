package com.example.accounting_app.io.entity;

import com.example.accounting_app.shared.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "address")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false,length = 20)
    private String addressId;
    @Column(nullable = false,length = 20)
    private String city;
    @Column(nullable = false,length = 20)
    private String country;
    @Column(nullable = false,length = 7)
    private int zipCode;
    @Column(nullable = false,length = 100)
    private String street;
    @Column(nullable = false,length = 20)
    private String type;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }
}
