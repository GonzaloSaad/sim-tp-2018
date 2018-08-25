package utn.frc.sim.generators.javanative;

import utn.frc.sim.generators.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class JavaGenerator implements RandomGenerator {

    private JavaGenerator() {

    }

    public static JavaGenerator defaultJava() {
        return new JavaGenerator();
    }

    @Override
    public double random() {
        return Math.random();
    }

    @Override
    public List<Double> random(int n) {
        List<Double> generatedNumbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            generatedNumbers.add(random());
        }
        return generatedNumbers;
    }
}
