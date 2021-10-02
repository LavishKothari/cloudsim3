package org.cloudbus.cloudsim.mkg;

import java.util.HashMap;
import java.util.Map;

public class HostFluctuatingParameters {

    // This class is not meant to be instantiated
    private HostFluctuatingParameters() {
        throw new RuntimeException("Don't instantiate this class");
    }

    public static final Map<Double, HostParameter> PARAMETER_MAP = new HashMap<>();

    /*
        int mips = 3720;
        int ram = 2048; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;
    * */
    static {
        PARAMETER_MAP.put(1.52, new HostParameter(380 / 2, 587 / 2, 1500 / 2, 400 / 2));
        PARAMETER_MAP.put(1.67, new HostParameter(500 / 2, 1234 / 2, 2000 / 2, 1000 / 2));
        PARAMETER_MAP.put(1.74, new HostParameter(800 / 2, 1298 / 2, 3000 / 2, 1500 / 2));
        PARAMETER_MAP.put(1.79, new HostParameter(1000 / 2, 1545 / 2, 10000 / 2, 1800 / 2));
        PARAMETER_MAP.put(1.81, new HostParameter(1800 / 2, 1762 / 2, 15000 / 2, 2500 / 2));
        PARAMETER_MAP.put(1.88, new HostParameter(2000 / 2, 1873 / 2, 20000 / 2, 3500 / 2));
        PARAMETER_MAP.put(1.92, new HostParameter(3000 / 2, 1984 / 2, 979990 / 2, 8500 / 2));
        PARAMETER_MAP.put(1.94, new HostParameter(3700 / 2, 2046 / 2, 999990 / 2, 9900 / 2));
    }

}
