package com.example.accounting_app.ui.response;

import java.util.List;

public class UserRest {
    private String userid;
    private String firstname;
    private String lastname;
    private String email;
    private List<AddressRest> address;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressRest> getAddress() {
        return address;
    }

    public void setAddress(List<AddressRest> address) {
        this.address = address;
    }
}
