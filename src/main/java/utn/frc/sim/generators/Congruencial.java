package utn.frc.sim.generators;

import java.util.Random;

public class Congruencial implements Generador {


    private int a;
    private int m;
    private int c;
    private int x;
    private int seed;

    public Congruencial(int a, int m, int c, int seed) {
        this.a = a;
        this.m = m;
        this.c = c;
        this.seed = seed;
        this.x = seed;
    }

    public Congruencial() {
        this(7, 1000, 501, 0);
    }

    public int next() {
        x = (c + a * x) % m;
        return x;
    }


}
