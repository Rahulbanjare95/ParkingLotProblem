package model;


import enums.CarSize;
import enums.DriverCategory;

import java.sql.Driver;
import java.util.Objects;

public class Car {

    public String registration;
    public String color;
    CarSize carSize;
    public String brand;
    DriverCategory type;

    public Car() {
    }

    public Car(String registration, String color, CarSize carSize, String brand,DriverCategory type) {
        this.registration = registration;
        this.color = color;
        this.carSize = carSize;
        this.brand = brand;
        this.type =type;
    }

    public DriverCategory getType() {
        return type;
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

    public CarSize getSize() {
        return carSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(registration, car.registration) &&
                Objects.equals(color, car.color) &&
                carSize == car.carSize &&
                Objects.equals(brand, car.brand) &&
                type == car.type;
    }
}
