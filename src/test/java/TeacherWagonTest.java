import model.FreightWagon;
import model.PassengerWagon;
import model.Wagon;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherWagonTest {

    @Test
    public void passengerWagonShouldReportCorrectId() {
        PassengerWagon wagon = new PassengerWagon(13, 0);

        assertEquals(13, wagon.getWagonId());
    }

    @Test
    public void freightWagonShouldReportCorrectId() {
        FreightWagon wagon = new FreightWagon(13, 0);

        assertEquals(13, wagon.getWagonId());
    }

    @Test
    public void shouldReportSelfAsLastWagon() {
        Wagon wagon = new PassengerWagon(0,0);

        assertEquals(wagon, wagon.getLastWagonAttached());
    }

    @Test
    public void shouldReportNextAsLastWagon() {
        Wagon wagon = new PassengerWagon(0,0);
        Wagon nextWagon = new PassengerWagon(0, 0);
        wagon.setNextWagon(nextWagon);

        assertEquals(nextWagon, wagon.getLastWagonAttached());
    }

    @Test
    public void shouldReportNextOfNextAsLastWagon() {
        Wagon wagon = new PassengerWagon(0,0);
        Wagon nextWagon = new PassengerWagon(0, 0);
        wagon.setNextWagon(nextWagon);
        Wagon nextOfNextWagon = new PassengerWagon(0, 0);
        nextWagon.setNextWagon(nextOfNextWagon);

        assertEquals(nextOfNextWagon, wagon.getLastWagonAttached());
    }

    @Test
    public void singleWagonHasNoNeighbours() {
        Wagon wagon = new FreightWagon(0, 0);

        assertNull(wagon.getPreviousWagon());
        assertNull(wagon.getNextWagon());
    }

    @Test
    public void wagonsShouldCorrectlyBeAttached() {
        Wagon previous = new FreightWagon(0, 0);
        Wagon wagon = new FreightWagon(0, 0);
        Wagon next = new FreightWagon(0, 0);

        previous.setNextWagon(wagon);
        wagon.setNextWagon(next);

        assertNull(previous.getPreviousWagon());
        assertEquals(wagon, previous.getNextWagon());

        assertEquals(previous, wagon.getPreviousWagon());
        assertEquals(next, wagon.getNextWagon());

        assertEquals(wagon, next.getPreviousWagon());
        assertNull(next.getNextWagon());
    }

    @Test
    public void lastWagonHasNoAttachedWagons() {
        Wagon wagon = new PassengerWagon(0, 0);

        assertEquals(0, wagon.getNumberOfWagonsAttached());
    }

    @Test
    public void differentWagonTypesShouldBeAttachedConsistently() {
        Wagon first = new PassengerWagon(1, 0);
        Wagon second = new FreightWagon(2, 0);

        first.setNextWagon(second);

        assertNull(first.getPreviousWagon());
        if (first.getNextWagon() != null || second.getPreviousWagon() != null) {
            assertEquals(second, first.getNextWagon());
            assertEquals(first, second.getPreviousWagon());
        }
        assertNull(second.getNextWagon());
    }

    @Test
    public void firstOfThreeWagonsHasTwoAttachedWagons() {
        Wagon first = new PassengerWagon(1, 0);
        Wagon middle = new PassengerWagon(2, 0);
        Wagon last = new PassengerWagon(3, 0);

        middle.setNextWagon(last);
        first.setNextWagon(middle);

        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(2,first.getNumberOfWagonsAttached());
        });
    }

    @Test
    public void checkToStringToUseWagonId() {
        Wagon wagon = new PassengerWagon(123, 321);

        assertEquals("[Wagon 123]", wagon.toString());
    }

    @Test
    public void checkWagonCannotBeInstantiated() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        assertThrows(InstantiationException.class, () -> {
                Class wClass = Wagon.class;
                Constructor wCons = wClass.getConstructor(new Class[]{int.class});
                Wagon wagon = (Wagon)wCons.newInstance(1);
        });
    }
}
