package org.cloudbus.cloudsim.mkg;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.ArrayList;
import java.util.List;

public class HostMkg extends Host {
    /**
     * Instantiates a new host.
     *
     * @param id             the host id
     * @param ramProvisioner the ram provisioner
     * @param bwProvisioner  the bw provisioner
     * @param storage        the storage capacity
     * @param peList         the host's PEs list
     * @param vmScheduler    the vm scheduler
     */

    /**
     * The list of VMs assigned to the host.
     */
    private final List<? extends Vm> vmList = new ArrayList<Vm>();
    /**
     * The VMs migrating in.
     */
    private final List<Vm> vmsMigratingIn = new ArrayList<Vm>();
    /**
     * The id of the host.
     */
    private int id;
    /**
     * The storage capacity.
     */
    private long storage;
    /**
     * The ram provisioner.
     */
    private RamProvisioner ramProvisioner;
    /**
     * The bw provisioner.
     */
    private BwProvisioner bwProvisioner;
    /**
     * The allocation policy for scheduling VM execution.
     */
    private VmScheduler vmScheduler;
    /**
     * The Processing Elements (PEs) of the host, that
     * represent the CPU cores of it, and thus, its processing capacity.
     */
    private List<? extends Pe> peList;
    /**
     * Tells whether this host is working properly or has failed.
     */
    private boolean failed;
    /**
     * The datacenter where the host is placed.
     */
    private Datacenter datacenter;

    public HostMkg(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
    }

    public void setHostStatus(boolean status) {
        if (status == false) {
            Log.printLine("------------------\nFAULT INJECTION\n------------------");
            Log.printLine(CloudSim.clock() + ":Setting Pm#" + getId() + " status to false");
            if (CloudSim.clock() > 0.2) {
                FailureParameters.FALT_INJECTION_TIME = CloudSim.clock();
            }
        }
        this.failed = status;
    }

    @Override
    public long getStorage() {
        return storage;
    }

    @Override
    public void setStorage(long storage) {
        this.storage = storage;
    }

    @Override
    public RamProvisioner getRamProvisioner() {
        return ramProvisioner;
    }

    @Override
    public void setRamProvisioner(RamProvisioner ramProvisioner) {
        this.ramProvisioner = ramProvisioner;
    }

    @Override
    public BwProvisioner getBwProvisioner() {
        return bwProvisioner;
    }

    @Override
    public void setBwProvisioner(BwProvisioner bwProvisioner) {
        this.bwProvisioner = bwProvisioner;
    }
}
