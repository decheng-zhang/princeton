package princeton;

public abstract class Algorithm{
	
	final int[] pCpu= {15,30,60,120,120};//core
	final int [] pMem = {80,160,320,640,1280};//GB
	final int[] pStorage = {7000,15000,15000,30000,30000};//GB
	final int[] pCost = {11200,22500,44900,64900,74900};//USD
	final int[] pNic = {360,1024,1024,10240,10240};//Mbps
	double cost;
	double networkUsage;
	 double lastNetworkUsage=Double.MAX_VALUE;
	double totalDelay;
	double bandwidthToCloud;
	boolean exit = false;
	abstract void Update();
	
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
	abstract void output();
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
