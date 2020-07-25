import exception.ParkingLotException;
import service.ParkingLot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem {

    int parkingLot;
    int parkingSlotInLot;
    public ArrayList<ParkingLot> parkingLots;
    public ParkingLotSystem(int parkingLot, int parkingSlotInLot) {

        this.parkingLot = parkingLot;
        this.parkingSlotInLot = parkingSlotInLot;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0,parkingLot)
                .forEach(lot -> {
                    this.parkingLots.add(new ParkingLot(parkingSlotInLot));
                });
    }


    public void parkVehicle(String registration) throws ParkingLotException {

        for ( ParkingLot lot : parkingLots){
            boolean isVehiclePresent = lot.isVehiclePresent(registration);
            if(isVehiclePresent){
                throw new ParkingLotException("Already parked",ParkingLotException.ExceptionType.ALREADY_PARKED);
            }
            ParkingLot lots = getPositionOnLot();
            lots.parkVehicle(registration);
        }

    }

    private ParkingLot getPositionOnLot() throws ParkingLotException {
        int carPresentinAllLots = IntStream.range(0, parkingLot)
            .map(slot -> this.parkingLots.get(slot).getNumberOfParkedCars()).sum();
        if (carPresentinAllLots > (parkingLot*parkingSlotInLot)){
            throw  new ParkingLotException("Parking Full",ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        ParkingLot lot;
        List<ParkingLot> lottobeParked = new ArrayList<>(this.parkingLots);
        lottobeParked.sort(Comparator.comparing(ParkingLot::getNumberOfParkedCars));
        lot = lottobeParked.get(0);
        return lot;
    }

//    public boolean isVehiclePresent(String registration){
//        return this.parkingLots.stream().anyMatch(lot ->lot.isVehiclePresent(registration));
//     }

//     public ParkingLot getVehicleParkingLocation(String registration) throws ParkingLotException {
//        return this.parkingLots.stream().filter(lot -> lot.isVehiclePresent(registration)).
//                findFirst().orElseThrow(()->new ParkingLotException("Vehicle Not Parked in lot",
//                ParkingLotException.ExceptionType.VEHICLE_NOT_PARKED));
//     }

     public String location(String registration ){
        ParkingLot parked = this.parkingLots.stream()
                .filter(lot-> lot.isVehiclePresent(registration)).findFirst().get();
        return String.format("%d:%d", parked.vehicleLocation(registration),parkingLots.indexOf(parked)+1);
     }









}
