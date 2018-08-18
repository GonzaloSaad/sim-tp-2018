package utn.frc.sim.generators.chicuadrado;

import org.junit.Test;
import utn.frc.sim.generators.chicuadrado.exceptions.IntervalNotDivisibleException;

import static junit.framework.TestCase.assertEquals;

public class IntervalsCreatorTest {

    private IntervalsCreator creator;
    private static final int NUMBERS = 400;
    private static final int NUMBERS_FOR_FAIL = 401;

    private static final int INTERVALS = 20;



    @Test
    public void testJava() throws IntervalNotDivisibleException {
        creator = new IntervalsCreator(NUMBERS, INTERVALS, IntervalsCreator.GeneratorType.JAVA_NATIVE);
        creator.getIntervals()
                .forEach(interval -> assertEquals(interval.getExpectedFrequency(), NUMBERS / INTERVALS));
    }

    @Test
    public void testCongruential() throws IntervalNotDivisibleException {
        creator = new IntervalsCreator(NUMBERS, INTERVALS, IntervalsCreator.GeneratorType.CONGRUENTIAL);
        creator.getIntervals()
                .forEach(interval -> assertEquals(interval.getExpectedFrequency(), NUMBERS / INTERVALS));
    }

    @Test(expected = IntervalNotDivisibleException.class)
    public void testIntervalNotDivisible() throws IntervalNotDivisibleException {
        creator = new IntervalsCreator(NUMBERS_FOR_FAIL, INTERVALS, IntervalsCreator.GeneratorType.CONGRUENTIAL);
    }
}
