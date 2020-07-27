package model;

import java.util.Map;

public class Car {

    public String registration;
    public String color;
    public String size;
    public Car() {
    }

    public Car(String registration, String color, String size) {
        this.registration = registration;
        this.color =color;
        this.size =size;

    }

    public String getColor() {

        return color;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getSize() {
        return size;
    }

}
