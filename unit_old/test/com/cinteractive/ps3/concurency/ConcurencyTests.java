package com.cinteractive.ps3.concurency;

import java.util.HashSet;
import java.util.Set;

import com.cinteractive.ps3.attributes.SimpleAttribute;
import com.cinteractive.ps3.lock.PrioritySemaphore;
import com.cinteractive.ps3.test.PSTestBase;

public class ConcurencyTests extends PSTestBase {

    public ConcurencyTests(String name) { super(name); }
    
    private static String PREFIX = "prefix";
    private Set set = new HashSet(); 
    	
    private class AttributeThread extends Thread {
    	private String name;
    	
    	public AttributeThread(String name) {
    		this.name = name;
    	}
    	
    	public void setNameParam(String name) {
    		this.name= name;
    	}
    	
    	public void run() {
    		while(!this.isInterrupted()) {
    			SimpleAttribute.cachedName(name);
    		}
    	}
    }
    
    private static class PSSemaphoreThread extends Thread {
		private String name;
		private volatile long i;
		private int priority;
		
		private static final PrioritySemaphore semaphore = new PrioritySemaphore();

		public PSSemaphoreThread(int priority, String name) {
			this.name = name;
			this.priority = priority;
		}

		public long getCount(){
			return i;
		}
		
		public String getNameParam() {
			return name;
		}
		
		public void run() {
			while (!this.isInterrupted()) {
				try {
					semaphore.acquire(priority);
					// do something
					i++;
				} catch (Exception ignored) {

				} finally {
					semaphore.release(priority);
				}
			}
		}
	}
    
    public void testPSSemaphore() {
    	final int priority1 = 1;
    	final int priority3 = 3;
    	final int priority7 = 7;
    	PSSemaphoreThread tr1 = new PSSemaphoreThread(priority1, "name1");
    	PSSemaphoreThread tr2 = new PSSemaphoreThread(priority1, "name2");
    	PSSemaphoreThread tr3 = new PSSemaphoreThread(priority3, "name3");
    	PSSemaphoreThread tr4 = new PSSemaphoreThread(priority3, "name4");
    	PSSemaphoreThread tr5 = new PSSemaphoreThread(priority7, "name5");
    	PSSemaphoreThread tr6 = new PSSemaphoreThread(priority7, "name6");
    	tr1.start();
    	tr2.start();
    	tr3.start();
    	tr4.start();
    	tr5.start();
    	tr6.start();
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	tr1.interrupt();
    	tr2.interrupt();
    	tr3.interrupt();
    	tr4.interrupt();
    	tr5.interrupt();
    	tr6.interrupt();
    	System.out.println(tr1.getNameParam() + " : " + String.valueOf(tr1.getCount()));
    	System.out.println(tr2.getNameParam() + " : " + String.valueOf(tr2.getCount()));
    	System.out.println(tr3.getNameParam() + " : " + String.valueOf(tr3.getCount()));
    	System.out.println(tr4.getNameParam() + " : " + String.valueOf(tr4.getCount()));
    	System.out.println(tr5.getNameParam() + " : " + String.valueOf(tr5.getCount()));
    	System.out.println(tr6.getNameParam() + " : " + String.valueOf(tr6.getCount()));
    	long res1 =  tr1.getCount() + tr2.getCount();
    	long res3 =  tr3.getCount() + tr4.getCount();
    	long res7 =  tr5.getCount() + tr6.getCount();
    	System.out.println( priority1 + " : " + String.valueOf(res1));
    	System.out.println( priority3 +  " : " + String.valueOf(res3));
    	System.out.println( priority7 +  " : " + String.valueOf(res7));
    	this.assertTrue(res7 > res3 && res3 > res1);
    }
    
    // just to estimate performance.
    public void testDeadLockOnAttributeNames() {
    	for (int i=0; i<=80000; i++) {
    		String name = PREFIX + String.valueOf(i);
    		// was used to protect weakHashMap from gc.
    		// is not actual now.
    		set.add(name);
    		SimpleAttribute.cachedName(name);
    	}
    	
    	AttributeThread tr1 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr2 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr3 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr4 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr5 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr6 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr7 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr8 = new AttributeThread(PREFIX+"60000");
    	AttributeThread tr9 = new AttributeThread(PREFIX+"60000");
    	tr1.start();
    	tr2.start();
    	tr3.start();
    	tr4.start();
    	tr5.start();
    	tr6.start();
    	tr7.start();
    	tr8.start();
    	tr9.start();
    	long start = System.currentTimeMillis();
    	for (int i=0; i<=3000000; i++) {
    		SimpleAttribute.cachedName(PREFIX + "60000");
    	}
    	long end = System.currentTimeMillis();
    	System.out.print((end-start)/1000);
    	tr1.interrupt();
    	tr2.interrupt();
    	tr3.interrupt();
    	tr4.interrupt();
    	tr5.interrupt();
    	tr6.interrupt();
    	tr7.interrupt();
    	tr8.interrupt();
    	tr9.interrupt();
    }
}
