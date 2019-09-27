package model;

public class Shunter {
    //         0    1       2         1    2      3        2    3      0
    //Train - next.wagon.previous - next.wagon.previous - next.wagon.previous

    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        Class firstWagonClass = train.getFirstWagon().getClass();
        Class wagonClass = wagon.getClass();

        if(firstWagonClass.equals(PassengerWagon.class)){
            return wagonClass.equals(PassengerWagon.class);
        } else if (firstWagonClass.equals(FreightWagon.class)){
            return wagonClass.equals(FreightWagon.class);
        }
        return true;
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        //If the first wagon and the second wagon are  passenger wagon, two can be coupled to number one
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
        return true;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        int totalAmountWagons = train.getNumberOfWagons() + 1;
        int maxAllowedAmountOfWagons = train.getEngine().getMaxWagons();

        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        if(totalAmountWagons >= maxAllowedAmountOfWagons){
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

         // Wagons can be attached if the wagon type is suitable and if this action does not
        // exceed the capacity of the train
        if (
                train.getNumberOfWagons() < train.getEngine().getMaxWagons() &&
                isSuitableWagon(train.getFirstWagon(), wagon)
        )
        {
            // set this new wagon as the previous wagon of the last wagon of the train
            // if there is no last wagon of the train, it means that this wagon is the first
            // one, so just set it as the first wagon of the train, then reset the number of
            // wagons
            Wagon lastWagon = train.getWagonOnPosition(train.getNumberOfWagons());
            if (lastWagon == null)
            {
                train.setFirstWagon(wagon);
            }
            else
            {
                wagon.setNextWagon(lastWagon);
                lastWagon.setPreviousWagon(wagon);
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
        if (train.getNumberOfWagons() < train.getEngine().getMaxWagons() &&
            isSuitableWagon(train.getFirstWagon(), wagon)
        )
        {
            if (train.getFirstWagon() == null)
            {
                train.setFirstWagon(wagon);
            }
            else
            {
                train.getFirstWagon().setNextWagon(wagon);
                train.setFirstWagon(wagon);
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
            return true;
        }
        return false;

    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        boolean onTrain = false;
        Wagon subject = train.getFirstWagon();
        while (subject != null)
        {
            if (subject.equals(wagon))
            {
                onTrain = true;
                break;
            }
            subject = subject.getPreviousWagon();
        }
        // TODO actual detachment...
        return false;

    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
         return false;

    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        return false;

    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon
        return false;

    }
}
