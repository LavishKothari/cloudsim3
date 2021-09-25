package org.cloudbus.cloudsim.mkg;

public class ConstsMkg {

private static final boolean False = false;

	/*
	*//** Parameter to decide for creating the datacenter characteristics  *//*
	public static final int datacentre  = ;
	*/
	/** Parameter to decide whether Migration Module works or not */
	public static final boolean CL_MIGRATION_ON = true;
	
	/** Parameter to decide whether Monitoring(Detecting) Module works or not */
	public static final boolean CMM_ON = true;
	
	/** The scheduing interval. Specify an interval at which you want logs for all cloudlets */
	public static final double SCHEDULING_INTERVAL = 1;
	
	/** The time interval at which the monitor of first detection algorithm is called */
	public final static int MONITORING_INTERVAL = 1;
	
	/** Parameter to decide which detection algorithm works 
	 * =1 for detection Algo 1
	 * =2 for detection Algo 2
	 * 
	 */
	public final static int DETECTION_ALGORITHM = 1;

	public final static int number_of_host_in_one_pod= 4;
	
	public final static int number_of_host_in_one_subpod= 2;

}
