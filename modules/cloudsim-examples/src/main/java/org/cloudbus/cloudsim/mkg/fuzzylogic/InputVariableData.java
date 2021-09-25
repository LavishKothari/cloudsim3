package org.cloudbus.cloudsim.mkg.fuzzylogic;


public class InputVariableData {

    private int cpuLoad;
    private int memoryPercentage;
    private int cpuTemperature;
    private int diskSpace;

    InputVariableData(int cpuLoad, int memoryPercentage, int cpuTemperature, int diskSpace) {
        this.cpuLoad = cpuLoad;
        this.cpuTemperature = cpuTemperature;
        this.memoryPercentage = memoryPercentage;
        this.diskSpace = diskSpace;
    }

    public int getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(int cpuLoad) {
        this.cpuLoad = cpuLoad;
    }


    public int getCpuTemperature() {
        return cpuTemperature;
    }

    public void setCpuTemperature(int cpuTemperature) {
        this.cpuTemperature = cpuTemperature;
    }

    public int getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(int diskSpace) {
        this.diskSpace = diskSpace;
    }

    public int getMemoryPercentage() {
        return memoryPercentage;
    }

    public void setMemoryPercentage(int memoryPercentage) {
        this.memoryPercentage = memoryPercentage;
    }
}
