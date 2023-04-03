package com.ahn.vehiclerentapp.models.posts;

import android.util.Log;

import java.util.ArrayList;

public class PostsDataList {

    private String created_user_id;
    private String status;
    private String nearest_town;
    private String tour_type;
    private String no_nights;
    private String no_passengers;
    private String start_date;
    private String start_time;
    private String start_location;
    private String end_location;
    private ArrayList<String> night_destination;
    private String approved_bid;
    private ArrayList<DriverData> driverData;
    private String vehicle_type;
    private String position;

    public PostsDataList() {
    }

    public PostsDataList(String created_user_id, String status, String nearest_town, String tour_type, String no_nights, String no_passengers, String start_date, String start_time, String start_location, String end_location, ArrayList<String> night_destination, String approved_bid, ArrayList<DriverData> driverData, String vehicle_type, String position) {
        this.created_user_id = created_user_id;
        this.status = status;
        this.nearest_town = nearest_town;
        this.tour_type = tour_type;
        this.no_nights = no_nights;
        this.no_passengers = no_passengers;
        this.start_date = start_date;
        this.start_time = start_time;
        this.start_location = start_location;
        this.end_location = end_location;
        this.night_destination = night_destination;
        this.approved_bid = approved_bid;
        this.driverData = driverData;
        this.vehicle_type = vehicle_type;
        this.position = position;
    }

    public String getCreated_user_id() {
        return created_user_id;
    }

    public void setCreated_user_id(String created_user_id) {
        this.created_user_id = created_user_id;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNearest_town() {
        return nearest_town;
    }

    public void setNearest_town(String nearest_town) {
        this.nearest_town = nearest_town;
    }

    public String getTour_type() {
        return tour_type;
    }

    public void setTour_type(String tour_type) {
        this.tour_type = tour_type;
    }

    public String getNo_nights() {
        return no_nights;
    }

    public void setNo_nights(String no_nights) {
        this.no_nights = no_nights;
    }

    public String getNo_passengers() {
        return no_passengers;
    }

    public void setNo_passengers(String no_passengers) {
        this.no_passengers = no_passengers;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
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

    public ArrayList<String> getNight_destination() {
        return night_destination;
    }

    public void setNight_destination(ArrayList<String> night_destination) {
        this.night_destination = night_destination;
    }

    public String getApproved_bid() {
        return approved_bid;
    }

    public void setApproved_bid(String approved_bid) {
        this.approved_bid = approved_bid;
    }

    public ArrayList<DriverData> getDriverData() {
        return driverData;
    }

    public void setDriverData(ArrayList<DriverData> driverData) {
        this.driverData = driverData;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
