package model;

import java.util.Map;
import java.util.Objects;

public class Car {

    public String registration;
    public String size;
    public String color;

    public Car() {
    }
    public Car(String registration, String size){
        this.registration = registration;
        this.size =size;
    }

    public Car(String registration, String size, String color) {
        this.registration = registration;
        this.size = size;
        this.color = color;
    }

    public Car(String registration) {
        this.registration = registration;

    }

    public String getSize() {
        return size;
    }

    public String getRegistration() {
        return registration;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(registration, car.registration) &&
                Objects.equals(size, car.size);
    }
}
