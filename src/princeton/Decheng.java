package princeton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.math3.util.OpenIntToDoubleHashMap.Iterator;

public class Decheng {
	static final int N = 1;
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch doneSignal = new CountDownLatch(N);// TODO Auto-generated method stub
		
		Instance t = new Instance(50,10,Helper.outputFileName);
		for(int i =0;i<N;i++) {
			
			//(new Thread(new Exp(t,("spring-"+((i)+1)),true,doneSignal))).start();
			//(new Thread(new Exp(t,("vicinity-"+((i)+1)),true,doneSignal))).start();
			(new Thread(new Exp(t,("kmean-"+((3)+1)),true,doneSignal))).start();
			
			}
			
		doneSignal.await();
		String indent = "   ";
		System.out.println("exp-name"+indent+"Delay(ms)"+indent+"bw2cloud(MB)"+indent +"cost"+indent+"netusage"+indent+"innerDelay");
		java.util.Iterator<Object[]> iter = t.results.iterator();
		while(iter.hasNext()) {
			//System.out.println(t.results.poll());
			System.out.format("%-10s %-10.3f %-10.5f %-10.1f %10.1f %5.3f %n", iter.next());
		}
		try {
		(new plotTool(t.results)).writeData();}
		catch(IOException e) {}
	}

}
