package utn.frc.sim.generators;

import org.junit.Test;
import utn.frc.sim.generators.congruential.Congruential;
import utn.frc.sim.generators.congruential.CongruentialGenerator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CongruentialGeneratorTest {

    @Test
    public void multiplicativeGeneration() {
        Congruential generator = CongruentialGenerator.defaultMultiplicative();
        assertFalse(generator.isMixed());
        assertTrue(generator.isMultiplicative());
    }

    @Test
    public void mixedGeneration() {
        Congruential generator = CongruentialGenerator.defaultMixed();
        assertTrue(generator.isMixed());
        assertFalse(generator.isMultiplicative());

    }


}
