package com.ahn.vehiclerentapp.models.city;

import java.util.ArrayList;

public class CityData {

    ArrayList<CityDataList> arrayList;

    public CityData(ArrayList<CityDataList> arrayList) {
        this.arrayList = arrayList;
    }

    public CityData() {
    }

    public ArrayList<CityDataList> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<CityDataList> arrayList) {
        this.arrayList = arrayList;
    }
}
