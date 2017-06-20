package princeton;

import java.util.ArrayList;
import java.util.List;

public class FogEntity {
	List<Integer> clientIdxList;
	double[] location =new double [2];
	FogEntity(double[] lo){
		clientIdxList= new ArrayList<Integer>();
		location = lo;
	}
	public void addClient(int idx){
		clientIdxList.add(idx);
	}
	public List<Integer> getClients() {
		return clientIdxList;
	}
	public double[]	getLocation() {
		return location;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
