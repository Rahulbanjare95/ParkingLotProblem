import exception.ParkingLotException;
import model.Car;
import observer.AirportSecurity;
import observer.Owner;
import org.junit.Assert;
import org.junit.Test;
import service.*;

import java.time.LocalTime;

public class ParkingLotTest {

    ParkingLot parkingLot = new ParkingLot(3);

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenCarWithDetails_whenUnParked_ShouldReturnTrue() {
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M7393");
            Assert.assertTrue(isUnPark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarDetailsIncorrect_WhenUnParked_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M0001");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.WRONG_DETAILS, e.type);
        }
    }

    @Test
    public void givenMultipleCarDetails_whenTriedToExceedParkingLimit_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            parkingLot.parkWithDetails(2,"KA01B1212");
            parkingLot.parkWithDetails(3,"DL03C0003");
            parkingLot.parkWithDetails(4,"MH04CD1011");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenMultipleCarsToFullParkingLot_WhenInformedToAirportSecurity_ShouldInformByTrue() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLot.registerObserver(airportSecurity);
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            parkingLot.parkWithDetails(2,"KA01B1212");
            parkingLot.parkWithDetails(3,"DL03C0003");
            parkingLot.parkWithDetails(4,"MH04CD1011");
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
        boolean capacityFull = airportSecurity.isParkingLotFull();
        Assert.assertTrue(capacityFull);
    }

    @Test
    public void givenFullOccupiedParkingLot_WhenSpaceIsFreeAgainInformOwner_shouldInformOwner() {
        Owner owner = new Owner();
        parkingLot.registerObserver(owner);
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            parkingLot.parkWithDetails(2,"KA01B1212");
            parkingLot.parkWithDetails(3,"DL03C0003");
            parkingLot.unParkCar("DL03C0003");
            boolean parkingLotAvailable = owner.isParkingLotAvailable();
            Assert.assertTrue(parkingLotAvailable);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCar_WhenParkedByAttendant_ShouldParkAtSpecificPosition() {
        try {
            parkingLot.parkWithDetails(0,"CG11M7231");
            int position = parkingLot.getPositionOfVehicle("CG11M7231");
            Assert.assertEquals(0, position);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarSlotCorrectly_whenCheckForPosition_shouldReturnTrue() {
        try {
            parkingLot.parkWithDetails(1,"CG11M7393");
            parkingLot.parkWithDetails(2,"CG04Z1122");
            int slot = parkingLot.getPositionOfVehicle("CG04Z1122");
            Assert.assertEquals(2,slot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCar_whenParked_ShouldReturnTime() {
        try {
            int slotPositionToPark = parkingLot.getSlotPositionToPark();
            parkingLot.parkWithDetails(slotPositionToPark,"CG04Z1122");
            LocalTime parkedTime = parkingLot.getParkingTime("CG04Z1122");
            LocalTime current = LocalTime.now().withNano(0);
            Assert.assertEquals(current, parkedTime);

        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

}
