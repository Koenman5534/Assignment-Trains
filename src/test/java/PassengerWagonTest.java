import model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassengerWagonTest {
    @Test
    void testCanInitialize() {
        PassengerWagon w = new PassengerWagon(1, 100);
        assertEquals(1, w.getWagonId());
        assertEquals(100, w.getNumberOfSeats());
    }
}
