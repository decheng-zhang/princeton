package princeton;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.introcs.Draw;

public abstract class Algorithm{
	int count =0;
	final int[] pCpu= {90,180,360,720};//core
	final int [] pMem = {480,800,1600,3200};//MB
	final int[] pStorage = {7000,15000,15000,30000,30000};//GB
	final int[] pCost = {67200,135000,269400,389400};//USD
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
	boolean []nodeOpenness;
	List<Integer> secondDropList = new ArrayList<Integer>();
	Algorithm(Instance i){
		instance =i;
		wifiType = i.wifiType;
		nodeOpenness = new boolean[i.Nodes.length];
	}
	
	void Update() {
		totalDelay = 0;
		networkUsage= 0;
		bandwidthToCloud = 0;
	};
	public double getInnerDelay() {
		return innerDelay;
	}
	protected double getUserToCloudDelay(int idx) {
		return (double)instance.packets[idx]*12000/(double)(wifiType[idx]*1000000) + 0.0056+ 0.000075;
	}
	protected double getUserToFogDelay(int idx, double dist){
		return (double)instance.packets[idx]*12000/(double)(wifiType[idx]*1000000)+ dist/177000 +0.0000125;
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
		double tcost = 0;
		for(FogEntity f : foglist) {
			tcost+=(calCost(f)+Helper.costPerLocation);
		}
		return tcost;
	}
	private double calCost(FogEntity f) {
		int tcpu=0;
		int tmem=0;
		int tpkts;
		int tBW = 0;
		Map<Integer,Integer> idxToCPU = new HashMap<Integer,Integer>();
		Map<Integer,Integer> idxToBW = new HashMap<Integer,Integer>();
		for(int i :f.clientIdxList) {
			idxToCPU.put(i, instance.cpu[i]);
			idxToBW.put(i, instance.bandwidth[i]);
		}
		idxToBW = Helper.sortByValue(idxToBW);
		//idxToMEM = Helper.sortByValue(idxToMEM);
		int i=0;
		for(Map.Entry<Integer,Integer> entry:idxToBW.entrySet()) {
			int last = entry.getKey();
			
		tcpu +=instance.cpu[entry.getKey()];
		tmem +=instance.mem[entry.getKey()];
			while(i<pCpu.length && pCpu[i]<tcpu) {
				i++;
				}
			while(i<pCpu.length && pMem[i]<tmem) {
				i++;
			}
		if(i>=pCpu.length) {
			System.out.println("overflow err,fog is not cloud");
			secondDropList.add(last);
			i--;
		}
	}
		f.setFogtype(i);
		return pCost[i];
	}
	public double[] closestProjector(double[] clo) {
		double dist = Double.MAX_VALUE;
		double[]candidate=new double[2];
		int openidx=-1; 
		int i = 0;
		for(double[] p: instance.Nodes) {
			double tempdx = p[0]-clo[0];
			double tempdy = p[1]-clo[1];
			if(!nodeOpenness[i] &&dist>Math.sqrt(tempdx*tempdx+tempdy*tempdy)) {
				dist=Math.sqrt(tempdx*tempdx+tempdy*tempdy);
				candidate=p;
				openidx = i;
				
			}
			i++;
		}
		nodeOpenness[openidx]=true;
		return candidate;
	}
	/*@Override
	public boolean equals(Object object) {
		return this.equals()
	}*/
	public void finalShow(Draw d, List<FogEntity> flist){
		d.clear();
        for (int ic = 0; ic < instance.rx.length; ic++) {
            // draw a circle for each node
        	
            d.filledCircle(instance.rx[ic], instance.ry[ic], 0.4);
            d.setFont(new Font("", Font.BOLD, 10));
            d.textLeft(instance.rx[ic]+1, instance.ry[ic]+1,String.valueOf(ic+1));
        }
        
		for(FogEntity f:flist) {
			
			d.filledCircle(f.location[0], f.location[1], f.getFogtype());
			d.textLeft(f.location[0], f.location[1]+3, "fog-"+ (f.getFogtype()+1));
			for(Integer i:f.getClients()) {
				if(!secondDropList.contains(i)) {
				d.line(instance.rx[i], instance.ry[i],f.location[0],f.location[1]);
			
				}
				}	
		}
		double [][]sites = instance.Nodes;
		for ( int si=0; si<sites.length;si++) {
        	d.filledSquare(sites[si][0],sites[si][1], 1.0);
        	d.setFont(new Font("", Font.BOLD, 10));
        	d.textLeft(sites[si][0], sites[si][1]-3, String.valueOf(si+1));
		
		d.setFont(new Font("", Font.BOLD, 20));
		d.text(35.0, 5.0, String.valueOf(networkUsage));
	}
		d.show();
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
	abstract void output();
	public void outputend(Draw d, List<FogEntity> flist) {
			cost = calCost(flist);
			//calculate delay;
			for(FogEntity f:flist) {
				for(Integer i:f.clientIdxList) {
					double []ic = {instance.rx[i],instance.ry[i]};
				if(!secondDropList.contains(i)) {
					totalDelay +=getUserToFogDelay(i,f.getDist2Client(ic));
					bandwidthToCloud += instance.bandwidth[i]*Helper.percent2Cloud;
					System.out.println("");
				}else {
					totalDelay +=getUserToCloudDelay(i);
					bandwidthToCloud += instance.bandwidth[i];
				}
				count++;
			}
			}
				System.out.println("attentino"+(count));
			finalShow(d,flist);
			//calculate bandwidth to cloud;
		
	 };
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
