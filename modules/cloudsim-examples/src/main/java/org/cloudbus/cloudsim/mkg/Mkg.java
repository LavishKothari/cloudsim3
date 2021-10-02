/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.mkg;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Simulation with 5 VMs and 10 CLs. One random VM is failed before any cloudlet
 * is assigned onto VM by the broker.
 */
public class Mkg {

    private static final String DATACENTER_0 = "Datacenter_0";
    private static final String BROKER_0 = "Broker_0";
    private static final int BROKER_ID = 0;
    private static final int VM_ID_POINTER = 0;
    private static final int VM_COUNT = 2;
    private static final int CLOUDLET_COUNT = 10;
    private static final int CLOUDLET_ID_POINTER = 0;
    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;
    /**
     * The vmlist.
     */
    private static List<VmMkg> vmlist;

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        Log.printLine("Starting the cloudsimulation for faults ");
        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 2; // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false; // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);
            Mkg ftp = new Mkg();

            // we will assume that only 1 datacenter will part of our usecase.
            DatacenterMkg datacenter0 = ftp.createDatacenter(DATACENTER_0, 2, 4, 1000);
            /*
             * @SuppressWarnings("unused") DatacenterMkg datacenter1 =
             * createDatacenter("Datacenter_1");
             */
            // Third step: Create Broker
            final DatacenterBrokerMkg broker = ftp.createBroker(BROKER_0, BROKER_ID);
            int brokerId = broker.getId();

            // Fourth step: Create VMs and Cloudlets and send them to broker
            int vmHigh = 50;
            int vmMid = 30;

            vmlist = ftp.createVM(brokerId, VM_COUNT, VM_ID_POINTER, vmHigh, vmMid);
            cloudletList = ftp.createCloudlet(brokerId, CLOUDLET_COUNT, CLOUDLET_ID_POINTER); // creating 10 cloudlets

            //initCSVFiles();
            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);

            //TODO

            /** EDIT THIS to Fail a VM before simulation starts */
            //failVMsBeforeSimulationStarts();
            //	datacenter0.setFailureParameters(vmlist, FailureParameters.FAIL_SINGLE_VM, FailureParameters.STATIC_DELAY,
            //			19.0, null);

            // mkg testing

            //	List<VmMkg> vmFailList = new ArrayList<VmMkg>();
            //	vmFailList.add(vmlist.get(0));
            //	vmFailList.add(vmlist.get(1));
            //TODO
            //	datacenter0.setFailureParameters(vmlist, FailureParameters.FAIL_SINGLE_VM, FailureParameters.RANDOM_DELAY,
            //			19.0, null);

            // code from other file vmFailList.add(vmlist.get(1));
            // datacenter0.setFailureParameters(vmlist,FailureParameters.FAIL_MULTI_VM,
            // FailureParameters.RANDOM_DELAY, 2.0, vmFailList);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();
            // Final step: Print results when simulation is over
            // List<Cloudlet> newList = broker.getCloudletReceivedList();
            List<Cloudlet> newList1 = broker.getCloudletSubmittedList();

            CloudSim.stopSimulation();

            // printCloudletList(newList);
            printCloudletList(newList1);
            // printCloudletList(cloudletList);
            ReportGenerator generator = new ReportGenerator(broker.getCloudletSubmittedList());
            generator.generateHTMLFile();
            generator.generateCloudletReport();
            // RG r = new RG(broker.getCloudletSubmittedList());
            // r.generateCloudletReport();
            Log.printLine("Fault Injection Time = " + FailureParameters.FALT_INJECTION_TIME);
            if (ConstsMkg.DETECTION_ALGORITHM == 1) {
                Log.printLine("Fault Detection Time(Algorithm 1) = " + FailureParameters.FAULT_DETECTION_TIME);
            } else if (ConstsMkg.DETECTION_ALGORITHM == 2) {
                Log.printLine("Fault Detection Time(Algorithm 2) = " + FailureParameters.FAULT_DETECTION_TIME);
            }
            Log.printLine("Number of Times Monitor was called = " + FailureParameters.NO_OF_MONITOR_CALLS);
            Log.printLine("CloudSimExampleAtul4 finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    /**
     * Fail a VM before the start of simulation.
     */
    private static void failVMsBeforeSimulationStarts() {
        int numberOfVmsToFail = 1;
        if (numberOfVmsToFail > vmlist.size()) {
            Log.printLine(CloudSim.clock() + ":[ERROR]: Number of Vms to Fails is greater than number of Vms Created!");
            return;
        }
        List<VmMkg> toFail = FailureParameters.getRandomVMsToFail(vmlist, numberOfVmsToFail);
        for (int i = 0; i < toFail.size(); i++) {
            VmMkg vm2 = toFail.get(i);
            vm2.setVmStatus(false);
        }
    }

    ////////////////////////// STATIC METHODS ///////////////////////

    /**
     * Initializes and flushes the CSV files that are created for every cloudlet.
     *
     * @throws IOException
     */
    private static void initCSVFiles() throws IOException {
        for (int i = 0; i < cloudletList.size(); i++) {
            Cloudlet cloudlet = cloudletList.get(i);
            FileWriter f = new FileWriter("Cloudlet" + cloudlet.getCloudletId() + ".csv");
            f.write("");
            f.append(
                    "ClockTick,Cloudlet ID,Status,VM ID,Remaining Cloudlet Length,Cloudlet Finished So Far,EstimatedFinishTime\n");
            f.close();
        }
    }

    private static Pe createPE(int mips) {
        return new Pe(0, new PeProvisionerSimple(mips));

    }

    private static DatacenterCharacteristics createDataCenterCharacteristics(List<Host> hostList) {
        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.1; // the cost of using storage in this resource
        double costPerBw = 0.1; // the cost of using bw in this resource

        return new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone,
                cost, costPerMem, costPerStorage, costPerBw);

    }

    private static Host createHost(int hostId, int ram, long storage, int bw, List<Pe> peList) {
        return new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList,
                new VmSchedulerTimeShared(peList));

    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + indent
                + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                // Log.print(cloudlet.getCloudletStatus()+""+indent);

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + indent
                        + cloudlet.getVmId() + indent + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            } else if (cloudlet.getCloudletStatus() == Cloudlet.FAILED) {
                Log.print("FAILED ");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + indent
                        + cloudlet.getVmId() + indent + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }

    }

    private List<VmMkg> createVM(int userId, int vmCount, int idShift, int vmHigh, int vmMid) {
        // Creates a container to store VMs. This list is passed to the broker later
        LinkedList<VmMkg> list = new LinkedList<VmMkg>();

        // VM Parameters
        long size = 10000; // image size (MB)
        int ram = 512; // vm memory (MB)
        int mips = 250;
        long bw = 1000;
        int pesNumber = 1; // number of cpus
        String vmm = "Xen"; // VMM name
        // create VMs
        VmMkg[] vm = new VmMkg[vmCount];
        for (int i = 0; i < vmCount; i++) {
            vm[i] = new VmMkg(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm,
                    new CloudletSchedulerTimeSharedMkg());
            list.add(vm[i]);
        }

        return list;
    }

    private List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        // cloudlet parameters
        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        int lengthIncr = 300;
        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            cloudlet[i] = new Cloudlet(idShift + i, length + lengthIncr, pesNumber, fileSize, outputSize,
                    utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            list.add(cloudlet[i]);
            lengthIncr += 50;
        }

        return list;
    }

    private DatacenterMkg createDatacenter(String name, int totalHost, int nPePerHost, int mips) {

        int hostId = 0;
        int ram = 16384; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;
        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        // Machines
        List<Host> hostList = new ArrayList<Host>();
        for (int nHost = 0; nHost < totalHost; nHost++) {
            List<Pe> peList = new ArrayList<Pe>();
            for (int nPe = 0; nPe < nPePerHost; nPe++) {
                peList.add(createPE(mips));
            }
            hostList.add(createHost(hostId, ram, storage, bw, peList));
        }


        DatacenterCharacteristics characteristics = createDataCenterCharacteristics(hostList);
        // 6. Finally, we need to create a PowerDatacenter object.
        DatacenterMkg datacenter = null;
        try {
            LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now

            datacenter = new DatacenterMkg(name, characteristics, new VmAllocationPolicySimple(hostList), storageList,
                    ConstsMkg.SCHEDULING_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    // We strongly encourage users to develop their own broker policies, to submit
    // vms and cloudlets according
    // to the specific rules of the simulated scenario
    private DatacenterBrokerMkg createBroker(String name, int id) throws Exception {
        DatacenterBrokerMkg broker = new DatacenterBrokerMkg(name);
        //	broker.setId(id);
        return broker;
    }
}
