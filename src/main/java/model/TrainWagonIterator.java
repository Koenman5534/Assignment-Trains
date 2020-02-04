package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

 class TrainWagonIterator implements Iterator<Wagon> {
    private int cursor = 0;
    private ArrayList<Wagon> wagons;
    private int amountOfWagons;

    public TrainWagonIterator(Wagon firstWagon) {
        if ((firstWagon != null)){
            wagons = new ArrayList<>();
            wagons.add(firstWagon);
            amountOfWagons = 1;

            /*Loop over the wagons, for as long as the next wagon attached
              isn't null, increase the amountOfWagons and set the next wagon to be checked.*/
            Wagon wagon = firstWagon;
            while (wagon.getNextWagon() != null){
                wagon = wagon.getNextWagon();
                wagons.add(wagon);
                amountOfWagons++;
            }
        } else{
            amountOfWagons = 0;
        }
    }

    public boolean hasNext() {
        return this.cursor < amountOfWagons;
    }

    //Make use of 'post-increment'
    public Wagon next() {
        if(this.hasNext()) {
            return wagons.get(cursor++);
        }

        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
