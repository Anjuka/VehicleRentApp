package com.ahn.vehiclerentapp.models.driver;

import java.io.Serializable;
import java.util.ArrayList;

public class DriverPostsDataList implements Serializable {

    private String type;
    private String no_passengers;
    private String vehicle_type;
    private String date;
    private String time;
    private String start_location;
    private String end_location;
    private String no_days;
    private ArrayList<String> night_list;
    private String host_contact_number;
    private String near_town;
    private String status;

    public DriverPostsDataList(String type, String no_passengers, String vehicle_type, String date, String time, String start_location, String end_location, String no_days, ArrayList<String> night_list, String host_contact_number, String near_town, String status) {
        this.type = type;
        this.no_passengers = no_passengers;
        this.vehicle_type = vehicle_type;
        this.date = date;
        this.time = time;
        this.start_location = start_location;
        this.end_location = end_location;
        this.no_days = no_days;
        this.night_list = night_list;
        this.host_contact_number = host_contact_number;
        this.near_town = near_town;
        this.status = status;
    }

    public DriverPostsDataList() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNo_passengers() {
        return no_passengers;
    }

    public void setNo_passengers(String no_passengers) {
        this.no_passengers = no_passengers;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public String getNo_days() {
        return no_days;
    }

    public void setNo_days(String no_days) {
        this.no_days = no_days;
    }

    public ArrayList<String> getNight_list() {
        return night_list;
    }

    public void setNight_list(ArrayList<String> night_list) {
        this.night_list = night_list;
    }

    public String getHost_contact_number() {
        return host_contact_number;
    }

    public void setHost_contact_number(String host_contact_number) {
        this.host_contact_number = host_contact_number;
    }

    public String getNear_town() {
        return near_town;
    }

    public void setNear_town(String near_town) {
        this.near_town = near_town;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

