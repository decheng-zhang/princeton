package princeton;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.math3.util.OpenIntToDoubleHashMap.Iterator;

public class Decheng {
	static final int N = 30;
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch doneSignal = new CountDownLatch(N);// TODO Auto-generated method stub
		
		Instance t = new Instance(50,6);
		for(int i =0;i<N;++i) {
			(new Thread(new Exp(t,("spring-"+((i%6)+1)),true,doneSignal))).start();
		}
		doneSignal.await();
		String indent = "   ";
		System.out.println("exp-name"+indent+"Delay(ms)"+indent+"bw2cloud(MB)"+indent +"cost"+indent+"netusage");
		java.util.Iterator<Object[]> iter = t.results.iterator();
		while(iter.hasNext()) {
			//System.out.println(t.results.poll());
			System.out.format("%-10s %15.3f   %-5.5f %10.1f %10.1f %n", iter.next());
		}
		try {
		(new plotTool(t.results)).writeData();}
		catch(IOException e) {}
	}

}
