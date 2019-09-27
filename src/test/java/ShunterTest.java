import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

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
}
