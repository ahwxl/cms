package com.bplow.cms;

public class C {
	
	public A.B b;
	
	
	public static void main(String[] args) {
		C c1 = new C();
		C c2 = new C();
		
		A a1 = new A();
		A a2 = new A();
		
		a1.get(c1);
		a2.get(c2);
	}

}
