/*
33 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */


package org.cloudbus.cloudsim.mkg;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.network.TopologicalGraph;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * Simulation with x VMs and y CLs. One random VM is failed before any
 * cloudlet is assigned onto VM by the broker.
 */
public class CloudSimExampleMkg4CHECK {

    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;
    static List<VmMkg> toFail;

    static List CoreSwitchList = new ArrayList();
    static List AggSwitchList = new ArrayList();
    static List EdgeSwitchList = new ArrayList();
    static List HostListids = new ArrayList();
    static List pod1 = new ArrayList();
    static List pod2 = new ArrayList();
    static List pod3 = new ArrayList();
    static List pod4 = new ArrayList();

    /**
     * The vmlist.
     */
    private static List<VmMkg> vmlist;

    private static List<VmMkg> createVM(int userId, int vms, int idShift) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<VmMkg> list = new LinkedList<VmMkg>();
        Random random = new Random();

        //VM Parameters
        long size = 1000; //image size (MB)
        int ram = 1000; //vm memory (MB)
        int mips = 360;
        long bw = random.nextInt(400);
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        VmMkg[] vm = new VmMkg[vms];

        int Randomdivider1 = random.nextInt(vms / 2);

        //int Randomdivider2=random.nextInt(vms);
        for (int i = 0; i < vms; i++) {


            if (i == 9) {
                mips = ThreadLocalRandom.current().nextInt(540, 620 + 1);
                bw = ThreadLocalRandom.current().nextInt(400, 940 + 1);
                System.out.println("\n\n\n ......mips value.........." + mips);
            } else if (i == 19) {
                mips = ThreadLocalRandom.current().nextInt(720, 1000 + 1);
                bw = ThreadLocalRandom.current().nextInt(0, 340 + 1);


                System.out.println("\n\n\n ......mips value.........." + mips);
            }

            vm[i] = new VmMkg(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeSharedMkg());
            list.add(vm[i]);
        }

        return list;
    }


    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 1000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        int lengthIncr = 300;
        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        int Randomdivider1 = ThreadLocalRandom.current().nextInt(0, cloudlets + 1);
        int Randomdivider2 = ThreadLocalRandom.current().nextInt(0, cloudlets + 1);

        for (int i = 0; i < cloudlets; i++) {


            if (i == Randomdivider1 || i == Randomdivider2) {
                length = ThreadLocalRandom.current().nextInt(500, 3000 + 1);
            }

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
        Log.printLine("Starting CloudSimExamplemkg1...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation


            final DatacenterMkg datacenter0 = createDatacenter("Datacenter_0");


            //Third step: Create Broker
            final DatacenterBrokerMkg broker = createBroker("Broker_0");
            int brokerId = broker.getId();


            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmlist = createVM(brokerId, 30, 0); //creating 5 vms
            cloudletList = createCloudlet(brokerId, 300, 0); // creating 10 cloudlets
            initCSVFiles();
            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);


            /** EDIT THIS to Fail a VM before simulation starts */
            //failVMsBeforeSimulationStarts();


            datacenter0.setFailureParameters(vmlist, FailureParameters.FAIL_SINGLE_VM, FailureParameters.STATIC_DELAY, 2.0, null);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();


            // Final step: Print results when simulation is over
            //List<Cloudlet> newList = broker.getCloudletReceivedList();
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
            //Log.printLine("\n\n\n vm fail list........ = " + toFail.get(0).getId());
            Log.printLine("\n\n\n Extra VM transmission overhead time in RFF_result....... = " + DatacenterMkg.RFF_result);
            //		Log.printLine("\n\n\n Extra  transmission of   packet_counter..... = "+ SimEntity.paket_counter );
            Log.printLine("\n\n\n  total packet size which were moved from one vm to another due to failure.... = " + DatacenterMkg.pkt_size);

            Log.printLine("\n\n\n  total number of times CORE SWITCH used....... = " + DatacenterMkg.core_sw_frc);
            Log.printLine("\n\n\n total number of times AGGR SWITCH used...... = " + DatacenterMkg.agg_sw_frc);
            Log.printLine("\n\n\n  total number of times EDGE SWITCH used...... = " + DatacenterMkg.edg_sw_frc);

            Log.printLine("\n\n\n  total network used by CORE SWITCH used....... = " + DatacenterMkg.core_sw_frc * 52 * 10.0);
            int e_core = (int) (DatacenterMkg.core_sw_frc * 52 * 10.0);

            Log.printLine("\n\n\n  total network used by  AGGR SWITCH used....... = " + DatacenterMkg.agg_sw_frc * 52 * 10.0);
            int e_aggr = (int) (DatacenterMkg.agg_sw_frc * 52 * 10.0);


            Log.printLine("\n\n\n  total network used by EDGE SWITCH used....... = " + DatacenterMkg.edg_sw_frc * 52 * 10.0);
            int e_edg = (int) (DatacenterMkg.edg_sw_frc * 52 * 10.0);

            int total_network_used = e_core + e_aggr + e_edg;
            Log.printLine("\n\n\n  total network used by all switches....... = " + total_network_used);
            Log.printLine("\n\n\n ....................................................... = ");

            Log.printLine(" Extra VM transmission overhead time in RFF_result....... = " + DatacenterMkg.RFF_result);
            Log.printLine(" vm transmission BF_result........ = " + DatacenterMkg.BF_result);
            Log.printLine(" vm transmission FF_result........ = " + DatacenterMkg.FF_result);


            Log.printLine("Fault Injection Time = " + FailureParameters.FALT_INJECTION_TIME);
            if (ConstsMkg.DETECTION_ALGORITHM == 1) {
                Log.printLine("Fault Detection Time(Algorithm 1) = " + FailureParameters.FALT_DETECTION_TIME);
            } else if (ConstsMkg.DETECTION_ALGORITHM == 2) {
                Log.printLine("Fault Detection Time(Algorithm 2) = " + FailureParameters.FALT_DETECTION_TIME);
            }
            Log.printLine("Number of Times Monitor was called = " + FailureParameters.NO_OF_MONITOR_CALLS);
            Log.printLine("CloudSimExamplemkg4 finished!");

            //mkg code

            {
                FileWriter f = new FileWriter("mkg.csv", true);
                //	FileWriter f = new FileWriter("mkg.csv",true);
                f.write("");
                //	f.append("DatacenterMkg.RFF_result,DatacenterMkg.BF_result,DatacenterMkg.FF_result,FailureParameters.NO_OF_MONITOR_CALLS ,e_edg,e_aggr,e_core\n");
                //	f.append(DatacenterMkg.RFF_result + ","+DatacenterMkg.BF_result +","+DatacenterMkg.FF_result+ ","+ FailureParameters.NO_OF_MONITOR_CALLS+"," + e_edg+","+e_aggr+","+e_core +"\n");
                //	f.append(DatacenterMkg.RFF_result + ","+DatacenterMkg.BF_result +","+DatacenterMkg.FF_result+ ","+ FailureParameters.NO_OF_MONITOR_CALLS+"," + e_edg+","+e_aggr+","+e_core +","+"CHECK"+"\n");
                f.append(DatacenterMkg.RFF_result + "," + DatacenterMkg.BF_result + "," + DatacenterMkg.FF_result + "," + FailureParameters.NO_OF_MONITOR_CALLS + "," + e_edg + "," + e_aggr + "," + e_core
                        + "," + "CHECK" + FailureParameters.FALT_INJECTION_TIME + "," + FailureParameters.FALT_DETECTION_TIME + "," + "\n");
                f.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    /**
     * Fail a VM before the start of simulation.
     * private static void failVMsBeforeSimulationStarts() {
     * int numberOfVmsToFail = 1;
     * if(numberOfVmsToFail>vmlist.size()) {
     * Log.printLine(CloudSim.clock()+":[ERROR]: Number of Vms to Fails is greater than number of Vms Created!");
     * return;
     * }
     * toFail = FailureParameters.getRandomVMsToFail(vmlist,numberOfVmsToFail);
     * System.out.println("\n\n\n\n..........toFail.....\n\n\n"+toFail);
     * for (int i = 0; i < toFail.size(); i++) {
     * VmMkg vm2 = toFail.get(i);
     * System.out.println("\n\n\n\n..........vm2.....\n\n\n"+vm2);
     * vm2.setVmStatus(false);
     * System.out.println("\n\n\n\n..........vm2 isActive.....\n\n\n"+vm2.isActive());
     * }
     * }
     * <p>
     * /**
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


    private static void createCoreLayerSwitch(int NUMBER) {
        System.out.print("Create Agg Layer..........");
        for (int x = 1; x <= NUMBER; x++) {
            String PREFIX = "100";
            if (x >= 10) {
                PREFIX = "10";

            }
            CoreSwitchList.add(PREFIX + x);
        }

        System.out.print("\n\n\n IDS of nodes in Core Layer.........." + CoreSwitchList);

    }

    private static void createAggLayerSwitch(int NUMBER) {
        System.out.print("Create Core Layer..........");
        for (int x = 1; x <= NUMBER; x++) {
            String PREFIX = "200";
            if (x >= 10) {
                PREFIX = "20";

            }
            AggSwitchList.add(PREFIX + x);
        }

        System.out.print("\n\n\n IDS of nodes in  Agg Layer .........." + AggSwitchList);

    }

    private static void createEdgeLayerSwitch(int NUMBER) {
        System.out.print("Create Edge Layer..........");
        for (int x = 1; x <= NUMBER; x++) {
            String PREFIX = "300";
            if (x >= 10) {
                PREFIX = "30";

            }
            EdgeSwitchList.add(PREFIX + x);
        }

        System.out.print("\n\n\n IDS of nodes in  Edge Layer .........." + EdgeSwitchList);

    }

    private static List<Host> createHost(List<Host> hostList, int NUMBER) {
        // 2. A Machine contains one or more PEs or CPUs/Cores.
        // In this example, it will have only one core.
        List<Pe> peList = new ArrayList<Pe>();

        int mips = 3720;

        // 3. Create PEs and add these into a list.
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        int hostId;
        // 4. Create Host with its id and list of PEs and add them to the list of
        // machines
        int ram = 2048; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;

        System.out.print("Create Host Layer..........");
        for (int x = 1; x <= NUMBER; x++) {
            String PREFIX = "400";
            if (x >= 10 && x < 100) {
                PREFIX = "40";

            }
            if (x >= 100) {
                PREFIX = "4";

            }
            HostListids.add(PREFIX + x);

        }


        System.out.print("\n\n\n IDS of Host layer.........." + HostListids);


        int Randomdivider1 = ThreadLocalRandom.current().nextInt(0, NUMBER + 1);
        int Randomdivider2 = ThreadLocalRandom.current().nextInt(0, NUMBER + 1);

        for (int x = 0; x < HostListids.size(); x++) {


            if (x == Randomdivider1 || x == Randomdivider2) {
                mips = ThreadLocalRandom.current().nextInt(3725, 5320 + 1);
                System.out.println("\n\n\n ......mips value..host........" + mips);
            }


            hostList.add(
                    new Host(hostId = Integer.parseInt(HostListids.get(x).toString()), new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw), storage, peList, new VmSchedulerTimeShared(peList))); // This
            // is
            // our
            // machine
        }

        return hostList;
    }


    private static DatacenterMkg createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store
        // our machine
        List<Host> hostList = new ArrayList<Host>();

        int iNUMBER = 4;
        int iCoreLayerSwitch = iNUMBER;
        int iAggLayerSwitch = iNUMBER * 2;
        int iEdgeLayerSwitch = iNUMBER * 2;
        int iHost = iEdgeLayerSwitch * 2;

        System.out.println("\n\n iHost.........." + iHost);

        createCoreLayerSwitch(iCoreLayerSwitch);
        createAggLayerSwitch(iAggLayerSwitch);
        createEdgeLayerSwitch(iEdgeLayerSwitch);
        hostList = createHost(hostList, iHost);

        System.out.println("\n\n Got Host List.........." + hostList);

        System.out.println("\n\nStart Create Core to Agg link..........");

        for (int x = 0; x < iAggLayerSwitch; x = x + 2) {
            NetworkTopology.addLink(Integer.parseInt(CoreSwitchList.get(0).toString()), Integer.parseInt(AggSwitchList.get(x).toString()), 10.00, 1);
            NetworkTopology.addLink(Integer.parseInt(CoreSwitchList.get(1).toString()), Integer.parseInt(AggSwitchList.get(x).toString()), 10.00, 1);

            // System.out.println("\n\n .core
            // 0..."+Integer.parseInt(CoreSwitchList.get(0).toString()));
            // System.out.println("\n\n .core
            // 1...."+Integer.parseInt(CoreSwitchList.get(1).toString()));
            // System.out.println("\n\n
            // .value...."+Integer.parseInt(AggSwitchList.get(x).toString()));
        }

        for (int x = 1; x < iAggLayerSwitch; x = x + 2) {

            NetworkTopology.addLink(Integer.parseInt(CoreSwitchList.get(2).toString()), Integer.parseInt(AggSwitchList.get(x).toString()), 10.00, 1);
            NetworkTopology.addLink(Integer.parseInt(CoreSwitchList.get(3).toString()),
                    Integer.parseInt(AggSwitchList.get(x).toString()), 10.00, 1);
            /*
             * System.out.println("\n\n Part 2m core 0..."+Integer.parseInt(CoreSwitchList.
             * get(2).toString()));
             * System.out.println("\n\n Part 2m core 0..."+Integer.parseInt(CoreSwitchList.
             * get(3).toString()));
             * System.out.println("\n\n Part 2m core 0..."+Integer.parseInt(AggSwitchList.
             * get(x).toString()));
             */
        }
        System.out.println("\n\n Done Create Core to Agg link..........");

        System.out.println("\n\n Start Create Agg to Edge..........");

        for (int x = 0; x < iAggLayerSwitch; x = x + 2) {

            NetworkTopology.addLink(Integer.parseInt(AggSwitchList.get(x).toString()),
                    Integer.parseInt(EdgeSwitchList.get(x).toString()), 1.00, 2.00);
            NetworkTopology.addLink(Integer.parseInt(AggSwitchList.get(x).toString()),
                    Integer.parseInt(EdgeSwitchList.get(x + 1).toString()), 1.00, 2.00);
            NetworkTopology.addLink(Integer.parseInt(AggSwitchList.get(x + 1).toString()),
                    Integer.parseInt(EdgeSwitchList.get(x).toString()), 1.00, 2.00);
            NetworkTopology.addLink(Integer.parseInt(AggSwitchList.get(x + 1).toString()),
                    Integer.parseInt(EdgeSwitchList.get(x + 1).toString()), 1.00, 2.00);

        }

        System.out.println("\n\n Done Create Agg to Edge..........");

        System.out.println("\n\n Start Create Edge to Host........");

        for (int x = 0; x < iEdgeLayerSwitch; x++) {

            NetworkTopology.addLink(Integer.parseInt(EdgeSwitchList.get(x).toString()), hostList.get(2 * x).getId(),
                    20.0, 10);
            NetworkTopology.addLink(Integer.parseInt(EdgeSwitchList.get(x).toString()), hostList.get(2 * x + 1).getId(),
                    20.0, 10);

        }

        System.out.println("\n\n Done Create  Edge to Host..........");

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
        } catch
        (Exception e) {
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
