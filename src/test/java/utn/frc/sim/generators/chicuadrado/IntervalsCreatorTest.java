package utn.frc.sim.generators.chicuadrado;

import org.junit.Test;
import utn.frc.sim.generators.chicuadrado.exceptions.IntervalNotDivisibleException;
import utn.frc.sim.generators.congruential.CongruentialGenerator;
import utn.frc.sim.generators.javanative.JavaGenerator;

import static junit.framework.TestCase.assertEquals;

public class IntervalsCreatorTest {

    private IntervalsCreator creator;
    private static final int NUMBERS = 400;
    private static final int NUMBERS_FOR_FAIL = 401;
    private static final int INTERVALS = 20;



    @Test
    public void testJava() throws IntervalNotDivisibleException {
        creator = IntervalsCreator.createFor(NUMBERS, INTERVALS, JavaGenerator.defaultJava());
        creator.getIntervals()
                .forEach(interval -> assertEquals(interval.getExpectedFrequency(), NUMBERS / INTERVALS));
    }

    @Test
    public void testCongruential() throws IntervalNotDivisibleException {
        creator = IntervalsCreator.createFor(NUMBERS, INTERVALS, CongruentialGenerator.defaultMixed());
        creator.getIntervals()
                .forEach(interval -> assertEquals(interval.getExpectedFrequency(), NUMBERS / INTERVALS));
    }

    @Test(expected = IntervalNotDivisibleException.class)
    public void testIntervalNotDivisible() throws IntervalNotDivisibleException {
        creator = IntervalsCreator.createFor(NUMBERS_FOR_FAIL, INTERVALS, CongruentialGenerator.defaultMixed());
    }
}
