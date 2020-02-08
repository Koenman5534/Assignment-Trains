import org.junit.jupiter.api.*;

import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShunterTest {
    @Test
    void testHookWagonOnTrainRear()
    {
        Train t = new Train(new Locomotive(1, 1), "Amsterdam", "Haarlem");
        PassengerWagon w = new PassengerWagon(1, 100);
        t.setFirstWagon(w);

        // test if another passenger wagon can be attached
        assertTrue(Shunter.hookWagonOnTrainRear(t, new PassengerWagon(2, 100)));
        // test if a freight wagon can be attached
        assertFalse(Shunter.hookWagonOnTrainRear(t, new FreightWagon(2, 100)));
        // test return false if hooking up another wagon exceeds the maximum number of wagons
        // the train can handle, which is 1 (defined in the locomotive)
        PassengerWagon w2 = new PassengerWagon(2, 100);
        w.setNextWagon(w2);
        assertFalse(Shunter.hookWagonOnTrainRear(t, w2));
    }

    @Test
    void testHookWagonFront()
    {
        Train t = new Train(new Locomotive(1, 1), "Amsterdam", "Haarlem");
        PassengerWagon w = new PassengerWagon(1, 100);
        t.setFirstWagon(w);

        // test if another passenger wagon can be attached
        assertTrue(Shunter.hookWagonOnTrainFront(t, new PassengerWagon(2, 100)));

        // test if a freight wagon can be attached
        assertFalse(Shunter.hookWagonOnTrainFront(t, new FreightWagon(2, 100)));

        // test return false if hooking up another wagon exceeds the maximum number of wagons
        // the train can handle, which is 1 (defined in the locomotive)
        PassengerWagon w2 = new PassengerWagon(2, 100);
        w.setNextWagon(w2);
        assertFalse(Shunter.hookWagonOnTrainFront(t, w2));
    }

    @Test
    void testHookWagonOnWagon()
    {
        Train t = new Train(new Locomotive(1, 1), "Amsterdam", "Haarlem");
        PassengerWagon w = new PassengerWagon(1, 100);

        assertFalse(Shunter.hookWagonOnWagon(w, new FreightWagon(0, 0)));
        assertTrue(Shunter.hookWagonOnWagon(w, new PassengerWagon(0, 0)));
    }

    @Test
    void testDetachingOfWagon()
    {
        Train t = new Train(new Locomotive(1, 3), "Amsterdam", "Haarlem");
        PassengerWagon w1 = new PassengerWagon(1, 100);
        PassengerWagon w2 = new PassengerWagon(2, 100);
        PassengerWagon w3 = new PassengerWagon(3, 100);
        t.setFirstWagon(w1);

        Shunter.hookWagonOnWagon(w1, w2);
        Shunter.hookWagonOnWagon(w2, w3);
        t.resetNumberOfWagons();

        //Check if they are added
        assertEquals(3, t.getNumberOfWagons());

        //Remove the middle one
        Shunter.detachOneWagon(t, w2);
        assertEquals(2, t.getNumberOfWagons());
        Shunter.detachAllFromTrain(t, w1);
        assertEquals(0, t.getNumberOfWagons());
    }

    @Test
    void testMovingOfWagons()
    {
        Train t1 = new Train(new Locomotive(1, 3), "Amsterdam", "Haarlem");
        Train t2 = new Train(new Locomotive(1, 3), "Amsterdam", "Haarlem");
        PassengerWagon w1 = new PassengerWagon(1, 100);
        PassengerWagon w2 = new PassengerWagon(2, 100);
        PassengerWagon w3 = new PassengerWagon(3, 100);
        t1.setFirstWagon(w1);

        Shunter.hookWagonOnWagon(w1, w2);
        Shunter.hookWagonOnWagon(w2, w3);
        t1.resetNumberOfWagons();

        Shunter.moveOneWagon(t1, t2, w3);

        assertEquals(3, t2.getFirstWagon().getWagonId());
        Shunter.moveAllFromTrain(t1,t2, w1);

        assertEquals(0, t1.getNumberOfWagons());
    }
}
