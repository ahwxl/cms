package com.bplow.look.bass.multiThread;

public class MyThreadTask implements Runnable{
	
	
	public void run() {
		Long threadId = Thread.currentThread().getId();
		
		System.out.println("当前线程id---->"+threadId);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Long threadId = Thread.currentThread().getId();
		System.out.println("main 当前线程id---->"+threadId);
		

		Thread td = new Thread(new MyThreadTask());
		td.run();
		
		Thread t2d = new Thread(new MyThreadTask());
		t2d.run();
	}

	

}
