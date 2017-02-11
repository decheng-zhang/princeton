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
	double networkUsage;
	KMeansPlusPlusClusterer<Kmeanpoint> clusterer;
	List<CentroidCluster<Kmeanpoint>> clusterResults=null;
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
	public double getNetworkUsage() {
		return networkUsage;
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
		clusterResults=clusterer.cluster(clientsloca);
	}
	void draw() {
		networkUsage = 0;
		for(int i =0;i<Springs.INITIAL_CLUSTER;i++) {
			double []po = clusterResults.get(i).getCenter().getPoint();
			d.filledCircle(po[0], po[1], 1);
			d.textLeft(po[0]+5, po[1]+5, String.valueOf(i));
			for(Kmeanpoint point:clusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				double weight = point.getWeight();
				d.line(clpo[0], clpo[1], po[0]	, po[1]);
				double dx =clpo[0]-po[0];
				double dy = clpo[1]-po[1];
				double distance =Math.sqrt(dx*dx+dy*dy);
				networkUsage += distance*weight;
		
		}	
	}
	}

}
