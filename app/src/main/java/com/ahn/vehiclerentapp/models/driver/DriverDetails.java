package com.ahn.vehiclerentapp.models.driver;

import java.io.Serializable;
import java.util.ArrayList;

public class DriverDetails implements Serializable {

    private String name;
    private String nic;
    private String phone_number;
    private String nearest_town;
    private String active_town;
    private String address;
    private String email;
    private String driver_image;
    private String licence_image;
    private String job_type;
    private ArrayList<DriverPostsDataList> driverPostsDataLists;
    //private VehicleDetails vehicleDetails;
    private String user_type;

    private String vehicle_type;
    private String img_1;
    private String img_2;
    private String img_3;
    private String img_4;
    private String vehicle_number;
    private String fcm_token;
    private ArrayList<String> allocated_time;

    public DriverDetails(String name, String nic, String phone_number, String nearest_town, String active_town, String address, String email, String driver_image, String licence_image, String job_type, ArrayList<DriverPostsDataList> driverPostsDataLists, String user_type, String vehicle_type, String img_1, String img_2, String img_3, String img_4, String vehicle_number) {
        this.name = name;
        this.nic = nic;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.email = email;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.job_type = job_type;
        this.driverPostsDataLists = driverPostsDataLists;
        this.user_type = user_type;
        this.vehicle_type = vehicle_type;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
        this.vehicle_number = vehicle_number;
    }

    public DriverDetails(String name, String nic, String phone_number, String nearest_town, String active_town, String address, String email, String driver_image, String licence_image, String job_type, ArrayList<DriverPostsDataList> driverPostsDataLists, String user_type, String vehicle_type, String img_1, String img_2, String img_3, String img_4, String vehicle_number, String fcm_token) {
        this.name = name;
        this.nic = nic;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.email = email;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.job_type = job_type;
        this.driverPostsDataLists = driverPostsDataLists;
        this.user_type = user_type;
        this.vehicle_type = vehicle_type;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
        this.vehicle_number = vehicle_number;
        this.fcm_token = fcm_token;
    }

    public DriverDetails(String name, String nic, String phone_number, String nearest_town, String active_town, String address, String email, String driver_image, String licence_image, String job_type, ArrayList<DriverPostsDataList> driverPostsDataLists, String user_type, String vehicle_type, String img_1, String img_2, String img_3, String img_4, String vehicle_number, String fcm_token, ArrayList<String> allocated_time) {
        this.name = name;
        this.nic = nic;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.email = email;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.job_type = job_type;
        this.driverPostsDataLists = driverPostsDataLists;
        this.user_type = user_type;
        this.vehicle_type = vehicle_type;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
        this.vehicle_number = vehicle_number;
        this.fcm_token = fcm_token;
        this.allocated_time = allocated_time;
    }

    public DriverDetails() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public ArrayList<DriverPostsDataList> getDriverPostsDataLists() {
        return driverPostsDataLists;
    }

    public void setDriverPostsDataLists(ArrayList<DriverPostsDataList> driverPostsDataLists) {
        this.driverPostsDataLists = driverPostsDataLists;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public void setImg_3(String img_3) {
        this.img_3 = img_3;
    }

    public String getImg_4() {
        return img_4;
    }

    public void setImg_4(String img_4) {
        this.img_4 = img_4;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public ArrayList<String> getAllocated_time() {
        return allocated_time;
    }

    public void setAllocated_time(ArrayList<String> allocated_time) {
        this.allocated_time = allocated_time;
    }
}
