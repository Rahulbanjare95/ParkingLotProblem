package model;

import java.util.Map;

public class Car {

    public String registration;
    public Car() {
    }

    public Car(String registration) {
        this.registration = registration;

    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }
}
