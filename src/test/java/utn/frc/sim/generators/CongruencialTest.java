package utn.frc.sim.generators;

import org.junit.Test;

public class CongruencialTest {

    @Test
    public void generateNumbers(){

        Generador generador = new Congruencial();
        for (int i = 0; i < 15; i++) {
            System.out.println(generador.next());

        }
    }


}
