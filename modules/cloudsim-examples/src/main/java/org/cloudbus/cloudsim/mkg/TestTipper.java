package org.cloudbus.cloudsim.mkg;

import net.sourceforge.jFuzzyLogic.FIS;

public class TestTipper {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "fuzzy.fcl";// use fuzzytip.fcl
        FIS fis = FIS.load(fileName, true);

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        // Show 
        // JFuzzyChart.get().chart(functionBlock);

        // Set inputs
        fis.setVariable("service", 3);
        fis.setVariable("food", 7);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        //Variable tip = functionBlock.getVariable("tip");
        // JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        // Print ruleSet
        //System.out.println(fis);
        System.out.println("Output value:" + fis.getVariable("tip").getValue());
        // Show each rule (and degree of support)

    }
}
