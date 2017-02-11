package princeton;

import org.apache.commons.math3.ml.clustering.DoublePoint;

public class Kmeanpoint extends DoublePoint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double w ;
	public Kmeanpoint(double[]location,double weight){
			super(location);
			w = weight;
		}
	public double getWeight() {
		return w;
	}
}
