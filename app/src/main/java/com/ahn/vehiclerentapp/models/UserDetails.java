package com.ahn.vehiclerentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDetails implements Serializable {

    private String name;
    private String nic;
    private String property_name;
    private String phone_number;
    private String br;
    private String active_town;
    private String address;
    private String email;
    private String image;
    private ArrayList<UserPostsDataList> userPostsDataLists;

    public UserDetails(String name, String nic, String property_name, String phone_number, String br, String active_town, String address, String email, String image, ArrayList<UserPostsDataList> userPostsDataLists) {
        this.name = name;
        this.nic = nic;
        this.property_name = property_name;
        this.phone_number = phone_number;
        this.br = br;
        this.active_town = active_town;
        this.address = address;
        this.email = email;
        this.image = image;
        this.userPostsDataLists = userPostsDataLists;
    }

    public UserDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getActive_town() {
        return active_town;
    }

    public void setActive_town(String active_town) {
        this.active_town = active_town;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<UserPostsDataList> getUserPostsDataLists() {
        return userPostsDataLists;
    }

    public void setUserPostsDataLists(ArrayList<UserPostsDataList> userPostsDataLists) {
        this.userPostsDataLists = userPostsDataLists;
    }
}

