import exception.ParkingLotException;
import model.Car;
import org.junit.Assert;
import org.junit.Test;
import service.Owner;
import service.ParkingLot;
import enums.ParkingLotControllers;

public class ParkingLotTest {

    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenOneCarWithDetails_whenParked_ShouldReturnOneCar()  {
        int numberOfParkedCars = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
        Assert.assertEquals(1, numberOfParkedCars);
    }

    @Test
    public void givenCarWithDetails_whenUnParked_ShouldReturnTrue() {
        try {
            parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
            boolean isUnPark = parkingLot.unParkCar("CG11M7393");
            Assert.assertTrue(isUnPark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarDetailsIncorrect_WhenUnParked_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
            boolean isUnPark = parkingLot.unParkCar("CG11M0001");
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.WRONG_DETAILS, e.type);
        }
    }

    @Test
    public void givenMultipleCarDetails_whenTriedToExceedParkingLimit_ShouldThrowException() {
        try {
            parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
            parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
            parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
            parkingLot.parkWithDetails("MH04CD1011", "Mercedes-Benz S-Class");
            parkingLot.getParkingAllotmentDetails();

        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenMultipleCarsToFullParkingLot_WhenInformedToAirportSecurity_ShouldInformByTrue() {
        parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
        parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
        parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
        parkingLot.parkWithDetails("MH04CD1011", "Mercedes-Benz S-Class");
        try {
            parkingLot.getParkingAllotmentDetails();
            Assert.assertTrue(ParkingLotControllers.AIRPORT_SECURITY.isParkingLotFull);
        } catch (ParkingLotException e) {
           e.printStackTrace();
        }
    }

    @Test
    public void givenFullOccupiedParkingLot_WhenSpaceIsFreeAgainInformOwner_shouldInformOwnerByFalse() {
        parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
        parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
        parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
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
            parkingLot.parkWithSlot(1,"CG11M7393", "Hyundai Verna" );
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
            parkingLot.parkWithSlot(1,"CG11M7393", "Hyundai Verna");
            parkingLot.parkWithSlot(2,"CG04Z1122", "Skoda Rapid");
            boolean parkedAt = parkingLot.isParkedAt(2);
            Assert.assertTrue(parkedAt);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }


}
