package utn.frc.sim.generators;

import java.util.List;

public interface Generator {

    int next();
    List<Integer> next(int amount);
    double random();
    List<Double> random(int n);
    int getSeed();
    void restart();
}
