package com.ahn.vehiclerentapp.models.user;

import java.util.ArrayList;

public class DriverUTripDetails {

    private String driver_name;
    private String bid;
    private String vehicle_type;
    private String vehicle_number;
    private ArrayList<String> vehicle_img_list;
    private String driver_contact_number;
    private String near_town;

    public DriverUTripDetails() {
    }

    public DriverUTripDetails(String driver_name, String bid, String vehicle_type, String vehicle_number, ArrayList<String> vehicle_img_list, String driver_contact_number, String near_town) {
        this.driver_name = driver_name;
        this.bid = bid;
        this.vehicle_type = vehicle_type;
        this.vehicle_number = vehicle_number;
        this.vehicle_img_list = vehicle_img_list;
        this.driver_contact_number = driver_contact_number;
        this.near_town = near_town;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public ArrayList<String> getVehicle_img_list() {
        return vehicle_img_list;
    }

    public void setVehicle_img_list(ArrayList<String> vehicle_img_list) {
        this.vehicle_img_list = vehicle_img_list;
    }

    public String getDriver_contact_number() {
        return driver_contact_number;
    }

    public void setDriver_contact_number(String driver_contact_number) {
        this.driver_contact_number = driver_contact_number;
    }

    public String getNear_town() {
        return near_town;
    }

    public void setNear_town(String near_town) {
        this.near_town = near_town;
    }
}
