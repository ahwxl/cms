package com.bplow.look.bass.multiThread;

import java.util.Date;

/*@src  http://eric-619.iteye.com/blog/693681 
* 生产者消费者问题其含义就是先生产出了产品，才能拉出去让消费者购买 
* 一、重点： 
*    1、多个线程数据共享区域化思想！---源于多线程的近亲思想！！（类似于静态变量的改变） 
*    （如栈内存和对内存，还有当做栈内存和堆内存，如数组和基本数据类型，只要是访问的同一个。） 
*    2、生产者消费者 
*     
* 二、synchronized加锁: 
*  
*/ 
public class ProductAndCustomer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SyncStack stack = new SyncStack();
		Consumer p = new Consumer(stack);
		Consumer pb = new Consumer(stack);
		Consumer pc = new Consumer(stack);
		Consumer pd = new Consumer(stack);
		Consumer pe = new Consumer(stack);
		Producer c = new Producer(stack);
		  
		  
		Thread trda = new Thread(p);
		Thread trdb = new Thread(pb);
		Thread trdc = new Thread(pb);
		Thread trdd = new Thread(pb);
		Thread trde = new Thread(pb);
		trda.setName("A");
		trdb.setName("B");
		trdc.setName("C");
		trdd.setName("D");
		trde.setName("E");
		trda.start();
		trdb.start();
		trdc.start();
		trdd.start();
		trde.start();
		new Thread(c).start();

	}
}	
	
	class Producer implements Runnable{   //生产者  
	    private SyncStack stack;  
	  
	    public Producer(SyncStack stack){  
	    this.stack = stack;  
	    }  
	  
	    public void run(){  
	    for (int i = 0; i < stack.pro().length; i++){  
	    String product = "产品"+i;  
	    stack.push(product);
	    //System.out.println("生产了: "+product);
	    try{
	     Thread.sleep(20);  
	     }catch(InterruptedException e)
	      {  
	       e.printStackTrace();  
	     }  
	   }  
	}  
	}  
	  
	class Consumer implements Runnable{   //消费者  
	   private SyncStack stack;
	   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
       "yyyy-MM-dd HH:mm:ss.SSS");
	  
	   public Consumer(SyncStack stack) {  
	      this.stack = stack;  
	   }  
	     
	   public void run(){  
	   for(int i = 0; i < stack.pro().length; i++){
		   String threadName = Thread.currentThread().getName();
		   //System.out.println("消费者"+threadName+"--开始时间--"+sdf.format(new Date()));
	       String product = stack.pop();
	    
//	       System.out.println("消费者"+threadName+"==消费了: "+product);
		   try{
		       Thread.sleep(1000);
		   }catch(InterruptedException e){
		       e.printStackTrace();  
		   }
		   stack.showMsg(product);
	       //System.out.println("消费者"+threadName+"--结束时间--"+sdf.format(new Date()));
	    }  
	   }  
	}  
	  
	class SyncStack{   // 此类是（本质上：共同访问的）共享数据区域  
	    private String[] str = new String[30];
	    private int index=0;
	    private int outIndex=0;

	    public synchronized void push(String sst){ //供生产者调用  
	    if(index == str.length ){//
	     try{
	      wait(); 
	     }catch(InterruptedException e){
	       e.printStackTrace();
	      }
	    }
	    str[index] = sst;
	    index++;
	    System.out.println("生产了: "+sst);
	    this.notify(); //唤醒在此对象监视器上等待的单个线程  
	    //this.notifyAll();
	  }  
	  
	public synchronized String pop(){   //供消费者调用
		//System.out.println(outIndex+"--------"+index);
	    if(index == 0 || index - outIndex <= 0 ){
	    try{
	       wait();//当前线程执行到该位置暂停，如果唤醒该线程，则从该位置下面执行，不会从方法头部开始；所以，只能由生产者来唤醒，
	       //不然执行该方法体以下代码会出错。 
	      }catch (InterruptedException e){  
	       e.printStackTrace();  
	      }
	    }
	    //notify();
	    outIndex++;
	    String product = str[outIndex-1];
	    String threadName = Thread.currentThread().getName();
	    System.out.println("消费者"+threadName+"==消费了: "+product);
	    //notify();
	    return product;
	   }  
	  
	    public String[] pro(){ //就是定义一个返回值为数组的方法,返回的是一个String[]引用  
	     return str;   //这是一个String[]引用  
	    }
	    
	    /**
	     * 验证方法体内的变量，是否为线程安全
	     * @param pName
	     */
	    public void showMsg(String pName){
	    	String threadName = Thread.currentThread().getName();
	    	String newName = "";
	    	Double rmdb =Math.random()*1000;
	    	System.out.println("消费者"+threadName+"消费"+pName+"产生随机数------"+rmdb);
	    	
	    	try {
				Thread.sleep(rmdb.longValue() );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	System.out.println("消费者"+threadName+"消费"+pName+"产生随机数----end---"+rmdb);
	    	
	    }
	    
	    
	}
	
	
	
	
	
	
	
	
	
	


