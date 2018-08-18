package utn.frc.sim.generators.chicuadrado;

import utn.frc.sim.generators.RandomGenerator;
import utn.frc.sim.generators.congruential.CongruentialGenerator;
import utn.frc.sim.generators.javanative.JavaGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntervalsCreator {

    private List<Interval> intervals;
    private static final double TOP_VALUE = 0.9999;

    public IntervalsCreator(int amountOfNumbers, int amountOfIntervals, GeneratorType type) {
        createIntervals(amountOfIntervals, amountOfNumbers, type);
    }

    private void createIntervals(int amountOfIntervals, int amountOfNumbers, GeneratorType type) {

        if (amountOfNumbers % amountOfIntervals != 0) {
            throw new IllegalArgumentException();
        }

        RandomGenerator generator = getGenerator(type);
        intervals = new ArrayList<>();

        double step = TOP_VALUE / amountOfIntervals;
        int expectedFrequency = amountOfNumbers / amountOfIntervals;

        for (int i = 0; i < amountOfIntervals; i++) {
            Interval interval = new Interval(step * i, step * (i + 1), expectedFrequency);
            intervals.add(interval);
        }

        for (int i = 0; i < amountOfNumbers; i++) {
            double number = generator.random();
            intervals.stream()
                    .filter(it -> it.includes(number))
                    .findFirst()
                    .ifPresent(Interval::addOccurrence);
        }
    }

    private RandomGenerator getGenerator(GeneratorType type) {

        if (type == GeneratorType.JAVA_NATIVE) {
            return new JavaGenerator();
        } else if (type == GeneratorType.CONGRUENTIAL) {
            return CongruentialGenerator.defaultMixed();
        }
        throw new IllegalStateException();
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public enum GeneratorType {
        JAVA_NATIVE,
        CONGRUENTIAL
    }

}

