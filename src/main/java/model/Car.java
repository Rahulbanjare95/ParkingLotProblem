package model;


import enums.DriverCategory;

import java.sql.Driver;
import java.util.Objects;

public class Car {

    public String registration;
    public String color;
    public String size;
    public String brand;
    DriverCategory type;

    public Car() {
    }

    public Car(String registration, String color, String size, DriverCategory type) {
        this.registration = registration;
        this.color = color;
        this.size = size;

    }

    public Car(String registration, String color, String size, String brand,DriverCategory type) {
        this.registration = registration;
        this.color = color;
        this.size = size;
        this.brand = brand;
        this.type =type;
    }

    public DriverCategory getType() {
        return type;
    }

    public void setType(DriverCategory type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(registration, car.registration) &&
                Objects.equals(color, car.color) &&
                Objects.equals(size, car.size) &&
                Objects.equals(brand, car.brand);
    }

}
