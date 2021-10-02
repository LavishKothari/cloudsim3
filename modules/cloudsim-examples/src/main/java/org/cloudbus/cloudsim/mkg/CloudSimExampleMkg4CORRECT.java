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
 * Simulation with 5 VMs and 10 CLs. One random VM is failed before any
 * cloudlet is assigned onto VM by the broker.
 */
public class CloudSimExampleMkg4CORRECT {

    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<VmMkg> vmlist;

    private static List<VmMkg> createVM(int userId, int vms, int idShift) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<VmMkg> list = new LinkedList<VmMkg>();

        //VM Parameters
        long size = 1000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 250;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        VmMkg[] vm = new VmMkg[vms];

        for (int i = 0; i < vms; i++) {
            vm[i] = new VmMkg(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeSharedMkg());
            list.add(vm[i]);
        }

        return list;
    }


    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 400;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        int lengthIncr = 300;
        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            cloudlet[i] = new Cloudlet(idShift + i, length + lengthIncr, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            list.add(cloudlet[i]);
            lengthIncr += 50;
        }

        return list;
    }


    ////////////////////////// STATIC METHODS ///////////////////////

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        Log.printLine("Starting CloudSimExampleAtul1...");
        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 2;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            @SuppressWarnings("unused")

            final DatacenterMkg datacenter0 = createDatacenter("Datacenter_0");
            @SuppressWarnings("unused")
            DatacenterMkg datacenter1 = createDatacenter("Datacenter_1");

            //Third step: Create Broker
            final DatacenterBrokerMkg broker = createBroker("Broker_0");
            int brokerId = broker.getId();


            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmlist = createVM(brokerId, 20, 0); //creating 5 vms
            cloudletList = createCloudlet(brokerId, 40, 0); // creating 10 cloudlets
            initCSVFiles();
            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);


            /** EDIT THIS to Fail a VM before simulation starts */
            failVMsBeforeSimulationStarts();
            datacenter0.setFailureParameters(vmlist, FailureParameters.FAIL_SINGLE_VM, FailureParameters.STATIC_DELAY, 19.0, null);

            //mkg testing

            List<VmMkg> vmFailList = new ArrayList<VmMkg>();
            vmFailList.add(vmlist.get(0));
            vmFailList.add(vmlist.get(1));
            //	datacenter0.setFailureParameters(vmlist,FailureParameters.FAIL_SINGLE_VM, FailureParameters.RANDOM_DELAY, 19.0, null);


            //code from other file vmFailList.add(vmlist.get(1));
            //datacenter0.setFailureParameters(vmlist,FailureParameters.FAIL_MULTI_VM, FailureParameters.RANDOM_DELAY, 2.0, vmFailList);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();
            //Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            List<Cloudlet> newList1 = broker.getCloudletSubmittedList();

            CloudSim.stopSimulation();

            //printCloudletList(newList);
            printCloudletList(newList1);
            //printCloudletList(cloudletList);
            ReportGenerator generator = new ReportGenerator(broker.getCloudletSubmittedList());
            generator.generateHTMLFile();
            generator.generateCloudletReport();
            //RG r = new RG(broker.getCloudletSubmittedList());
            //r.generateCloudletReport();
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
            f.append("ClockTick,Cloudlet ID,Status,VM ID,Remaining Cloudlet Length,Cloudlet Finished So Far,EstimatedFinishTime\n");
            f.close();
        }
    }


    private static DatacenterMkg createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        List<Pe> peList1 = new ArrayList<Pe>();

        int mips = 1000;

        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

        //Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<Pe>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId = 0;
        int ram = 16384; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList1,
                        new VmSchedulerTimeShared(peList1)
                )
        ); // This is our first machine

        hostId++;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList2,
                        new VmSchedulerTimeShared(peList2)
                )
        ); // Second machine


        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;        // the cost of using memory in this resource
        double costPerStorage = 0.1;    // the cost of using storage in this resource
        double costPerBw = 0.1;            // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();    //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        DatacenterMkg datacenter = null;
        try {
            datacenter = new DatacenterMkg(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, ConstsMkg.SCHEDULING_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static DatacenterBrokerMkg createBroker(String name) {

        DatacenterBrokerMkg broker = null;
        try {
            broker = new DatacenterBrokerMkg(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
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
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                //Log.print(cloudlet.getCloudletStatus()+""+indent);

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            } else if (cloudlet.getCloudletStatus() == Cloudlet.FAILED) {
                Log.print("FAILED ");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }
}
