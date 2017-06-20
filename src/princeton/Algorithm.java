package princeton;

import java.util.List;

public abstract class Algorithm{
	
	final int[] pCpu= {15,30,60,120,120};//core
	final int [] pMem = {80,160,320,640,1280};//GB
	final int[] pStorage = {7000,15000,15000,30000,30000};//GB
	final int[] pCost = {11200,22500,44900,64900,74900};//USD
	final int[] pNic = {360,1024,1024,10240,10240};//Mbps
	Instance instance;
	double cost;
	double networkUsage;
	 double lastNetworkUsage=Double.MAX_VALUE;
	double totalDelay;
	double bandwidthToCloud;
	double innerDelay;
	boolean exit = false;
	int []wifiType;
	/*Algorithm(Instance i){
		wifiType = i.wifiType;
	}
	*/
	void Update() {
		totalDelay = 0;
		networkUsage= 0;
		bandwidthToCloud = 0;
	};
	public double getInnerDelay() {
		return innerDelay;
	}
	protected double getUserToCloudDelay(int idx) {
		return 12000/(wifiType[idx]*1000000) + 0.0056+ 0.000075;
	}
	protected double getUserToFogDelay(int idx, double dist){
		return 12000/(wifiType[idx]*1000000) + dist/177000 +0.0000125;
	}
	protected void doneCheck() {
		if(Math.abs(lastNetworkUsage - networkUsage) < 0.1) {
			exit = true;
			//System.out.println(Arrays.toString(glocon[0]));
		}
			lastNetworkUsage = networkUsage ;
		
	}
	/**
	 * @return the bandwidthToCloud
	 */
	public double calCost(List<FogEntity> foglist) {
		for(FogEntity f : foglist) {
			cost+=calCost(p);
		}
	}
	private double calCost(FogEntity f) {
		int tcpu;
		int tmem;
		int tpkts
		for(int i :f.clientIdxList) {
			tcpu += instance.cpu[i];
			tmem+=instance.cpu[i];;
			tpkts+=instance.cpu[i];
		}
		
		
	}
	public double[] closestProjector(double[] clo) {
		double dist = Double.MAX_VALUE;
		double[]candidate=new double[2];
		for(double[] p: instance.Nodes) {
			double tempdx = p[0]-clo[0];
			double tempdy = p[1]-clo[1];
			if(dist>Math.sqrt(tempdx*tempdx+tempdy*tempdy)) {
				dist=Math.sqrt(tempdx*tempdx+tempdy*tempdy);
				candidate=p;
			}
		}
		return candidate;
	}
	public double getBandwidthToCloud() {
		return bandwidthToCloud;
	}
	/**
	 * @param bandwidthToCloud the bandwidthToCloud to set
	 */
	public void setBandwidthToCloud(double bandwidthToCloud) {
		this.bandwidthToCloud = bandwidthToCloud;
	}
	abstract void iterate();
	abstract void output() ;
	/**
	 * @return the cost
	 */
	public boolean done() {
		return exit;
	}
	public double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	/**
	 * @return the networkUsage
	 */
	public double getNetworkUsage() {
		return networkUsage;
	}
	/**
	 * @param networkUsage the networkUsage to set
	 */
	public void setNetworkUsage(double networkUsage) {
		this.networkUsage = networkUsage;
	}
	/**
	 * @return the totalDelay
	 */
	public double getTotalDelay() {
		return totalDelay;
	}
	/**
	 * @param totalDelay the totalDelay to set
	 */
	public void setTotalDelay(double totalDelay) {
		this.totalDelay = totalDelay;
	}
	
}	
