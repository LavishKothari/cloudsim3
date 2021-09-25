package org.cloudbus.cloudsim.mkg;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.ArrayList;
import java.util.List;

public class VmMkg extends Vm {

    /**
     * List of cloudlets scheduled on this VM
     */
    public List<Cloudlet> submittedCloudlets;

    /**
     * Wether vm is active or not
     */
    private boolean isActive;

    public VmMkg(int id, int userId, double mips, int numberOfPes, int ram,
                 long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
        setVmStatus(true);
        submittedCloudlets = new ArrayList<Cloudlet>();
    }

    /**
     * Checks if is active
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return isActive;
    }

    public int getid(int id) {
        return id;
    }

    /**
     * Sets the being instantiated.
     *
     * @param beingInstantiated the new being instantiated
     */
    public void setVmStatus(boolean status) {
        if (!status) {
            Log.printLine("------------------\nFAULT INJECTION\n------------------");
            Log.printLine(CloudSim.clock() + ":Setting Vm#" + getId() + " status to false");
            if (CloudSim.clock() > 0.2) {
                FailureParameters.FALT_INJECTION_TIME = CloudSim.clock();
            }
        }
        this.isActive = status;
    }

    /**
     * @return List of cloudlet submitted to be scheduled on this VM
     */
    public List<Cloudlet> getSubmittedCloudletList() {
        return this.submittedCloudlets;
    }

}
