package utn.frc.sim.generators;

import java.util.List;

public interface Generator extends RandomGenerator {
    int next();
    List<Integer> next(int amount);
    int getSeed();
    void restart();
}
