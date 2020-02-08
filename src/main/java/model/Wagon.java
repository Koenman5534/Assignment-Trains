package model;

import com.sun.jdi.IncompatibleThreadStateException;

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }

    public Wagon getLastWagonAttached() {
        Wagon wagon = this;
        while (wagon.getNextWagon() != null){
            wagon = wagon.getNextWagon();
        }

        return wagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;

        if (nextWagon != null) {
            nextWagon.setPreviousWagon(this);
        }
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfWagonsAttached() {
        //If there is no extra wagon attached, return zero
        if (nextWagon == null) {
            return 0;
        }

        //If there seem to be more, loop un till aren't any wagons attached.
        //Increment the number each time another wagon is found
        int number = 0;
        Wagon wagon = nextWagon;
        while (wagon != null)
        {
            wagon = wagon.nextWagon;
            number++;
        }

        return number;
    }

    boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
