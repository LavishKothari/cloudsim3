package org.cloudbus.cloudsim.mkg;

public class HostParameter {
    private final int occupiedMips;
    private final int occupiedRam;
    private final long occupiedStorage;
    private final long occupiedBw;

    public HostParameter(int occupiedMips, int occupiedRam, long occupiedStorage, long occupiedBw) {
        this.occupiedMips = occupiedMips;
        this.occupiedRam = occupiedRam;
        this.occupiedStorage = occupiedStorage;
        this.occupiedBw = occupiedBw;
    }

    public int getOccupiedMips() {
        return occupiedMips;
    }

    public int getOccupiedRam() {
        return occupiedRam;
    }

    public long getOccupiedStorage() {
        return occupiedStorage;
    }

    public long getOccupiedBw() {
        return occupiedBw;
    }

    @Override
    public String toString() {
        return "HostParameter{" +
                "occupiedMips=" + occupiedMips +
                ", occupiedRam=" + occupiedRam +
                ", occupiedStorage=" + occupiedStorage +
                ", occupiedBw=" + occupiedBw +
                '}';
    }
}
