package model;

import java.util.Iterator;

public class Train implements Iterable<Wagon>{
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
        /*  To reset the number of wagons attached to a train
         * a check on the firstWagon and a while loop is used, to traverse attached wagons
         * With each found wagon, the numberOfWagons increases */

        numberOfWagons = 0;
        //If there is no first wagon, the numberOfWagons will be kept at 0;
        if (firstWagon != null) {
            Wagon wagon = firstWagon;
            while (wagon != null) {
                numberOfWagons++;
                wagon = wagon.getNextWagon();
            }
        }
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    /* three helper methods that are usefull in other methods */
    boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1
        if (firstWagon == null) return -1;

        int position = 1;
        Wagon wagon = this.firstWagon;

        while (wagon != null) {
            if (wagon.getWagonId() == wagonId) return position;

            wagon = wagon.getNextWagon();
            position++;

            if (wagon == null){
                return -1;
            }
        }

        return position;
    }

    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */

        if (position > this.getNumberOfWagons() || position == 0 || position == -1)
            throw new IndexOutOfBoundsException(String.format("This train doesn't have %d wagons.", position));

        int seen = 0;
        Wagon subject = this.firstWagon;
        while (subject != null) {
            seen++;
            if (seen == position)
                break;

            subject = subject.getNextWagon();
        }

        return subject;
    }

    public int getNumberOfSeats() {
        if (this.firstWagon == null || !(this.firstWagon instanceof PassengerWagon))
            return 0;

        int seats = 0;
        //For each implementation with the TrainWagon iterator
        for (Wagon wagon : this) {
            seats += ((PassengerWagon) wagon).getNumberOfSeats();
        }

        return seats;
    }

    public int getTotalMaxWeight() {
        if (this.firstWagon == null || !(this.firstWagon instanceof FreightWagon))
            return 0;

        int weight = 0;

        //For each implementation with the TrainWagon iterator
        for (Wagon wagon : this) {
            weight += ((FreightWagon) wagon).getMaxWeight();
        }

        return weight;
    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator<Wagon> iterator() {
        return new TrainWagonIterator(this.firstWagon);
    }
}

