package com.jordinavines.applicationtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class User implements Parcelable {

    int _id;

    Name name;

    String about;

    String avatar;

    String address;

    String phone;

    String email;

    String dob;

    String department;

    int[] subordinates;

    ArrayList<User> subordinates_list;



    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }


    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void createtName(String names) {
//        String[] array=  Arrays.asList(names).toArray(new String[2]);
//        this.name = new Name();
//        name.setFirst();
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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User" +
                "  name: '" + name.toString() + '\'' +
                ", avatar: '" + avatar + '\'' +
                ", address: '" + address + '\'' +
                ", email: '" + email + '\'' +
                ", department:'" + department;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_id);
        out.writeString(about);
        out.writeString(avatar);
        out.writeString(address);
        out.writeString(email);
        out.writeString(dob);
        out.writeString(department);
        out.writeString(phone);
        out.writeIntArray(subordinates);
        out.writeParcelable(name, flags);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        _id= in.readInt();
        about= in.readString();
        avatar= in.readString();
        address= in.readString();
        email= in.readString();
        dob= in.readString();
        department= in.readString();
        phone= in.readString();
        subordinates= in.createIntArray();
        name= (Name)in.readParcelable(Name.class.getClassLoader());
    }
}
