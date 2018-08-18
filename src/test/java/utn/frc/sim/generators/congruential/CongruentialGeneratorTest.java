package utn.frc.sim.generators.congruential;

import org.junit.Test;
import utn.frc.sim.generators.congruential.Congruential;
import utn.frc.sim.generators.congruential.CongruentialGenerator;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CongruentialGeneratorTest {

    /*
        Numbers created with: https://www.mathcelebrity.com/linear-congruential-generator-calculator.php
     */

    private static Integer[] mixed = new Integer[]{1, 10, 73, 19, 37, 64, 55,
            91, 46, 28, 1, 10, 73, 19, 37, 64, 55, 91, 46, 28, 1, 10, 73, 19,
            37, 64, 55, 91, 46, 28, 1, 10, 73, 19, 37, 64, 55, 91, 46, 28, 1,
            10, 73, 19, 37, 64, 55, 91, 46, 28, 1, 10, 73, 19, 37, 64, 55, 91,
            46, 28, 1, 10, 73, 19, 37, 64, 55, 91, 46, 28, 1, 10, 73, 19, 37,
            64, 55, 91, 46, 28, 1, 10, 73, 19, 37, 64, 55, 91, 46, 28, 1, 10,
            73, 19, 37, 64, 55, 91, 46};

    private static Integer[] multiplicative = new Integer[]{1, 7, 49, 46, 25, 76, 37, 61,
            31, 19, 34, 40, 82, 79, 58, 10, 70, 94, 64, 52, 67, 73, 16, 13, 91, 43, 4, 28, 97, 85, 1,
            7, 49, 46, 25, 76, 37, 61, 31, 19, 34, 40, 82, 79, 58, 10, 70, 94, 64, 52, 67, 73, 16, 13,
            91, 43, 4, 28, 97, 85, 1, 7, 49, 46, 25, 76, 37, 61, 31, 19, 34, 40, 82, 79, 58, 10, 70, 94,
            64, 52, 67, 73, 16, 13, 91, 43, 4, 28, 97, 85, 1, 7, 49, 46, 25, 76, 37, 61, 31};

    private static final int A = 7;
    private static final int M = 99;
    private static final int SEED = 1;
    private static final int MIXED_C = 3;
    private static final int MULTIPLICATIVE_C = 0;


    @Test
    public void mixedGeneration() {
        Congruential generator = CongruentialGenerator.createOf(A, M, MIXED_C, SEED);

        assertTrue(generator.isMixed());
        assertFalse(generator.isMultiplicative());

        List<Integer> numbers = generator.next(mixed.length);
        List<Integer> expected = Arrays.asList(mixed);
        assertEquals(numbers, expected);
    }

    @Test
    public void multiplicativeGeneration() {
        Congruential generator = CongruentialGenerator.createOf(A, M, MULTIPLICATIVE_C, SEED);

        assertFalse(generator.isMixed());
        assertTrue(generator.isMultiplicative());

        List<Integer> numbers = generator.next(multiplicative.length);
        List<Integer> expected = Arrays.asList(multiplicative);
        assertEquals(numbers, expected);

    }


}
