package princeton;

import edu.princeton.cs.introcs.Draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vicinity {
	double []rx  =null;
	double []ry = null;
	double []w = null;
	static final double vic  = 10;
	Draw drawer=null;
	double [][]sites=null ;
	iterHashMap<Integer, List<Integer>,Integer> mapping=null;
	{
		sites= new double[(Springs.Nodes).length][2];
		
	}
	public Vicinity( Draw d) {
		drawer = d;
		for ( int si=0; si<Springs.Nodes.length;si++) {
			sites[si][0] = Springs.Nodes[si][0]+50;
			sites[si][1] =Springs.Nodes[si][1]+50;
        }
		mapping = new iterHashMap<Integer, List<Integer>,Integer>();
		rx  = Springs.rx;
		ry = Springs.ry;
		w = Springs.w;
	}	
	

	public void Update() {
		rx  = Springs.rx;
		ry = Springs.ry;
		
		mapping = new iterHashMap<Integer, List<Integer>,Integer>();
		updateCluster(Springs.INITIAL_CLUSTER);
	}
	static double distance(double ix,double iy,double jx,double jy) {
		double rx = ix-jx;
		double ry = jx-jy;
		return Math.sqrt(rx*rx+ry*ry);
		
	}
	public void start() {
		Update();
		show();
	}
	private void updateCluster(int centroidnum){
		
		if (centroidnum<1){
			return;
		}
			else {
				updateCluster(centroidnum-1);
				System.out.println(centroidnum);
				double lastrate= 0;
				int index = -1;
				List<Integer> tempclis =null;
				for(int i =0;i<sites.length;i++) {
					if (mapping.containsKey(i)) continue;
					
					List<Integer> temlist= new ArrayList<Integer>();
					double hotrate = 0;
					for(int j= 0;j<rx.length;j++) {
						//System.out.println(mapping.iterContainsValue(j));
						if(mapping.iterContainsValue(j)||distance(sites[i][0],sites[i][1],rx[j],ry[j])>vic) continue;
						temlist.add(j);
						hotrate+=w[j];
					}
					
					if(hotrate>lastrate) {
						lastrate = hotrate;
						index = i;
						tempclis = temlist;
					}
					System.out.println(String.valueOf(hotrate)+ "[["+ String.valueOf(lastrate));
				}
				if(index ==-1) {
					//System.out.println(centroidnum);
					System.out.println("no sites");
					//System.exit(1);	
				}
				else {
					mapping.put(index, tempclis);
				}
				
			}
	}
	void show() {
		for(Integer k: mapping.keySet()) {
			drawer.filledCircle(sites[k][0],sites[k][1], 1);
			drawer.textLeft(sites[k][0]+5, sites[k][1], String.valueOf(k));
			for(Integer v: mapping.get(k)) {
				drawer.line(sites[k][0], sites[k][1], rx[v]	, ry[v]);
				
			}
			
			
		}
		
		for ( int si=0; si<sites.length;si++) {
        	drawer.filledSquare(sites[si][0],sites[si][1], 1.0);
        	
        }
	}
}
