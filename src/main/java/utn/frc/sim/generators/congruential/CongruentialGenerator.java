package utn.frc.sim.generators.congruential;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CongruentialGenerator implements Congruential {

    private int a;
    private int m;
    private int c;
    private int x;
    private int seed;

    private CongruentialGenerator(int a, int m, int c, int seed) {
        this.a = a;
        this.m = m;
        this.c = c;
        this.seed = seed;
        this.x = seed;
    }

    private CongruentialGenerator(int c) {
        this(DEFAULT_A, DEFAULT_M, c, DEFAULT_SEED);
    }

    /**
     * Metodo que instancia a la clase con los valores necesarios.
     * Patron factory method.
     */
    public static CongruentialGenerator createOf(int a, int m, int c, int seed) {
        return new CongruentialGenerator(a, m, c, seed);
    }

    /**
     * Metodo que crea una instancia con valores por defecto para mixto.
     */
    public static CongruentialGenerator defaultMixed() {
        return new CongruentialGenerator(MIXED_CG_DEFAULT_C);
    }

    /**
     * Metodo que crea una instancia con valores por defecto para multiplicativo.
     */
    public static CongruentialGenerator defaultMultiplicative() {
        return new CongruentialGenerator(MULTIPLICATIVE_CG_DEFAULT_C);
    }

    /**
     * Metodo que realiza el calculo de un generador
     * pseudoaleatorio congruencial.
     * x = (a * xn + c) % m
     * La serie a generar debe comenzar por la semilla
     * por eso se retorna el valor viejo (xn).
     */
    @Override
    public int next() {
        int xn = x;
        x = BigInteger
                .valueOf(c + a * x)
                .mod(BigInteger.valueOf(m))
                .intValue();

        return xn;
    }

    /**
     * Metodo que genera una cantidad 'amount' de
     * numeros pseudoaleatorios.
     */
    @Override
    public List<Integer> next(int amount) {
        List<Integer> generatedNumbers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            generatedNumbers.add(next());
        }
        return generatedNumbers;
    }

    /**
     * Metodo que genera un numero pseudoaleatorio
     * entre 0 y 1.
     * Intervalo -> [0,1)
     */
    @Override
    public double random() {
        return normalize(next());
    }

    /**
     * Metodo que genera una cantidad 'n' de
     * numeros pseudoaleatorios.
     */
    @Override
    public List<Double> random(int n) {
        return next(n)
                .stream()
                .map(this::normalize)
                .collect(toList());
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void restart() {
        x = seed;
    }

    @Override
    public boolean isMultiplicative() {
        return c == 0;
    }

    @Override
    public boolean isMixed() {
        return c != 0;
    }

    /**
     * Metodo que convierte un numero entero
     * generado por la serie a un valor
     * del intervalo [0,1)
     */
    private double normalize(int n) {
        return (double) n / (double) m;
    }
}
