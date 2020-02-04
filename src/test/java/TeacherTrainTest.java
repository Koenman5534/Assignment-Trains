import model.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTrainTest {

    @Test
    public void trainShouldReportCorrectRoute() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");

        assertTrue(train.toString().indexOf(" from Haarlem to Amsterdam") > 0);
    }

    @Test
    public void reportPassengerTrainCorrectly() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        assertEquals(train.isPassengerTrain(), train.isFreightTrain(),
                "An empty train should be a passenger train as much as a freight train");
        Wagon wagon = new PassengerWagon(0, 0);
        train.setFirstWagon(wagon);

        assertTrue(train.isPassengerTrain());
        assertFalse(train.isFreightTrain());
    }

    @Test
    public void reportFreightTrainCorrectly() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        assertEquals(train.isPassengerTrain(), train.isFreightTrain(),
                "An empty train should be a passenger train as much as a freight train");
        Wagon wagon = new FreightWagon(0, 0);
        train.setFirstWagon(wagon);

        assertTrue(train.isFreightTrain());
        assertFalse(train.isPassengerTrain());
    }


    @Test
    public void trainWithoutWagonsHasNoWagonsAttached() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        train.resetNumberOfWagons();

        assertEquals(0, train.getNumberOfWagons());
    }

    @Test
    public void trainWithThreeWagonsHasThreeWagonsAttached() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        for (int i = 0; i < 3; i++) {
            Wagon wagon = new PassengerWagon(i, i);
            wagon.setNextWagon(train.getFirstWagon());
            train.setFirstWagon(wagon);
        }

        train.resetNumberOfWagons();

        assertEquals(3, train.getNumberOfWagons());
    }

    @Test
    public void passengerTrainShouldReportTotalNumberOfSeatsCorrectly() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        for (int i = 0; i < 10; i++) {
            Wagon wagon = new PassengerWagon(i, i);
            wagon.setNextWagon(train.getFirstWagon());
            train.setFirstWagon(wagon);
        }
        train.resetNumberOfWagons();

        assertEquals(45, train.getNumberOfSeats());
    }

    @Test
    public void freightTrainShouldReportTotalMaxWeightCorrectly() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        for (int i = 0; i < 10; i++) {
            Wagon wagon = new FreightWagon(i, i);
            wagon.setNextWagon(train.getFirstWagon());
            train.setFirstWagon(wagon);
        }
        train.resetNumberOfWagons();

        assertEquals(45, train.getTotalMaxWeight());
    }

    @Test
    public void passengerTrainHasNoMaxWeight() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        train.setFirstWagon(new PassengerWagon(0, 0));

        assertEquals(0, train.getTotalMaxWeight());
    }

    @Test
    public void freightTrainHasNoSeats() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        train.setFirstWagon(new FreightWagon(0, 0));

        assertEquals(0, train.getNumberOfSeats());
    }

    @Test
    public void reportsPositionCorrectly() {
        Train train = new Train(new Locomotive(0,3), "Haarlem", "Amsterdam");
        Wagon first = new PassengerWagon(1, 10);
        Wagon second = new PassengerWagon(2, 2);
        Wagon third = new PassengerWagon(3, 30);

        train.setFirstWagon(first);
        first.setNextWagon(second);
        second.setNextWagon(third);
        train.resetNumberOfWagons();

        assertEquals(1, train.getPositionOfWagon(1));
        assertEquals(2, train.getPositionOfWagon(2));
        assertEquals(3, train.getPositionOfWagon(3));
    }

    @Test
    public void returnsCorrectWagonAtPositionOrThrowsException() {
        Train train = new Train(new Locomotive(0,3), "Haarlem", "Amsterdam");
        Wagon first = new PassengerWagon(1, 10);
        Wagon second = new PassengerWagon(2, 2);
        Wagon third = new PassengerWagon(3, 30);

        train.setFirstWagon(first);
        first.setNextWagon(second);
        second.setNextWagon(third);
        train.resetNumberOfWagons();

        assertEquals(first, train.getWagonOnPosition(1));
        assertEquals(second, train.getWagonOnPosition(2));
        assertEquals(third, train.getWagonOnPosition(3));
        assertThrows(IndexOutOfBoundsException.class, () -> { train.getWagonOnPosition(0); } );
        assertThrows(IndexOutOfBoundsException.class, () -> { train.getWagonOnPosition(-1); } );
        assertThrows(IndexOutOfBoundsException.class, () -> { train.getWagonOnPosition(4); } );
        assertThrows(IndexOutOfBoundsException.class, () -> { train.getWagonOnPosition(99); } );
    }

    @Test
    public void reportsUnknownWagonAsSuch() {
        Train train = new Train(new Locomotive(0,3), "Haarlem", "Amsterdam");
        Wagon first = new PassengerWagon(1, 10);
        Wagon second = new PassengerWagon(2, 2);
        Wagon third = new PassengerWagon(3, 30);

        train.setFirstWagon(first);
        first.setNextWagon(second);
        second.setNextWagon(third);
        train.resetNumberOfWagons();

        assertEquals(-1, train.getPositionOfWagon(-13));
    }

    @Test
    public void trainIteratorShouldIterateAllWagons() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        // ExtendedTrain train = new ExtendedTrain(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        int sumIds = 0;
        // iterating objects, because some students have built an object iterator
        for (Object w: train) {
            sumIds += ((Wagon)w).getWagonId();
        }
        assertEquals(0, sumIds);
        for (int i = 0; i < 10; i++) {
            Wagon wagon = new PassengerWagon(i, i);
            wagon.setNextWagon(train.getFirstWagon());
            train.setFirstWagon(wagon);
        }
        train.resetNumberOfWagons();

        for (Object w: train) {
            sumIds += ((Wagon)w).getWagonId();
        }
        assertEquals(45, sumIds);
    }

    @Test
    public void trainImplementsWagonIterableInterface() {
        Train train = new Train(new Locomotive(0, 0), "Haarlem", "Amsterdam");
        // get all interfaces of the train class
        AnnotatedType[] interf = train.getClass().getAnnotatedInterfaces();
        assertTrue(interf.length > 0, "The Train class has no Iterable interface");

        // search for the Wagon Iterable interface
        ParameterizedType iterableInterface = null;
        ParameterizedType wagonIterableInterface = null;
        for (int i = 0; i < interf.length; i++) {
            if (interf[i].getType().getTypeName().contains("Iterable")) {
                iterableInterface = (ParameterizedType)(interf[i].getType());
                if (iterableInterface.getActualTypeArguments().length > 0 &&
                        iterableInterface.getActualTypeArguments()[0].getTypeName().endsWith("Wagon"))
                    wagonIterableInterface = iterableInterface;
            }
        }
        assertNotNull(iterableInterface, "Train should implement an Iterable interface");
        assertNotNull(wagonIterableInterface, "Iterable interface on train should iterate wagons");
    }
}
