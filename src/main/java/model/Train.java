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
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */
       int number = 0;
       if (firstWagon == null) {
           if (firstWagon.getPreviousWagon() == null){
               this.numberOfWagons = 1;
               return;
           }
           numberOfWagons = 0;
       }

       Wagon wagon = firstWagon;
       while (wagon != null) {
           number++;
           wagon = wagon.getNextWagon();
       }

       this.numberOfWagons = number;
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasNoWagons() {
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
        if (this.firstWagon == null)
            return -1;

        int pos = 1;
        Wagon subject = this.firstWagon;
        while (subject != null) {
            if (subject.getWagonId() == wagonId)
                break;

            pos++;
            subject = subject.getPreviousWagon();

            if (subject == null){
                return  -1;
            }
        }

        return pos;
    }


    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */
        if (position > this.getNumberOfWagons())
            throw new IndexOutOfBoundsException(String.format("This train doesn't have %d wagons.", position));

        int seen = 0;
        Wagon subject = this.firstWagon;
        while (subject != null) {
            seen++;
            if (seen == position)
                break;

            subject = subject.getPreviousWagon();
        }

        return subject;
    }

    public int getNumberOfSeats() {
        if (this.firstWagon == null || !(this.firstWagon instanceof PassengerWagon))
            return 0;

        int seats = 0;
//        PassengerWagon passengerWagon = (PassengerWagon) this.firstWagon;
//        while(passengerWagon != null) {
//            seats += passengerWagon.getNumberOfSeats();
//            passengerWagon = (PassengerWagon) passengerWagon.getPreviousWagon();
//        }

        for (Iterator<Wagon> it = iterator(); it.hasNext(); ) {
            Wagon wagon = it.next();
                seats += ((PassengerWagon) wagon).getNumberOfSeats();
        }

        return seats;
    }

    public int getTotalMaxWeight() {
        if (this.firstWagon == null || !(this.firstWagon instanceof FreightWagon))
            return 0;

        int weight = 0;
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

