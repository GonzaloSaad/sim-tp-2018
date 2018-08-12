package utn.frc.sim.generators.congruential;

import utn.frc.sim.generators.Generator;

public interface Congruential extends Generator {
    int DEFAULT_A = 22695477;
    int DEFAULT_M = Integer.MAX_VALUE;
    int DEFAULT_SEED = 1;
    int MIXED_CG_DEFAULT_C = 1;
    int MULTIPLICATIVE_CG_DEFAULT_C = 0;

    boolean isMultiplicative();
    boolean isMixed();
}
