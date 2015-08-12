package com.bplow.cms;

public class A {
	
	public Object get(C c){
		
		if(c.b == null){
			c.b = new A.B(this);
		}
		
		return c;
	}
	
	static class B{
		B(A a){
			System.out.println("静态内部b，执行1");
		}
	}

}
