package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.IContact;

import java.io.Serializable;

public class ContactInputForm implements ValidForm,IContact,Serializable {

    private String firstName;
    private String lastName;
    private String telephone;
    private String mobile;
    private String fax;
    private String email;
    private AddressInputForm  address;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressInputForm getAddress() {
        return address;
    }

    public void setAddress(AddressInputForm address) {
        this.address = address;
    }
}
