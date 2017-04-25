package princeton;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.*;

import edu.princeton.cs.introcs.Draw;

public class Kmeans {
	double []rx  =null;
	double []ry = null;
	double []w  = null;
	List<Kmeanpoint> clientsloca=null;
	Draw d = null;
	double lastNetworkUsage=Double.MAX_VALUE;
	/**
	 * @return the lastNetworkUsage
	 */
	public double getLastNetworkUsage() {
		return lastNetworkUsage;
	}
	double networkUsage;
	KMeansPlusPlusClusterer<Kmeanpoint> clusterer;
	List<CentroidCluster<Kmeanpoint>> clusterResults=null;
	//List<CentroidCluster<Kmeanpoint>> lastClusterResults = null;
	{
		rx  = Springs.rx;
		ry = Springs.ry;
		w= Springs.w;
	};
	public Kmeans(Draw draw2) {
		//updateClients();
		d = draw2;
		clientsloca = new ArrayList<Kmeanpoint>(rx.length);
		clusterer = new KMeansPlusPlusClusterer<Kmeanpoint>(Springs.INITIAL_CLUSTER, 10000);
			
	}
	
	private List<Kmeanpoint> updateClients(){
		List<Kmeanpoint> temploclist = new ArrayList<Kmeanpoint>(rx.length);
		rx  = Springs.rx;
		ry = Springs.ry;
		
		for(int i=0;i<rx.length;i++) {
			double []point = {rx[i],ry[i]};
			temploclist.add(new Kmeanpoint(point,w[i]));
		}
		return temploclist;
	}
	void start () {
		Update();
		draw();
		
	}
	void Update() {
		clientsloca = updateClients();
		if (lastNetworkUsage > caluNetworkUsage()) {
		System.out.println("Kmean update occurs");
		}
		
	}
	private double caluNetworkUsage() {
		List<CentroidCluster<Kmeanpoint>> tempclusterResults=null;
		double nu =0;
		clientsloca = updateClients();
		tempclusterResults=clusterer.cluster(clientsloca);
		for(int i =0;i<Springs.INITIAL_CLUSTER;i++) {
			double []po = tempclusterResults.get(i).getCenter().getPoint();
			
			for(Kmeanpoint point:tempclusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				double weight = point.getWeight();
				
				double dx =clpo[0]-po[0];
				double dy = clpo[1]-po[1];
				double distance =Math.sqrt(dx*dx+dy*dy);
				nu += distance*weight;
		
			}	
		}
		if(lastNetworkUsage> nu) {
			lastNetworkUsage=nu;
			clusterResults = tempclusterResults;
			}
		return lastNetworkUsage;
	}
	void draw() {
		
		for(int i =0;i<Springs.INITIAL_CLUSTER;i++) {
			double []po = clusterResults.get(i).getCenter().getPoint();
			d.filledCircle(po[0], po[1], 1);
			d.textLeft(po[0]+5, po[1]+5, String.valueOf(i));
			for(Kmeanpoint point:clusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				
				d.line(clpo[0], clpo[1], po[0]	, po[1]);
			
		
			}	
		}
	}

}
