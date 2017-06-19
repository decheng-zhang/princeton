package princeton;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.File;
import java.io.IOException;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
/**
 * @author dechengzhang-admin
 *
 */
public class Instance {
	final int [][] labelPosition = {{0,0},{0,1},{0,2},{0,4},{0,5},{0,6},{0,9},{0,10},{0,11},{0,12},{0,13}};
 	final String[] labelName = {"Users", "Possiblei","distanceU","Possiblei","Possiblej","distanceT","Users","Cpu","Memory","Packets","BandwidthU"};
 	final double [] areaScale= Helper.areaScale;
	
	double[] rx = null;
    double[] ry = null;
	double[][] Nodes=null;
	double [] w = null;
	int [] cpu;
	int[] mem;
	int[] packets;
	int[] bandwidth;
	int[] wifiType;
	int clents;
	int fogs;
	public ConcurrentLinkedQueue<Object[]> results;
	/**
	 * @param clentsnum
	 * @param k_cnt
	 */
	public Instance(int clentsnum, int k_cnt) {
		clents = clentsnum;
		fogs = k_cnt;
		results  = new ConcurrentLinkedQueue<Object[]>();
		rx = new double[clentsnum];
		ry = new double[clentsnum];
		w = new double[clentsnum];
		cpu = new int[clentsnum];
		mem= new int[clentsnum];
		packets = new int[clentsnum];
		bandwidth = new int[clentsnum];
		wifiType = new int[clentsnum];
		//Nodes = nodeAdder(10.0,12,3);
		Nodes = nodeRandomizer(areaScale, k_cnt);
        //Nodes = nodeCutter((areaScale),(int)Math.sqrt(k_cnt));
		for (int i = 0; i < clentsnum; i++) {
            rx[i] = areaScale[0] * Math.random();
            ry[i] = areaScale[1] * Math.random();
            Random r = new Random();
            w[i] = r.nextGaussian()*10+50;
            cpu[i] = r.nextInt(72)+1;
            mem[i]= r.nextInt(720)+1;
            packets[i]= r.nextInt(1000)+1;
            bandwidth[i] = packets[i] *1500;
            wifiType[i]=(r.nextInt(6)+2)*10;
        }
	}
	public void serializer() {
		try {
		WritableWorkbook workbook = Workbook.createWorkbook(new File("output.xls"));
		WritableSheet sheet = workbook.createSheet("Sheet1",0);
		//Writing Label
		for(int i=0;i<labelPosition.length;i++) {
		sheet.addCell(new Label(labelPosition[i][1],labelPosition[i][0],labelName[i]));
		}
		//Writing user-fog distance 
		int p=1;
		for(int i= 0;i<clents;i++) {
			for(int j = 0;j<fogs;j++) {
				sheet.addCell(new Number(0,p,i+1));
				sheet.addCell(new Number(1,p,j+1));
				double dis = Math.sqrt(Math.pow((rx[i]-Nodes[j][0]),2)+Math.pow((ry[i]-Nodes[j][1]),2));
				sheet.addCell(new Number(2,p,dis));
				p++;
			}
		}
		//Writing fog-fog distance
		p=1;
		for(int i=0;i<fogs;i++) {
		for (int j=0;j<fogs;j++)
		{
			sheet.addCell(new Number(4,p,j+1));
			sheet.addCell(new Number(5,p,j+1));
			double dis = Math.sqrt(Math.pow((Nodes[i][0]-Nodes[j][0]),2)+Math.pow((Nodes[i][0]-Nodes[j][1]),2));
			sheet.addCell(new Number(6,p,dis));
			p++;
			
		}
		}
		//Writing user-request table
		p=1;
		for(int i=0;i<clents;i++) {
			sheet.addCell(new Number(9,p,i+1));
			sheet.addCell(new Number(10,p,cpu[i]));
			
			sheet.addCell(new Number(11,p,mem[i]));
			sheet.addCell(new Number(12,p,packets[i]));
			sheet.addCell(new Number(13,p,bandwidth[i]));
			p++;
		}
			
		workbook.write();
		workbook.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @param radius 
	 * @param degreecut ;total number is degreecut*level+1
	 * @param level
	 * @return
	 */
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
	/**
	 * @param areaScale {100,100}
	 * @param cut ;total number is cut*cut
	 * @return
	 */
	public double [][] nodeCutter(double[] areaScale,int cut) {
		double [][] Nodes =  new double [cut*cut][2];
		
		int t =0;
		for (int i=0;i< cut;i++){
			for(int j=0;j<cut;j++) {
				
				Nodes[t][0]=areaScale[0] *((double)i+1)/((double)cut+1);
				Nodes[t][1]=areaScale[1]*((double)j+1)/((double)cut+1);
				t++;
			}
			} 
		return Nodes;
	}
	/**
	 * @param areaScale
	 * @param count number of fogs
	 * @return
	 */
	public double [][] nodeRandomizer(double[] areaScale,int count){
		double[][] nodes=new double[count][2];
		for(int i=0;i<count;i++) {
			nodes[i][0]=areaScale[0] * Math.random();
			nodes[i][1]=areaScale[1] * Math.random();
		}
		
		
		return nodes;
	}
	public static void main(String args[]) {
		 (new Instance(5,10)).serializer();
	 }
}
