import enums.DriverCategory;
import exception.ParkingLotException;
import model.Car;
import observer.AirportSecurity;
import observer.Owner;
import org.junit.Assert;
import org.junit.Test;
import service.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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
            int slotPositionToPark = parkingLot.getSlotPositionToPark(1);
            parkingLot.parkWithDetails(slotPositionToPark,"CG04Z1122");
            LocalTime parkedTime = parkingLot.getParkingTime("CG04Z1122");
            LocalTime current = LocalTime.now().withNano(0);
            Assert.assertEquals(current, parkedTime);

        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    // UC 9 Refactored uses ParkingLotSystem to park
    @Test
    public void givenVehicle_whenParkedinMultipleLots_ShouldBeParkedEvenly() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000","BLACK","SMALL");
        Car second = new Car("CG11M001","BLACK","SMALL");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","BLACK","SMALL");
        try {

            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
            String locationParkingForThird = parkingLotSystem.vehicleLocation(third);
            String expectedForThird ="lot 1 slot 2";
            Assert.assertEquals(expectedForThird,locationParkingForThird);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkedVehicleRegistration_WhenChecked_shouldReturnTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        try {
            Car first = new Car("CG11M0000","BLACK","SMALL");
            Car second = new Car("CG11M001","BLACK","SMALL");
            parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
            boolean isVehiclePresent = parkingLotSystem.isVehiclePresent(first);
            Assert.assertTrue(isVehiclePresent);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUnavailableVehichleRegistration_WhenChecked_shouldReturnFalse() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        try {
            Car first = new Car("CG11M0000","BLACK","SMALL");
            Car second = new Car("CG11M001","BLACK","SMALL");
            Car third = new Car("CG11M789","BLACK","SMALL");
            parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
            boolean isVehiclePresent = parkingLotSystem.isVehiclePresent(third);
            Assert.assertFalse(isVehiclePresent);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
    //UC 10 Handicap Driver

    @Test
    public void givenHandiCapDriver_whenParked_ShouldParkAtNearestSlot() throws ParkingLotException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
        Car first = new Car("CG11M0000","BLACK","SMALL");
        Car second = new Car("CG11M001","BLACK","SMALL");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","BLACK","SMALL");
        parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(second,DriverCategory.HANDICAP);
        parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
        String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
        String expected = "lot 1 slot 2";
        Assert.assertEquals(expected,locationParkingForSecond);
    }
    @Test
    public void givenNormalDriver_whenParked_ShouldParkEvenly() throws ParkingLotException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
        Car first = new Car("CG11M0000","BLACK","SMALL");
        Car second = new Car("CG11M001","BLACK","SMALL");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","BLACK","SMALL");
        parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
        String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
        String expected = "lot 2 slot 1";
        Assert.assertEquals(expected,locationParkingForSecond);
    }

    @Test
    public void givenLargeCars_whenParked_ShouldParkAtMostFreeSpace() throws ParkingLotException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
        Car first = new Car("CG11M0000","BLACK","SMALL");
        Car second = new Car("CG11M001","BLACK","LARGE");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","BLACK","SMALL");
        parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(second,DriverCategory.HANDICAP);
        parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
        String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
        String expected = "lot 2 slot 1";
        Assert.assertEquals(expected,locationParkingForSecond);
    }

    @Test
    public void givenParkingLotList_whenFilteredBasedOnWhiteColour_ShouldReturnWhiteCars() throws ParkingLotException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
        Car first = new Car("CG11M0000","WHITE","SMALL");
        Car second = new Car("CG11M001","WHITE","LARGE");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","WHITE","SMALL");
        parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
        parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
        List colorlist = parkingLotSystem.findWhiteVehiclePosition("WHITE");
        List expected = Arrays.asList("lot 1 slot [1]","lot 2 slot [1, 2]");
        Assert.assertEquals(expected,colorlist);
    }
    @Test
    public void givenParkingLotListFilteredOnWhiteColor_whenNoWhiteColorsPresent_ShouldThrowException(){
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
        Car first = new Car("CG11M0000","GREEN","SMALL");
        Car second = new Car("CG11M001","BLUE","LARGE");
        Car third = new Car("CG11M0002","BLACK","SMALL");
        Car fourth = new Car("CG11M0003","BLUE","SMALL");
        try {
            parkingLotSystem.parkVehicle(first,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(second,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(third,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourth,DriverCategory.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.NO_SUCH_COLOR, e.type);
        }
    }



}
