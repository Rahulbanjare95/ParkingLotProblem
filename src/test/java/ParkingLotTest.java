import exception.ParkingLotException;
import model.Car;
import org.junit.Assert;
import org.junit.Test;
import service.Driver;
import service.Owner;
import service.ParkingLot;
import enums.ParkingLotControllers;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ParkingLotTest {

    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenOneCarWithDetails_whenParked_ShouldReturnOneCar()  {
        int numberOfParkedCars = parkingLot.parkWithDetails("CG11M7393");
        Assert.assertEquals(1, numberOfParkedCars);
    }

    @Test
    public void givenCarWithDetails_whenUnParked_ShouldReturnTrue() {
        try {
            parkingLot.parkWithDetails("CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M7393");
            Assert.assertTrue(isUnPark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarDetailsIncorrect_WhenUnParked_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails("CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M0001");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.WRONG_DETAILS, e.type);
        }
    }

    @Test
    public void givenMultipleCarDetails_whenTriedToExceedParkingLimit_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails("CG11M7393");
            parkingLot.parkWithDetails("KA01B1212");
            parkingLot.parkWithDetails("DL03C0003");
            parkingLot.parkWithDetails("MH04CD1011");
            parkingLot.getParkingAllotmentDetails();

        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenMultipleCarsToFullParkingLot_WhenInformedToAirportSecurity_ShouldInformByTrue() {
        parkingLot.parkWithDetails("CG11M7393");
        parkingLot.parkWithDetails("KA01B1212");
        parkingLot.parkWithDetails("DL03C0003");
        parkingLot.parkWithDetails("MH04CD1011");
        try {
            parkingLot.getParkingAllotmentDetails();
            Assert.assertTrue(ParkingLotControllers.AIRPORT_SECURITY.isParkingLotFull);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenFullOccupiedParkingLot_WhenSpaceIsFreeAgainInformOwner_shouldInformOwnerByFalse() {
        parkingLot.parkWithDetails("CG11M7393");
        parkingLot.parkWithDetails("KA01B1212");
        parkingLot.parkWithDetails("DL03C0003");
        try {
            parkingLot.unParkCar("DL03C0003");
            parkingLot.getParkingAllotmentDetails();
            Assert.assertFalse(ParkingLotControllers.PARKING_LOT_OWNER.isParkingLotFull);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCar_WhenParkedByAttendant_ShouldParkAtSpecificPosition() {
        try {
            parkingLot.parkWithSlot(1,"CG11M7393");
            boolean parkedAt = parkingLot.isParkedAt(1);
            Owner owner = new Owner();
            Assert.assertTrue(owner.isParkedAt(parkedAt));
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarSlotCorrectly_whenDriverWantsToCheck_shouldReturnTrue() {
        try {
            parkingLot.parkWithSlot(1,"CG11M7393");
            parkingLot.parkWithSlot(2,"CG04Z1122");
            boolean isParked = parkingLot.isParkedAt(2);
            Driver driver = new Driver();
            Assert.assertTrue(driver.isAvailable(isParked));
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCar_whenParkedWithTime_ShouldReturnParkedTime() {
            parkingLot.parkWithDetails("CG11M1234");
            LocalTime time = LocalTime.now().withNano(0);
            parkingLot.parkWithSlot(1,"CG11M7393");
            LocalTime parkedTiming = parkingLot.getParkingTime(1,"CG11M7393");
            Assert.assertEquals(time, parkedTiming);

    }
}