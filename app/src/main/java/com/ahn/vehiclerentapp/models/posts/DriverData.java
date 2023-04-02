package com.ahn.vehiclerentapp.models.posts;

public class DriverData {

    private String name;
    private String phone_number;
    private String nearest_town;
    private String active_town;
    private String address;
    private String driver_image;
    private String licence_image;
    private String bit;

    public DriverData() {
    }

    public DriverData(String name, String phone_number, String nearest_town, String active_town, String address, String driver_image, String licence_image, String bit) {
        this.name = name;
        this.phone_number = phone_number;
        this.nearest_town = nearest_town;
        this.active_town = active_town;
        this.address = address;
        this.driver_image = driver_image;
        this.licence_image = licence_image;
        this.bit = bit;
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
}
