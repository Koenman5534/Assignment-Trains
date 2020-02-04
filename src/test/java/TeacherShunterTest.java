import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherShunterTest {

    @Test
    public void checkHookWagonOnTrainFront() {
        Wagon sequence = new PassengerWagon(22, 143);
        sequence.setNextWagon(new PassengerWagon(23, 150));
        Train train = new Train(new Locomotive(0, 1), "Haarlem", "Amsterdam");
        assertFalse(Shunter.hookWagonOnTrainFront(train, sequence),
                "Succeeded to hook two wagons on a train with capacity 1");
        assertTrue(Shunter.hookWagonOnTrainFront(train, new FreightWagon(101, 4000)),
                "Cannot hook a freight wagon on empty train");

        train = new Train(new Locomotive(0, 3), "Haarlem", "Amsterdam");
        assertTrue(Shunter.hookWagonOnTrainFront(train, new PassengerWagon(21, 140)),
                "Cannot hook a passenger wagon on empty train");
        assertEquals(1, train.getNumberOfWagons());
        assertEquals(1, train.getPositionOfWagon(21));

        assertTrue(Shunter.hookWagonOnTrainFront(train, sequence));
        assertEquals(3, train.getNumberOfWagons());
        assertEquals( 1, train.getPositionOfWagon(22));
        assertEquals( 2, train.getPositionOfWagon(23));
        assertEquals( 3, train.getPositionOfWagon(21));

        assertFalse(Shunter.hookWagonOnTrainFront(train, new PassengerWagon(25, 10)),
                "Succeeded to hook another wagon on a full train");
        assertFalse(Shunter.hookWagonOnTrainFront(train, new FreightWagon(22, 4000)),
                "Succeeded to hook another Freight wagon on a full passenger train");

        assertTrue(Shunter.detachAllFromTrain(train, train.getFirstWagon()),
                "Cannot detach all wagons from the train");
    }

    @Test
    public void checkHookWagonOnTrainRear() {
        Wagon sequence = new PassengerWagon(22, 143);
        sequence.setNextWagon(new PassengerWagon(23, 150));
        Train train = new Train(new Locomotive(0, 1), "Haarlem", "Amsterdam");
        assertFalse(Shunter.hookWagonOnTrainRear(train, sequence),
                "Succeeded to hook two wagons on a train with capacity 1");
        assertTrue(Shunter.hookWagonOnTrainRear(train, new FreightWagon(101, 4000)),
                "Cannot hook a freight wagon on empty train");

        train = new Train(new Locomotive(0, 3), "Haarlem", "Amsterdam");
        assertTrue(Shunter.hookWagonOnTrainRear(train, new PassengerWagon(21, 140)),
                "Cannot hook a passenger wagon on empty train");
        assertEquals(1, train.getNumberOfWagons());
        assertEquals(1, train.getPositionOfWagon(21));

        assertTrue(Shunter.hookWagonOnTrainRear(train, sequence));
        assertEquals(3, train.getNumberOfWagons());
        assertEquals( 1, train.getPositionOfWagon(21));
        assertEquals( 2, train.getPositionOfWagon(22));
        assertEquals( 3, train.getPositionOfWagon(23));

        assertFalse(Shunter.hookWagonOnTrainRear(train, new PassengerWagon(25, 10)),
                "Succeeded to hook another wagon on a full train");
        assertFalse(Shunter.hookWagonOnTrainRear(train, new FreightWagon(52, 4000)),
                "Succeeded to hook another Freight wagon on a full passenger train");
    }

    @Test
    public void checkDetachFromTrain() {
        Wagon sequence = new PassengerWagon(22, 143);
        sequence.setNextWagon(new PassengerWagon(23, 150));
        sequence.getLastWagonAttached().setNextWagon(new PassengerWagon(24, 150));

        Train train = new Train(new Locomotive(0, 5), "Haarlem", "Amsterdam");
        assertFalse(Shunter.detachAllFromTrain(train, new PassengerWagon(51, 143)),
                "Have detached a wagon that was not on the train");
        assertFalse(Shunter.detachAllFromTrain(train, new FreightWagon(52, 4000)),
                "Have detached a wagon that was not on the train");

        train.setFirstWagon(sequence); train.resetNumberOfWagons();
        assertTrue(Shunter.detachOneWagon(train, sequence.getNextWagon()),
                "Could not detach the middle wagon");
        assertEquals(2, train.getNumberOfWagons());
        assertTrue(Shunter.detachOneWagon(train, sequence.getNextWagon()),
                "Could not detach the last wagon");
        assertEquals(1, train.getNumberOfWagons());
        assertTrue(Shunter.detachAllFromTrain(train, train.getFirstWagon()),
                "Could not detach the first wagons");
        assertEquals(0, train.getNumberOfWagons());
    }

    @Test
    public void checkMoveOneWagon() {
        Wagon sequence = new PassengerWagon(22, 143);
        sequence.setNextWagon(new PassengerWagon(23, 150));
        sequence.getLastWagonAttached().setNextWagon(new PassengerWagon(24, 150));

        Train train1 = new Train(new Locomotive(0, 5), "Haarlem", "Amsterdam");
        Train train2 = new Train(new Locomotive(0, 1), "Haarlem", "Amsterdam");
        Train train3 = new Train(new Locomotive(0, 2), "Haarlem", "Amsterdam");
        assertFalse(Shunter.moveOneWagon(train1, train2, new PassengerWagon(51, 143)),
                "Have moved a wagon that was not on the train");

        train1.setFirstWagon(sequence); train1.resetNumberOfWagons();
        train3.setFirstWagon(new FreightWagon(53, 5000)); train3.resetNumberOfWagons();
        assertFalse(Shunter.moveOneWagon(train1, train2, new FreightWagon(52, 4000)),
                "Have moved a wagon that was not on the train");
        assertEquals(3, train1.getNumberOfWagons());
        assertFalse(Shunter.moveOneWagon(train1, train3, train1.getFirstWagon()),
                "Have moved a passenger wagon to a freight train");
        assertEquals(3, train1.getNumberOfWagons());
        assertEquals(1, train3.getNumberOfWagons());

        assertTrue(Shunter.moveOneWagon(train1, train2, sequence.getNextWagon()),
                "Could not move the middle wagon");
        assertEquals(2, train1.getNumberOfWagons());
        assertEquals(1, train2.getNumberOfWagons());
        assertFalse(Shunter.moveOneWagon(train1, train2, sequence.getNextWagon()),
                "Could move the last wagon without capacity at destination");
        assertEquals(2, train1.getNumberOfWagons());
        assertEquals(1, train2.getNumberOfWagons());
        assertTrue(Shunter.moveOneWagon(train2, train1, train2.getFirstWagon()),
                "Could not move back a single wagon");
        assertEquals(3, train1.getNumberOfWagons());
        assertEquals(0, train2.getNumberOfWagons());
        assertTrue(Shunter.moveOneWagon(train1, train1, train1.getFirstWagon()),
                "Could not move the first wagon to the end of a single train");
        assertEquals(3, train1.getNumberOfWagons());
    }

    @Test
    public void checkMoveAllFromTrain() {
        Wagon sequence = new PassengerWagon(22, 143);
        sequence.setNextWagon(new PassengerWagon(23, 150));
        sequence.getLastWagonAttached().setNextWagon(new PassengerWagon(24, 150));

        Train train1 = new Train(new Locomotive(0, 5), "Haarlem", "Amsterdam");
        Train train2 = new Train(new Locomotive(0, 2), "Haarlem", "Amsterdam");
        Train train3 = new Train(new Locomotive(0, 5), "Haarlem", "Amsterdam");
        assertFalse(Shunter.moveAllFromTrain(train1, train2, new PassengerWagon(51, 143)),
                "Have moved a wagon that was not on the train");

        train1.setFirstWagon(sequence); train1.resetNumberOfWagons();
        train3.setFirstWagon(new FreightWagon(53, 5000)); train3.resetNumberOfWagons();
        assertFalse(Shunter.moveAllFromTrain(train1, train2, new FreightWagon(52, 4000)),
                "Have moved a wagon that was not on the train");
        assertEquals(3, train1.getNumberOfWagons());
        assertFalse(Shunter.moveAllFromTrain(train1, train2, sequence),
                "Have moved wagons without capacity at destination");
        assertEquals(3, train1.getNumberOfWagons());
        assertEquals(0, train2.getNumberOfWagons());
        assertFalse(Shunter.moveAllFromTrain(train1, train3, sequence),
                "Have moved passenger wagons to a freight train");
        assertEquals(3, train1.getNumberOfWagons());
        assertEquals(1, train3.getNumberOfWagons());

        assertTrue(Shunter.moveAllFromTrain(train1, train2, sequence.getNextWagon()),
                "Could not move a series of wagons within capacity of destination");
        assertEquals(1, train1.getNumberOfWagons());
        assertEquals(2, train2.getNumberOfWagons());
        assertTrue(Shunter.moveAllFromTrain(train2, train1, train2.getFirstWagon()),
                "Could not move back a single wagon");
        assertEquals(3, train1.getNumberOfWagons());
        assertEquals(0, train2.getNumberOfWagons());
    }
}
