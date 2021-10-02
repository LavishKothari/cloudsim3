package org.cloudbus.cloudsim.mkg;

public class FuzzyLogicConstants {

    // this class is not meant to be instantiated
    private FuzzyLogicConstants() {
        throw new RuntimeException("don't instantiate this class");
    }

    public static final double RISK_THRESHOLD_FOR_FAULT_DETECTION = 85.0;

    public static final double MONITORING_DELAY_AFTER_FLUCTUATE = 0.001;

}
