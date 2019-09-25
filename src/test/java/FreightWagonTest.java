import model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FreightWagonTest {
    @Test
    void testCanInitialize() {
        FreightWagon w = new FreightWagon(1, 100);
        assertEquals(1, w.getWagonId());
        assertEquals(100, w.getMaxWeight());
    }
}
