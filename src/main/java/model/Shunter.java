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
            // the engine of a train has a maximum capacity, this method checks for a row of wagons
            if (totalAmountWagons >= maxAllowedAmountOfWagons) {
                return false;
            }
            return true;
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

        if (isSuitableWagon(train, wagon) && hasPlaceForWagons(train, wagon)){
            //get the last wagon and check if it is compatible with the given wagon(To couple)
            //Also check is the last one already has a 'previous' wagon, if so, something has gone wrong

            if (train.getNumberOfWagons() == 0){
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }

            Wagon lastWagon = train.getWagonOnPosition(train.getNumberOfWagons());
            if(isSuitableWagon(lastWagon, wagon) && !lastWagon.hasNextWagon()){
                hookWagonOnWagon(lastWagon, wagon);
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

        if (hasPlaceForWagons(train, wagon))
        {
            if (train.getFirstWagon() == null)
            {
                train.setFirstWagon(wagon);
            }
            else if (isSuitableWagon(train.getFirstWagon(), wagon))
            {
                Wagon currentFirstWagon =  train.getFirstWagon();
                //Hook the current first wagon to the last possible wagon attached to the given wagon.
                //If there aren't any wagons coupled to the wagon, the given wagon will be used.
                currentFirstWagon.setPreviousWagon(wagon.getLastWagonAttached());
                wagon.getLastWagonAttached().setNextWagon(currentFirstWagon);
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
            first.setNextWagon(second);
            second.setPreviousWagon(first);
            return true;
        }

        return false;
    }

    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        int wagonPosition = train.getPositionOfWagon(wagon.getWagonId());
        if (wagonPosition > 0) {

            if (train.getFirstWagon().getWagonId() == wagon.getWagonId()){
                train.setFirstWagon(null);

                train.resetNumberOfWagons();
                return true;
            } else if (wagon.hasPreviousWagon()){
                wagon.getPreviousWagon().setNextWagon(null);
                return true;
            }
            else {
                wagon.setPreviousWagon(null);
                train.resetNumberOfWagons();
                return true;
            }
        }
       return false;
    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */

        // check if the wagon is on the train
        int wagonPosition = train.getPositionOfWagon(wagon.getWagonId());
        if (wagonPosition != 0) {

            //Front
            //If the given wagon is the first wagon on the train
            if (train.getFirstWagon().equals(wagon)){
                train.setFirstWagon(wagon.getNextWagon());

                if (wagon.hasNextWagon()) {
                    wagon.getNextWagon().setPreviousWagon(null);
                }

                train.resetNumberOfWagons();
                return true;
            }

           //Middle
           //Recouple the wagon in front and the back of the given wagon
            if (wagon.hasPreviousWagon() && wagon.hasNextWagon()){
                wagon.getPreviousWagon().setNextWagon(wagon.getNextWagon());
                wagon.getNextWagon().setPreviousWagon(wagon.getPreviousWagon());

                train.resetNumberOfWagons();
                return true;
            }

            //Back
            if (wagon.hasPreviousWagon()|| !wagon.hasNextWagon()){
                Wagon previousWagon = wagon.getPreviousWagon();
                previousWagon.setNextWagon(null);

                train.resetNumberOfWagons();
                return true;
            }

            train.resetNumberOfWagons();
            return true;
        } else {
            return false;
        }
    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        if(from != null && to != null && wagon != null){
            if (from.getPositionOfWagon(wagon.getWagonId()) > 0 && isSuitableWagon(to, wagon)){
                if (hasPlaceForWagons(to, wagon)){

                    // Front wagon
                    if (from.getFirstWagon().getWagonId() == wagon.getWagonId()){
                        detachAllFromTrain(from, from.getFirstWagon());
                        hookWagonOnWagon(to.getWagonOnPosition(to.getNumberOfWagons()), wagon);
                    }
                    else { // Middle
                        detachAllFromTrain(from, wagon);
                        //If the 'to' train doesn't have any wagons
                        if (to.hasNoWagons()){
                            to.setFirstWagon(wagon);
                            wagon.setPreviousWagon(null);
                        } else { //back
                            Wagon wagonToCoupleTo = to.getWagonOnPosition(to.getNumberOfWagons());
                            hookWagonOnWagon(wagonToCoupleTo, wagon);
                        }
                    }

                    from.resetNumberOfWagons();
                    to.resetNumberOfWagons();
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon

        if(from != null && to != null && wagon != null) {
            if (from.getPositionOfWagon(wagon.getWagonId()) > 0 && isSuitableWagon(to, wagon)) {
                //First check if the 'To' train has enough room and the wagons are of the same type
                if (hasPlaceForOneWagon(to, wagon)) {
                    // Front wagon
                    if (from.getFirstWagon().getWagonId() == wagon.getWagonId()){
                        detachOneWagon(from, from.getFirstWagon());
                        hookWagonOnWagon(to.getWagonOnPosition(to.getNumberOfWagons()), wagon);
                        wagon.setNextWagon(null);
                    }
                    else { // Middle
                        detachOneWagon(from, wagon);

                        //If the 'to' train doesn't have any wagons
                        if (to.hasNoWagons()){
                            to.setFirstWagon(wagon);
                            wagon.setPreviousWagon(null);
                            wagon.setNextWagon(null);

                        } else { //back

                            Wagon wagonToCoupleTo = to.getWagonOnPosition(to.getNumberOfWagons());
                            hookWagonOnWagon(wagonToCoupleTo, wagon);
                            wagon.setNextWagon(null);
                        }
                    }

                    from.resetNumberOfWagons();
                    to.resetNumberOfWagons();
                    return true;
                }
            }
        }
        return false;
    }
}
