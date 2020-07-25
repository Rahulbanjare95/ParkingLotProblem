package service;

import java.time.LocalTime;
import java.util.Objects;

public class SlotDetails {
    int slotNu;
    String carRegistration;
    LocalTime time;

    public SlotDetails(int slotNu, String carRegistration, LocalTime time) {
        this.slotNu = slotNu;
        this.carRegistration = carRegistration;
        this.time = time;
    }

    public SlotDetails() {
    }

    public LocalTime getTime() {
        return time;
    }

    public int getSlotNu() {
        return slotNu;
    }

    public String getCarRegistration() {
        return carRegistration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotDetails that = (SlotDetails) o;
        return slotNu == that.slotNu &&
                Objects.equals(carRegistration, that.carRegistration) &&
                Objects.equals(time, that.time);
    }

}
