package service;

import enums.DriverCategory;
import exception.ParkingLotException;
import model.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem<whiteVehicleDetails> {

    int parkingLot;
    int parkingSlotInLot;
    public ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int parkingLot, int parkingSlotInLot) {

        this.parkingLot = parkingLot;
        this.parkingSlotInLot = parkingSlotInLot;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, parkingLot).forEachOrdered(lot -> parkingLotList.add(new ParkingLot(parkingSlotInLot)));
    }

    public void parkVehicle(Car carDetails, DriverCategory type) throws ParkingLotException {

        for (ParkingLot lot : parkingLotList) {
            boolean isVehiclePresent = lot.isVehiclePresent(carDetails);
            if (isVehiclePresent) {
                throw new ParkingLotException("Already parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
            }
        }
        if (type.equals(DriverCategory.HANDICAP)) {
            if (carDetails.getSize().equals("SMALL"))
                IntStream.range(0, parkingLot).filter(lot -> parkingLotList.get(lot)
                        .getNumberOfParkedCars() != parkingSlotInLot)
                        .findFirst().ifPresent(lot -> {
                    try {
                        parkingLotList.get(lot).parkVehicle(carDetails);
                    } catch (ParkingLotException e) {
                        e.printStackTrace();
                    }
                });
            if (carDetails.getSize().equals("LARGE")) {
                ParkingLot lots = getPositionOnLot();
                lots.parkVehicle(carDetails);

            }
        }
        if (type.equals(DriverCategory.NORMAL)) {
            ParkingLot lots = getPositionOnLot();
            lots.parkVehicle(carDetails);
        }
    }


    private ParkingLot getPositionOnLot() throws ParkingLotException {
        int totalParked = IntStream.range(0, parkingLot).map(slot -> this.parkingLotList.get(slot).getNumberOfParkedCars()).sum();
        if (totalParked > (parkingLot * parkingSlotInLot)) {
            throw new ParkingLotException("Parking Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        List<ParkingLot> lotList = new ArrayList<>(this.parkingLotList);
        lotList.sort(Comparator.comparing(ParkingLot::getNumberOfParkedCars));
        ParkingLot lotTobeParked = lotList.get(0);
        return lotTobeParked;
    }

    public boolean isVehiclePresent(Car carDetails) {
        return this.parkingLotList.stream().anyMatch(lot -> lot.isVehiclePresent(carDetails));
    }

    public String vehicleLocation(Car carDetails) {
        ParkingLot parked = this.parkingLotList.stream()
                .filter(lot -> lot.isVehiclePresent(carDetails)).findFirst().get();
        String vehicleLocation = String.format("lot %d slot %d", parkingLotList.indexOf(parked) + 1, parked.
                vehicleLocation(carDetails));
        System.out.println(vehicleLocation);
        return vehicleLocation;
    }

    public List getVehiclePositionByColors(String color) throws ParkingLotException {
        List<String> whiteVehicleDetails = new ArrayList<>();
        int lotNumber = 0;
        for (ParkingLot parkingLot : parkingLotList) {
            List<Integer> onColor = parkingLot.findOnColor(color);
            if (onColor.size() == 0)
                throw new ParkingLotException("No such color", ParkingLotException.ExceptionType.NO_SUCH_COLOR);
            lotNumber++;
            whiteVehicleDetails.add("lot " + lotNumber + " slot " + onColor);
        }
        return whiteVehicleDetails;
    }
}