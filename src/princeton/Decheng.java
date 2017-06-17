package princeton;

import java.util.concurrent.CountDownLatch;

public class Decheng {
	static final int N = 6;
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch doneSignal = new CountDownLatch(N);// TODO Auto-generated method stub
		
		Instance t = new Instance(50,6);
		for(int i =0;i<N;++i) {
			(new Thread(new Exp(t,("spring-"+3),true,doneSignal))).start();
		}
		doneSignal.await();
		System.out.println("lalalla-------------------");
		while(!t.results.isEmpty()) {
			System.out.println(t.results.poll());
		}
	}

}
