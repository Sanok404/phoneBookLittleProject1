package com.company;

import java.io.Serializable;
import java.util.ArrayList;


public class Contact implements Serializable, Comparable<Contact> {
    String firstName;
    String lastName;
    ArrayList<String> phoneNumbers;
    String address;
    String date;

    public Contact() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return  "First Name = " + firstName + '\n' +
                "Last Name = " + lastName + '\n' +
                "Phone Number(s) = " + phoneNumbers + '\n' +
                "Address = " + address + '\n' +
                "Date created/modified = " + date +'\n';
    }

    @Override
    public int compareTo(Contact o) {
        return 0;
    }
}
