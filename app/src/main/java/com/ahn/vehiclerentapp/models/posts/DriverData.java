package com.ahn.vehiclerentapp.models.posts;

import java.io.Serializable;
import java.util.ArrayList;

public class DriverData implements Serializable {

    private String name;
    private String phone_number;
    private String nearest_town;
    private String active_town;
    private String address;
    private String driver_image;
    private String licence_image;
    private String bit;
    private String driver_id;
    private ArrayList<String> allocated_time;
    private String driver_fcm_token;

    public DriverData() {
    }

    /*public DriverData(String name, String phone_number, String nearest_town, String active_town, String address, String driver_image, String licence_image, String bit, String driver_id, ArrayList<String> allocated_time) {
        this.name = name;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.bit = bit;
        this.driver_id = driver_id;
        this.allocated_time = allocated_time;
    }*/

    public DriverData(String name, String phone_number, String nearest_town, String active_town, String address, String driver_image, String licence_image, String bit, String driver_id, ArrayList<String> allocated_time, String driver_fcm_token) {
        this.name = name;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.bit = bit;
        this.driver_id = driver_id;
        this.allocated_time = allocated_time;
        this.driver_fcm_token = driver_fcm_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNearest_town() {
        return nearest_town;
    }

    public void setNearest_town(String nearest_town) {
        this.nearest_town = nearest_town;
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

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public String getLicence_image() {
        return licence_image;
    }

    public void setLicence_image(String licence_image) {
        this.licence_image = licence_image;
    }

    public String getBit() {
        return bit;
    }

    public void setBit(String bit) {
        this.bit = bit;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public ArrayList<String> getAllocated_time() {
        return allocated_time;
    }

    public void setAllocated_time(ArrayList<String> allocated_time) {
        this.allocated_time = allocated_time;
    }

    public String getDriver_fcm_token() {
        return driver_fcm_token;
    }

    public void setDriver_fcm_token(String driver_fcm_token) {
        this.driver_fcm_token = driver_fcm_token;
    }
}
