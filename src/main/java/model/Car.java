package model;


import java.util.Objects;

public class Car {

    public String registration;
    public String color;
    public String size;
    public String brand;

    public Car() {
    }

    public Car(String registration, String color, String size) {
        this.registration = registration;
        this.color = color;
        this.size = size;

    }

    public Car(String registration, String color, String size, String brand) {
        this.registration = registration;
        this.color = color;
        this.size = size;
        this.brand = brand;
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
