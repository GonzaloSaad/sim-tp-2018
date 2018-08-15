package utn.frc.sim.generators.chicuadrado;

import org.junit.Test;

public class IntervalsCreatorTest {

    private IntervalsCreator creator;
    private static final int NUMBERS = 400;
    private static final int INTERVALS = 20;



    @Test
    public void testJava(){
        creator = new IntervalsCreator(NUMBERS, INTERVALS, IntervalsCreator.GeneratorType.JAVA_NATIVE);
        creator.getIntervals().forEach(System.out::println);
    }

    @Test
    public void testCongruential(){
        creator = new IntervalsCreator(NUMBERS, INTERVALS, IntervalsCreator.GeneratorType.CONGRUENTIAL);
        creator.getIntervals().forEach(System.out::println);
    }





}
