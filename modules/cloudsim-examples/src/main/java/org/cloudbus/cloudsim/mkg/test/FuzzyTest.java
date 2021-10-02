package org.cloudbus.cloudsim.mkg.test;

import net.sourceforge.jFuzzyLogic.FIS;

import java.io.InputStream;

public class FuzzyTest {

    public static void main(String[] args) {

        compute(
                99,
                99,
                99,
                99
        );

    }

    private static void compute(
            int occupiedMips,
            int occupiedRam,
            long occupiedStorage,
            long occupiedBw
    ) {
        InputStream inputStream = FuzzyTest.class.getClassLoader().getResourceAsStream("fcl/fuzzytip.fcl");
        FIS fis = FIS.load(inputStream, true); // Load from 'FCL' file
        fis.setVariable("cpu", occupiedMips); // Set inputs
        fis.setVariable("memory", occupiedRam);
        fis.setVariable("disk", occupiedStorage);
        fis.setVariable("network", occupiedBw);
        fis.evaluate(); // Evaluate

        double risk = fis.getVariable("risk").getValue();
        System.out.println(risk);

    }

}
