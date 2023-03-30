package com.ahn.vehiclerentapp.models.driver;

import java.io.Serializable;

public class VehicleDetails implements Serializable {

    private String vehicle_type;
    private String img_1;
    private String img_2;
    private String img_3;
    private String img_4;

    public VehicleDetails(String vehicle_type, String img_1, String img_2, String img_3, String img_4) {
        this.vehicle_type = vehicle_type;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
    }

    public VehicleDetails() {
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
}
