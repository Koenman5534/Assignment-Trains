import model.PassengerWagon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WagonTest {
    @Test
    void testGetLastWagonAttached() {
        // test returning the wagon itself if there are no wagons attached
        PassengerWagon w = new PassengerWagon(1, 100);
        assertEquals(w, w.getLastWagonAttached());
        PassengerWagon aw1 = new PassengerWagon(2, 100);
        aw1.setPreviousWagon(w);
        PassengerWagon aw2 = new PassengerWagon(3, 100);
        aw2.setPreviousWagon(aw1);

        // test there is only one wagon attached
        w.setNextWagon(aw1);
        assertEquals(aw1, w.getLastWagonAttached());
        // test there are multiple wagons attached
        aw1.setNextWagon(aw2);
        assertEquals(aw2, w.getLastWagonAttached());
    }
}
