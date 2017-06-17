package princeton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.*;

import edu.princeton.cs.introcs.Draw;

public class Kmeans extends Algorithm{
	double []rx  =null;
	double []ry = null;
	double []w  = null;
	int k_count;
	double [][] sites = null;
	List<Kmeanpoint> clientsloca=null;
	Draw d = null;
	

	//double networkUsage;
	KMeansPlusPlusClusterer<Kmeanpoint> clusterer;
	List<CentroidCluster<Kmeanpoint>> clusterResults=null;
	//List<CentroidCluster<Kmeanpoint>> lastClusterResults = null;
	{
		rx  = Springs.rx;
		ry = Springs.ry;
		w= Springs.w;
	};
	
	//TODO deal with correct parameter
	public Kmeans(Instance ins) {
		this(ins,4);
	}
	public Kmeans(Instance ins,int k_cnt) {
		networkUsage=Double.MAX_VALUE;
		k_count = k_cnt;
		d = new Draw("Kmean Algorithm");
		 d.setXscale(0, 100);
	     d.setYscale(0, 100);
	     d.setPenColor(Draw.BLUE);
	     d.setPenRadius(0.0025);
		rx = ins.rx;
		ry = ins.ry;
		w  = ins.w;
		sites = ins.Nodes;
		clientsloca = new ArrayList<Kmeanpoint>(rx.length);
		clusterer = new KMeansPlusPlusClusterer<Kmeanpoint>(k_cnt, 10000);
		
		for ( int si=0; si<Springs.Nodes.length;si++) {
			sites[si][0] = Springs.Nodes[si][0]+50;
			sites[si][1] =Springs.Nodes[si][1]+50;
        }
	}
	@Deprecated
	public Kmeans(Draw draw2) {
		//updateClients();
		d = draw2;
		clientsloca = new ArrayList<Kmeanpoint>(rx.length);
		clusterer = new KMeansPlusPlusClusterer<Kmeanpoint>(Springs.INITIAL_CLUSTER, 10000);
		sites=new double[Springs.Nodes.length][2];
		for ( int si=0; si<Springs.Nodes.length;si++) {
			sites[si][0] = Springs.Nodes[si][0]+50;
			sites[si][1] =Springs.Nodes[si][1]+50;
        }
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
	void iterate () {
		d.clear();
		Update();
		draw();
		d.show(10);
	}
	void Update() {
		clientsloca = updateClients();
		if (networkUsage > caluNetworkUsage()) {
		System.out.println("Kmean update occurs");
		}
		
	}
	private double caluNetworkUsage() {
		List<CentroidCluster<Kmeanpoint>> tempclusterResults=null;
		double nu =0;
		clientsloca = updateClients();
		tempclusterResults=clusterer.cluster(clientsloca);
		for(int i =0;i<k_count;i++) {
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
		if(networkUsage> nu) {
			networkUsage=nu;
			clusterResults = tempclusterResults;
			}
		return nu;
	}
	void draw() {
		
		for(int i =0;i<k_count;i++) {
			double []po = clusterResults.get(i).getCenter().getPoint();
			d.filledCircle(po[0], po[1], 1);
			d.textLeft(po[0]+5, po[1]+5, String.valueOf(i));
			for(Kmeanpoint point:clusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				
				d.line(clpo[0], clpo[1], po[0]	, po[1]);
			
		
			}	
		}
		for ( int si=0; si<sites.length;si++) {
        	d.filledSquare(sites[si][0],sites[si][1], 1.0);
        	d.setFont(new Font("", Font.BOLD, 10));
        	d.textLeft(sites[si][0], sites[si][1], String.valueOf(si));
		
		d.setFont(new Font("", Font.BOLD, 20));
		d.text(35.0, 5.0, String.valueOf(getLastNetworkUsage()));
	}

}
}