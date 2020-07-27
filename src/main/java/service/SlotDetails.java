package service;

import model.Car;

import java.time.LocalTime;
import java.util.Objects;

public class SlotDetails {
    int slotNu;
    LocalTime time;
    Car car;

    public SlotDetails(int slotNu,  Car car, LocalTime time) {
        this.slotNu = slotNu;
        this.time = time;
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public SlotDetails() {
    }

    public LocalTime getTime() {
        return time.withNano(0);

    }

    public int getSlotNu() {
        return slotNu;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotDetails that = (SlotDetails) o;
        return slotNu == that.slotNu &&
                Objects.equals(time, that.time) &&
                Objects.equals(car, that.car);
    }

}
