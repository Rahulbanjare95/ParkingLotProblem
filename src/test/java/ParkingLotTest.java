import exception.ParkingLotException;
import model.Car;
import org.junit.Assert;
import org.junit.Test;
import service.ParkingLot;
import service.ParkingLotControllers;

public class ParkingLotTest {
    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenOneCarWithDetails_whenParked_ShouldReturnOneCar() throws ParkingLotException {
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
            e.printStackTrace();
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_FULL, e.type);
        }
    }

    @Test
    public void givenMultipleCarsToFullParkingLot_WhenInformedToAirportSecurity_shouldReturnTrue() {
        parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
        parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
        parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
        parkingLot.parkWithDetails("MH04CD1011", "Mercedes-Benz S-Class");
        try {
            parkingLot.getParkingAllotmentDetails();
        } catch (ParkingLotException e) {
            Assert.assertTrue(ParkingLotControllers.AIRPORT_SECURITY.isParkingLotFull);
        }
    }

    @Test
    public void givenFullOccupiedParkingLot_WhenSpaceIsFreeAgainInformOwner_shouldReturnTrue() {
        parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
        parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
        parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
        try {
            parkingLot.unParkCar("DL03C0003");
            Assert.assertTrue(ParkingLotControllers.PARKING_LOT_OWNER.isParkingLotFull);
        } catch (ParkingLotException e) {

        }
    }
}
