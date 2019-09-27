package model;

import java.util.Collections;
import java.util.List;

public class Shunter {
    //         0    1       2         1    2      3        2    3      0
    //Train - next.wagon.previous - next.wagon.previous - next.wagon.previous

    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        Wagon firstWagon = train.getFirstWagon();

        if (firstWagon == null){
            return true;
        } else {
            return isSuitableWagon(firstWagon, wagon);
        }
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        //If the first wagon and the second wagon are  passenger wagon, two can be coupled to number one
        if (one == null || two == null)
        {
            return false;
        }
        if(one.getClass().equals(PassengerWagon.class)) {
           if (two.getClass().equals(PassengerWagon.class)){
               return true;
           }
        } else if (two.getClass().equals(FreightWagon.class)){
            return true;
        }

        return false;
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons

        if (train != null && wagon != null){
            int totalAmountWagons = train.getNumberOfWagons();
            int totalAmountOfWagonsHooked = wagon.getNumberOfWagonsAttached();
            int maxAllowedAmountOfWagons = train.getEngine().getMaxWagons();

            if ((totalAmountWagons + totalAmountOfWagonsHooked) >= maxAllowedAmountOfWagons){
                return false;
            }
        }
        return true;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        int totalAmountWagons = train.getNumberOfWagons() ;
        int maxAllowedAmountOfWagons = train.getEngine().getMaxWagons();

        if (wagon.getNumberOfWagonsAttached() == 0) {
            // the engine of a train has a maximum capacity, this method checks for a row of wagons
            if (totalAmountWagons >= maxAllowedAmountOfWagons) {
                return false;
            }
            return true;
        }

        return hasPlaceForWagons(train,wagon);
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

        if (hasPlaceForOneWagon(train, wagon)){
            //get the last wagon and check if it is compatible with the given wagon(To couple)
            //Also check is the last one already has a 'previous' wagon, if so, something has gone wrong
            Wagon lastWagon = train.getWagonOnPosition(train.getNumberOfWagons());

            if (lastWagon == null){
                train.setFirstWagon(wagon);
            } else if(isSuitableWagon(lastWagon, wagon) && !lastWagon.hasPreviousWagon()){
                lastWagon.setPreviousWagon(wagon);
                wagon.setNextWagon(lastWagon);
            }

            train.resetNumberOfWagons();
            return true;
        }
        return false;
    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */

        if (train.getNumberOfWagons() < train.getEngine().getMaxWagons())
        {
            if (train.getFirstWagon() == null)
            {
                train.setFirstWagon(wagon);
            }
            else if (isSuitableWagon(train.getFirstWagon(), wagon))
            {
                Wagon currentFirstWagon =  train.getFirstWagon();
                currentFirstWagon.setNextWagon(wagon);

                wagon.setPreviousWagon(currentFirstWagon);
                train.setFirstWagon(wagon);
            } else {
                return false;
            }

            train.resetNumberOfWagons();
            return true;
        }

        return false;
    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
        * if so make second wagon next wagon of first */
        if (isSuitableWagon(first, second))
        {
            first.setPreviousWagon(second);
            second.setNextWagon(first);
            return true;
        }
        return false;
    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        int wagonPosition = train.getPositionOfWagon(wagon.getWagonId());
        if (wagonPosition != 0) {
            wagon.getNextWagon().setPreviousWagon(null);
            train.resetNumberOfWagons();
        }
        return true;
    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
        // check if the wagon is on the train

        int wagonPosition = train.getPositionOfWagon(wagon.getWagonId());
        if (wagonPosition != 0) {

            Wagon previousWagon = wagon.getPreviousWagon();
            Wagon nextWagon = wagon.getNextWagon();

            //Check if there is a previous wagon, if so, set the previous wagons's next to the wagon
            // that is in front of the wagon that is to be detached
            if (previousWagon != null) {
                //Check if the current wagon has a Next wagon to couple
                if (nextWagon != null) {
                    previousWagon.setNextWagon(nextWagon);
                } else {
                    previousWagon.setNextWagon(null);
                }
            }

            if (nextWagon != null) {
                //Check if the current wagon has a previous wagon to couple
                if (previousWagon != null) {
                    nextWagon.setPreviousWagon(previousWagon);
                } else {
                    nextWagon.setPreviousWagon(null);
                }
            }

            train.resetNumberOfWagons();

        } else {
            return false;
        }

        return true;
    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        if(from != null && to != null && wagon != null){
            if (from.getPositionOfWagon(wagon.getWagonId()) > 0){
                List<Integer> allWagonsAttached = Collections.singletonList(wagon.getNumberOfWagonsAttached());

                if (hasPlaceForWagons(to, wagon)){
                    System.out.println("To has place for wagons");
                    if (detachAllFromTrain(from, wagon)){
                        System.out.println("Wagons are detached from from");
                        if (hookWagonOnTrainRear(to,wagon)){
                            System.out.println("wagons are hooked to TO");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon

        if(from != null && to != null && wagon != null) {
            if (from.getPositionOfWagon(wagon.getWagonId()) > 0) {
                //first check if the 'To' train has enough room and the wagons are of the same type
                if (hasPlaceForOneWagon(to, wagon) && isSuitableWagon(to, wagon)) {
                    //Try to detach the currently attached wagon
                    if (detachOneWagon(from, wagon)) {
                        //Then hook the wagon to the 'To train'
                        wagon.setPreviousWagon(null);
                        wagon.setNextWagon(null);
                        return hookWagonOnTrainRear(to, wagon);
                    }
                }
            }
        }
        return false;
    }
}
