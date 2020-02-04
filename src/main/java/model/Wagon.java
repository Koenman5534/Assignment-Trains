package model;

import com.sun.jdi.IncompatibleThreadStateException;

public class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }

    public Wagon getLastWagonAttached() {

          //  3 -> 2 -> 1
//        Wagon lastWagon = this;
//        while (lastWagon.getPreviousWagon() != null){
//            lastWagon = lastWagon.getPreviousWagon();
//        }

        if (this.nextWagon != null){
            return  this.nextWagon;
        }


        return this.previousWagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfWagonsAttached() {
        if (previousWagon == null) {
            return 0;
        }
        int number = 0;
        Wagon subject = previousWagon;
        while (subject != null)
        {
            number++;
            subject = subject.previousWagon;
        }

        return number;
    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
