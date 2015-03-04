package com.jordinavines.applicationtest.model;

import java.util.Arrays;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class User {

    int _id;

    Name name;

    String about;

    String avatar;

    String address;

    String email;

    String dob;

    String department;

    int[] subordinates;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int[] getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(int[] subordinates) {
        this.subordinates = subordinates;
    }

    public String getCompletName(){
        return name.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + _id +
                ", name='" + name.toString() + '\'' +
                ", about='" + about + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", department='" + department + '\'' +
                ", subordinates=" + Arrays.toString(subordinates) +
                '}';
    }
}
