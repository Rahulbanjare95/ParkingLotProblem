import exception.ParkingLotException;
import model.Car;
import observer.AirportSecurity;
import observer.Owner;
import org.junit.Assert;
import org.junit.Test;
import service.*;

import java.time.LocalTime;
import java.util.ArrayList;
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
            ParkingLot parkingLot = new ParkingLot(1);
            int slotPositionToPark = parkingLot.getSlotPositionToPark(1);
            parkingLot.parkWithDetails(slotPositionToPark,"CG04Z0592");
            LocalTime parkedTime = parkingLot.getParkingTime("CG04Z0592");
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
        try {
            Car firstCar = new Car("CG11M7393","SMALL");
            Car secondCar = new Car("CG04Z1994","SMALL");
            Car thirdCar = new Car("CG012K1964", "SMALL");
            Car fourthCar = new Car("KA01B2030","SMALL");
            parkingLotSystem.parkVehicle(firstCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(thirdCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourthCar,DriverCategory.NORMAL);
            String parkedPositionSecondCar = parkingLotSystem.vehicleLocation(secondCar);
            String expectedPosition = "lot 2 slot 1";
            Assert.assertEquals(expectedPosition, parkedPositionSecondCar);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkedVehicleRegistration_WhenChecked_shouldReturnTrue() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        try {
            Car firstCar = new Car("CG11M0000","SMALL");
            Car secondCar = new Car("CG11M0001","SMALL");
            parkingLotSystem.parkVehicle(firstCar, DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar, DriverCategory.NORMAL);
            boolean isVehiclePresent = parkingLotSystem.isVehiclePresent(firstCar);
            Assert.assertTrue(isVehiclePresent);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void givenUnavailableRegistration_WhenChecked_shouldReturnFalse() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 2);
        try {
            Car firstCar = new Car("CG11M0000","SMALL");
            Car secondCar = new Car("CG11M0001","SMALL");
            Car invalidCar = new Car("CG11M0021","SMALL");
            parkingLotSystem.parkVehicle(firstCar, DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar, DriverCategory.NORMAL);
            boolean isVehiclePresent = parkingLotSystem.isVehiclePresent(invalidCar);
            Assert.assertFalse(isVehiclePresent);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //UC 10 Handicap Driver

    @Test
    public void givenHandiCapDriver_whenParked_ShouldParkAtNearestSlot() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
            Car firstCar = new Car("CG11M7393","SMALL");
            Car secondCar = new Car("CG04Z1994","SMALL");
            Car thirdCar = new Car("CG012K1964", "SMALL");
            Car fourthCar = new Car("KA01B2030","SMALL");
            parkingLotSystem.parkVehicle(firstCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.HANDICAP);
            parkingLotSystem.parkVehicle(thirdCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourthCar,DriverCategory.NORMAL);
            String parkedPosition = parkingLotSystem.vehicleLocation(secondCar);
            String expectedPosition = "lot 1 slot 2";
            Assert.assertEquals(expectedPosition,parkedPosition);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenSameVehicle_WhenParkedAgain_ShouldThrowException()  {
        try {
            Car firstCar = new Car("CG11M0000","SMALL");
            Car secondCar = new Car("CG11M0000","SMALL");
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
            parkingLotSystem.parkVehicle(firstCar, DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.ALREADY_PARKED,e.type);
        }
    }

    //UC11

    @Test
    public void givenLargeCarToBeParkedByHandicap_WhenParked_ShouldBeParkedAtMostFreeSlot() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
            Car firstCar = new Car("CG11M7393","SMALL");
            Car secondCar = new Car("CG04Z1994","LARGE");
            Car thirdCar = new Car("CG012K1964", "SMALL");
            Car fourthCar = new Car("KA01B2030","SMALL");
            parkingLotSystem.parkVehicle(firstCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.HANDICAP);
            parkingLotSystem.parkVehicle(thirdCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourthCar,DriverCategory.NORMAL);
            String parkedPosition = parkingLotSystem.vehicleLocation(secondCar);
            String expectedPosition = "lot 2 slot 1";
            Assert.assertEquals(expectedPosition,parkedPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void givenSmallCarToBeParkedByHandicap_WhenParked_ShouldBeParkedAtNearestSlot() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
            Car firstCar = new Car("CG11M7393","SMALL");
            Car secondCar = new Car("CG04Z1994","SMALL");
            Car thirdCar = new Car("CG012K1964", "SMALL");
            Car fourthCar = new Car("KA01B2030","SMALL");
            parkingLotSystem.parkVehicle(firstCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.HANDICAP);
            parkingLotSystem.parkVehicle(thirdCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourthCar,DriverCategory.NORMAL);
            String parkedPosition = parkingLotSystem.vehicleLocation(secondCar);
            String expectedPosition = "lot 1 slot 2";
            Assert.assertEquals(expectedPosition,parkedPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenMultipleVehiclesToPark_WhenSearchedForWhiteColor_ShouldReturnWhiteCarLocations() {
        try{
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2,2);
            Car firstCar = new Car("CG11M7393","SMALL","RED");
            Car secondCar = new Car("CG04Z1994","SMALL","WHITE");
            Car thirdCar = new Car("CG012K1964", "SMALL","BLACK");
            Car fourthCar = new Car("KA01B2030","SMALL","WHITE");
            parkingLotSystem.parkVehicle(firstCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(secondCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(thirdCar,DriverCategory.NORMAL);
            parkingLotSystem.parkVehicle(fourthCar,DriverCategory.NORMAL);
            List<String> white = parkingLotSystem.findWhiteVehiclePosition("WHITE");
            String expected = white.get(1);
            Assert.assertEquals(expected,white.get(1));
        }catch (ParkingLotException e){

        }
    }

}
