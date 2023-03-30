package com.ahn.vehiclerentapp.models.city;

public class CityDataList {

   // int city_id;
    String city_name;

    public CityDataList(String city_name) {
        this.city_name = city_name;
    }

    public CityDataList() {
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
