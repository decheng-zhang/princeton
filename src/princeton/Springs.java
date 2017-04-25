package princeton;

import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdOut;
import princeton.Springs.Centroid;

/******************************************************************************
 *  Compilation:  javac Springs.java
 *  Execution:    java Springs N
 *  Dependencies: draw1.java
 *
 *  Simulates a system of springs.
 *
 *  % java Springs 15
 *
 *  Credits:  Jeff Traer-Bernstein
 *
 ******************************************************************************/


public class Springs {
    static double[] rx = null;
    static double[] ry = null;
	static double[][] Nodes=null;
	static double [] w = null;
    static final double particleMass = 1.0;
    static final double drag = 0.1;
    static final double springStrength = 0.1;
    static final double springLength = 30;
    static final double timeStep = 0.1;
    static final int INITIAL_CLUSTER =6;
    static final int MERGE_UPTATE_INTERVAL =20;
    static  int MERGE_DISTANCE=10;
    List<Centroid> cents=null;
    Random seed =null;
    static Stopwatch timer1 = null;
    static double mergelastime = 0;
    double networkUsage;
    
    /**
	 * @return the cents
	 */
	public List<Centroid>getCents() {
		return cents;
	}
	double[][]glocon = null;
	/**
	 * @return the nodes
	 */
	
	public Springs(int clentsnum) {
		Random r = new Random();
		cents = new LinkedList<Centroid>();
		glocon = new double[2][clentsnum];
		Arrays.fill(glocon[0], -1);
		System.out.println(Arrays.toString(glocon[0]));
		Arrays.fill(glocon[1], Double.MAX_VALUE );
		rx = new double[clentsnum];
		ry = new double[clentsnum];
		w = new double[clentsnum];
        seed = new Random();
        
         timer1=new Stopwatch();
        
        for (int i = 0; i < clentsnum; i++) {
            rx[i] = 100 * Math.random();
            ry[i] = 100 * Math.random();
            
            w[i] = r.nextGaussian()*10+50;
        }
      for(int j =0;j<INITIAL_CLUSTER;j++) {
        	cents.add( new Centroid(j,clentsnum,getThreshold(INITIAL_CLUSTER)));
        }
		
	}
	private static double elapsedTime() {
		return timer1.elapsedTime();
	}
	private double getThreshold(double clentsnum) {
		return 100/(Math.floor(Math.sqrt(clentsnum))+1);
	}
	private Centroid merge(Centroid back,Centroid front) {
		Centroid retune = null;
		front.rx+=back.rx;
		front.rx /=2;
		front.ry+=back.ry;
		front.ry /=2;
		front.threshold *=2;
		for(int i =0;i<front.totalNum;i++) {
			if(back.connection[i]==1) {
				glocon[0][i]=front.id;
				glocon[1][i]=Double.MAX_VALUE;
			}
			front.connection[i]+=back.connection[i];
			front.connection[i]=(front.connection[i]>=1)? 1:0;
		}
		retune = front;
		return retune;
	}
	public List<Centroid> mergeCheck(List<Centroid> ces,double tt) {
		if (ces.size()<=1) {
			return ces;
		}
		else {
			boolean offmission = false;
			LinkedList<Centroid> newcens = new LinkedList<Centroid>();
			//newcens.add(ces.get(0));
			boolean once = false;
			for(int i =0;i<ces.size();i++) {
				for(int j=0;j<i;j++) {
					
					double mergematic =0.6* Math.max(ces.get(i).getThreshold(),ces.get(j).getThreshold()) ;
					if(!once&&distance(ces.get(i),ces.get(j))<tt){
						System.out.println("merge occurs");
						
						newcens.set(j, merge(ces.get(i),ces.get(j)));
						offmission = true;
						once = true;
						break;
						
					}
					
					}
						if(offmission == false) {//System.out.println("First adding i" + i);
						newcens.add(ces.get(i));
						}else { 
							offmission = false;
						}
				}
					
						
					
					
				
			
			return newcens;
		}
			
		}
	
	private double distance(Centroid back,Centroid front) {
		double dx= back.rx-front.rx;
		double dy = back.ry-front.ry;
		return Math.sqrt(dx*dx+dy*dy);
	}
	public void start(Draw draw1) {
		networkUsage=0;
		if((elapsedTime()-mergelastime)> MERGE_UPTATE_INTERVAL) {
			System.out.println("The cents size before mergecheck func" + cents.size());
			System.out.println("MERGE_DISTANCE" + MERGE_DISTANCE);
			mergelastime = elapsedTime();
			cents = mergeCheck(cents,MERGE_DISTANCE);
			
			System.out.println("The cents size after mergecheck func" + cents.size());
		}
		for(int j = 0;j<cents.size();j++) {
			cents.get(j).Update();
			cents.get(j).draw(draw1);
			
		}
		
		//System.out.println(Arrays.deepToString(glocon));
		//for(int j = 0;j<cents.length;j++) {
		
		//}
	}
	public double getNetworkUsage() {
		return networkUsage;
	}
	public double[][] getNodes() {
		return Nodes;
	}
	public double [][] nodeAdder(double radius, int degreecut,int level) {
		if( Nodes == null){
		Nodes =  new double [degreecut*level+1][2];
		Nodes[Nodes.length-1][0]=0;
		Nodes[Nodes.length-1][1]=0;
		}
		for(int i = 0;i<degreecut;i++) {
			//System.out.println(Math.toRadians(360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][0]=radius*Math.cos(Math.toRadians(i*360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][1]=radius*Math.sin(Math.toRadians(i*360/(double)degreecut));
		}
		if(level!=1) {
			nodeAdder(radius*2,degreecut, level-1);	
		}

		return Nodes;
		
	}
	public class Centroid {
		
		public int id =-1;
		
		
		public int []connection= null;
		/**
		 * @param connection the connection to set
		 */
		public void setConnection(int[] connection) {
			this.connection = connection;
		}
		double fx = 0;
        double fy = 0;
        double vy = 0;
        double vx = 0;
        int totalNum = 0;
        double rx =0;
        double ry =0;
        double threshold=0;
		/**
		 * @return the threshold
		 */
		public double getThreshold() {
			return threshold;
		}
		public Centroid(int cenid, int clientNum,double thresholdinput){
		
			this.id = cenid;
			this.totalNum = clientNum;
			this.connection =new int[clientNum];
			this.threshold  = thresholdinput;
			this.rx = seed.nextDouble()*100;
			this.ry = seed.nextDouble()*100;
		}
		public void Update() {
			double fx = 0;
            double fy = 0;
            
            // spring forces act between every pairing of particles
            // spring force is proportional to the difference between the rest length of the spring
            // and the distance between the 2 particles it's acting on
       
            	
                for (int j = 0; j < totalNum; j++) {

       

                    
                    // calculate distance between particles i and j
                    double dx = Springs.rx[j] - rx;
                    double dy = Springs.ry[j] - ry;
                    double length = Math.sqrt(dx*dx + dy*dy);
                    
                    // figure out the force
                    
                    double ss =springStrength*(w[j]/10);
                    
                   double force = (length<threshold)? ss*length :0;
                   if(force ==0) {
                	   connection[j]=0;
                	   
                	   continue;
                   }else if(glocon[0][j]==id&&connection[j]==1){
                	   glocon[1][j]=length;
                   }else if(connection[j]==1) {
                	   connection[j]=0;
                	   continue;
                   }else if(connection[j]==0&&glocon[1][j]-5>length)
                   {
                	   connection[j]=1;
                	   glocon[0][j]=id;
                	   glocon[1][j]=length;
                   }else {
                	   continue;
                   }
 
                		 
       
                
                   
                    //double force =  ss * (length);
                	
                    double springForceX = force * dx / length;
                    double springForceY = force * dy / length;
                    
                    // update the force
                    fx += springForceX;
                    fy += springForceY;
                   
                }
                
            
            // add drag force
            // drag is proportional to velocity but in the opposite direction
            
                fx += -drag * vx;
                fy += -drag * vy;
            
            
                
            // update positions using approximation
        
                vx += fx *timeStep/particleMass;
                vy += fy *timeStep/particleMass;
                rx += vx *timeStep;
                ry += vy *timeStep;
                rx= (rx>100)? 100:rx;
                rx= (rx<0)? 0:rx;
                ry= (ry>100)? 100:ry;
                ry= (ry<0)? 0:ry;
		
               // System.out.println(Arrays.toString(connection));
                //System.out.println("\n");
		}
	
		public void draw(Draw draw1) {
			draw1.filledCircle(rx, ry, 1);
			draw1.textLeft(rx+5, ry+5, String.valueOf(id));
			for(int j =0;j<totalNum;j++) {
			if(connection[j]==1) {
                draw1.line(Springs.rx[j], Springs.ry[j], rx, ry);
                double tempdx =Springs.rx[j]-rx;
				double tempdy = Springs.ry[j]-ry;
				double tempdistance =Math.sqrt(tempdx*tempdx+tempdy*tempdy);
                networkUsage+= w[j]*tempdistance;
            	}
		}
		}
		
		
		/**
		 * @return the connection
		 */
		public int[] getConnection() {
			return connection;
		}
	};
    public static void main(String[] args) {
        // mess around with this, try 7, 8, 9, 10, 11, 12, 15
        // probably have to turn down the spring force to keep it stable after that...
        int n = Integer.parseInt(args[0]);

        Draw draw1= new Draw("Spring Algorithm");
        Draw draw2 = new Draw("Kmean Algorithm");
        Draw draw3 = new Draw("Vicinity Algorithm");
        
        
        
        
        
        

        
        
        // set up the drawing area
        draw1.setXscale(0, 100);
        draw1.setYscale(0, 100);
        draw1.setPenColor(Draw.BLUE);
        draw1.setPenRadius(0.0025);
        draw2.setXscale(0, 100);
        draw2.setYscale(0, 100);
        draw2.setPenColor(Draw.BLUE);
        draw2.setPenRadius(0.0025);
        draw3.setXscale(0, 100);
        draw3.setYscale(0, 100);
        draw3.setPenColor(Draw.BLUE);
        draw3.setPenRadius(0.0025);
        //draw1.enableDoubleBuffering();
        
        // initialize the particle positions randomly
  ;
  		
        Springs s = new Springs(n);
        s.nodeAdder(10.0,12,3);
        Kmeans k = new Kmeans(draw2);
        Vicinity v = new Vicinity(draw3);
        
   
      

        int tii = 1;
        double lltime =0;
        
        while (true) {
        	//CountDown; 
        	/*if(Springs.elapsedTime()-lltime >1)
               {
               	System.out.println(tii++);
               	lltime = Springs.elapsedTime();
               }
        	*/
            // clear all the forces
         
            
            /*if(draw1.mousePressed()) {
                rx[0] = draw1.mouseX();
                ry[0] = draw1.mouseY();
                vx[0] = 0.0;
                vy[0] = 0.0;
                fx[0] = 0.0;
                fy[0] = 0.0;
                }
*/
         for(int t=0;t<n;t++) {
        	  rx[t] +=(Math.random()-0.5)*10*timeStep;
        	  ry[t] += (Math.random()-0.5)*10*timeStep;
          
            rx[t]= (rx[t]>100)? 100:rx[t];
            rx[t]= (rx[t]<0)? 0:rx[t];
            ry[t]= (ry[t]>100)? 100:ry[t];
            ry[t]= (ry[t]<0)? 0:ry[t];
            
        }

            // clear
            draw1.clear();
            draw2.clear();
            draw3.clear();
            s.start(draw1);
            k.start();
            v.start();
            // draw Sites[]
            for ( int si=0; si<Nodes.length;si++) {
            	draw1.filledSquare(Nodes[si][0]+50,Nodes[si][1]+50, 1.0);
            	
            }
           
            for (int ic = 0; ic < n; ic++) {
                // draw a circle for each node
            	
                draw1.filledCircle(rx[ic], ry[ic], 0.4);
                draw1.setFont(new Font("", Font.BOLD, 20));
                draw1.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                draw2.filledCircle(rx[ic], ry[ic], 0.4);
                draw2.setFont(new Font("", Font.BOLD, 20));
                draw2.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                draw3.filledCircle(rx[ic], ry[ic], 0.4);
                draw3.setFont(new Font("", Font.BOLD, 20));
                draw3.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                // draw the connections between every 2 nodes
            }
            

            
            draw1.text(35.0, 5.0, String.valueOf(s.getNetworkUsage()));
            draw2.text(35.0, 5.0, String.valueOf(k.getLastNetworkUsage()));
            

            // show and wait
            draw1.show(10);
            draw2.show(10);
            draw3.show(10);
           // draw1.pause(10);
        }
    }
}