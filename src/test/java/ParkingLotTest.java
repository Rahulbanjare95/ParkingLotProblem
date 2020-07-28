import enums.DriverCategory;
import exception.ParkingLotException;
import model.Car;
import observer.AirportSecurity;
import observer.Owner;
import org.junit.Assert;
import org.junit.Test;
import service.ParkingLot;
import service.ParkingLotSystem;

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
            parkingLot.parkWithDetails(1, "CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M7393");
            Assert.assertTrue(isUnPark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarDetailsIncorrect_WhenUnParked_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails(1, "CG11M7393");
            boolean isUnPark = parkingLot.unParkCar("CG11M0001");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.WRONG_DETAILS, e.type);
        }
    }

    @Test
    public void givenMultipleCarDetails_whenTriedToExceedParkingLimit_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails(1, "CG11M7393");
            parkingLot.parkWithDetails(2, "KA01B1212");
            parkingLot.parkWithDetails(3, "DL03C0003");
            parkingLot.parkWithDetails(4, "MH04CD1011");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenMultipleCarsToFullParkingLot_WhenInformedToAirportSecurity_ShouldInformByTrue() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLot.registerObserver(airportSecurity);
        try {
            parkingLot.parkWithDetails(1, "CG11M7393");
            parkingLot.parkWithDetails(2, "KA01B1212");
            parkingLot.parkWithDetails(3, "DL03C0003");
            parkingLot.parkWithDetails(4, "MH04CD1011");
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
            parkingLot.parkWithDetails(1, "CG11M7393");
            parkingLot.parkWithDetails(2, "KA01B1212");
            parkingLot.parkWithDetails(3, "DL03C0003");
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
            parkingLot.parkWithDetails(0, "CG11M7231");
            int position = parkingLot.getPositionOfVehicle("CG11M7231");
            Assert.assertEquals(0, position);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarSlotCorrectly_whenCheckForPosition_shouldReturnTrue() {
        try {
            parkingLot.parkWithDetails(1, "CG11M7393");
            parkingLot.parkWithDetails(2, "CG04Z1122");
            int slot = parkingLot.getPositionOfVehicle("CG04Z1122");
            Assert.assertEquals(2, slot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCar_whenParked_ShouldReturnTime() {
        try {
            int slotPositionToPark = parkingLot.getSlotPositionToPark(1);
            parkingLot.parkWithDetails(slotPositionToPark, "CG04Z1122");
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
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            String locationParkingForThird = parkingLotSystem.vehicleLocation(third);
            String expectedForThird = "lot 1 slot 2";
            Assert.assertEquals(expectedForThird, locationParkingForThird);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkedVehicleRegistration_WhenChecked_shouldReturnTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        try {
            Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
            Car second = new Car("CG11M001", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
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
            Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
            Car second = new Car("CG11M001", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
            Car third = new Car("CG11M789", "BLACK", "SMALL", "BMW", DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            boolean isVehiclePresent = parkingLotSystem.isVehiclePresent(third);
            Assert.assertFalse(isVehiclePresent);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
    //UC 10 Handicap Driver

    @Test
    public void givenHandiCapDriver_whenParked_ShouldParkAtNearestSlot() throws ParkingLotException {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "BLUE", "SMALL", "TOYOTA", DriverCategory.HANDICAP);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.HANDICAP, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
            String expected = "lot 1 slot 2";
            Assert.assertEquals(expected, locationParkingForSecond);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenNormalDriver_whenParked_ShouldParkEvenly() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
            String expected = "lot 2 slot 1";
            Assert.assertEquals(expected, locationParkingForSecond);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void givenLargeCars_whenParked_ShouldParkAtMostFreeSpace() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "BLUE", "LARGE", "TOYOTA", DriverCategory.HANDICAP);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.HANDICAP, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            String locationParkingForSecond = parkingLotSystem.vehicleLocation(second);
            String expected = "lot 2 slot 1";
            Assert.assertEquals(expected, locationParkingForSecond);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
=======
    //
    @Test
    public void givenParkingLotList_whenFilteredBasedOnWhiteColour_ShouldReturnWhiteCars() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "WHITE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "WHITE", "LARGE", "TOYOTA", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "WHITE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List colorlist = parkingLotSystem.findWhiteVehiclePosition("WHITE");
            List expected = Arrays.asList("lot 1 slot [1]", "lot 2 slot [1, 2]");
            Assert.assertEquals(expected, colorlist);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

>>>>>>> UC17_ListOfAllParkedCars

    @Test
    public void givenMultipleCarsToPark_whenParkedAndSearchedForBlueToyotaCars_ShouldReturnBlueToyotaCarsList() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "BLUE", "LARGE", "TOYOTA", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "BLACK", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List carByBrandsAndColor = parkingLotSystem.findCarByBrandsAndColor("BLUE", "TOYOTA");
            List expected = Arrays.asList("lot 1 slot [1 CG11M0000 ABC]", "lot 2 slot [1 CG11M001 XYZ, 2 CG11M0003 XYZ]");
            Assert.assertEquals(expected, carByBrandsAndColor);

        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenMultipleCarsParked_whenSearchedForBMWcars_ShouldReturnBMWPosition() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "MARUTI", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "GREEN", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "SILVER", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "BMW", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List<String> list = parkingLotSystem.findBMWCars("BMW");
            List<String> expected = Arrays.asList("lot 1 slot []", "lot 2 slot [2]");
            Assert.assertEquals(expected, list);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void givenMultipleCarsButNoBMW_whenSearchedForBMWcars_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car second = new Car("CG11M001", "GREEN", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "MARUTI", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "SILVER", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "KIA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List bmw = parkingLotSystem.findBMWCars("BMW");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.NO_SUCH_BRAND, e.type);
        }
    }

    @Test
    public void givenMultipleCarsparked_whenSearchedForCarsParkedInLast30Mins_ShouldReturRecentParkCarsList() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car second = new Car("CG11M001", "GREEN", "SMALL", "TOYOTA", DriverCategory.NORMAL);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "MARUTI", DriverCategory.NORMAL);
        Car third = new Car("CG11M0002", "SILVER", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "KIA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List carsParkedByTime = parkingLotSystem.findCarsParkedByTime(30);
            List<String> list = Arrays.asList("lot1 slot [1, 2]", "lot2 slot [1, 2]");
            Assert.assertEquals(list, carsParkedByTime);
        } catch (ParkingLotException e) {
            e.getMessage();
        }
    }

    @Test
    public void givenMultipleCars_whenCheckedForHandicapDriverAndSmallCar_ShouldReturnListOfHandicapDriversParked() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "MARUTI", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "GREEN", "SMALL", "TOYOTA", DriverCategory.HANDICAP);
        Car third = new Car("CG11M0002", "SILVER", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "KIA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List handicaplist = parkingLotSystem.findCarsByRowForHandicap(DriverCategory.HANDICAP, "SMALL");
            List<String> expected = Arrays.asList(" lot 1 ==  [  Slot: 2 Registration CG11M001 BRAND TOYOTA Size - SMALL AttendantName XYZ]", " lot 2 ==  []");
            Assert.assertEquals(expected, handicaplist);
        } catch (ParkingLotException e) {
            e.getMessage();
        }
    }

    @Test
    public void givenMultipleCarsParked_whenCheckedForAllParkedCars_ShouldReturnAllParkedCars() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        Car first = new Car("CG11M0000", "BLUE", "SMALL", "MARUTI", DriverCategory.NORMAL);
        Car second = new Car("CG11M001", "GREEN", "SMALL", "TOYOTA", DriverCategory.HANDICAP);
        Car third = new Car("CG11M0002", "SILVER", "SMALL", "FORD", DriverCategory.NORMAL);
        Car fourth = new Car("CG11M0003", "BLUE", "SMALL", "KIA", DriverCategory.NORMAL);
        try {
            parkingLotSystem.parkVehicle(first, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(second, DriverCategory.NORMAL, "XYZ");
            parkingLotSystem.parkVehicle(third, DriverCategory.NORMAL, "ABC");
            parkingLotSystem.parkVehicle(fourth, DriverCategory.NORMAL, "XYZ");
            List allParkedVehicles = parkingLotSystem.getAllParkedVehicles();
            List<String> explist = Arrays.asList("lot 1==[Slot: 1 Registration CG11M0000, Slot: 2 Registration CG11M001]", "lot 2==[Slot: 1 Registration CG11M0002, Slot: 2 Registration CG11M0003]");
            Assert.assertEquals(explist,allParkedVehicles);
        } catch (ParkingLotException e) {
            e.getMessage();
        }
    }

}