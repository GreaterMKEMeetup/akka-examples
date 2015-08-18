package org.gmjm.akka.futures;

import akka.dispatch.OnSuccess;

public class TimedResut<T> extends OnSuccess<T>{

	public final long start = System.currentTimeMillis();
	
	
	@Override
	public void onSuccess(T obj) throws Throwable {
		System.out.println("Completed in: " + (System.currentTimeMillis() - start)/1000 + " seconds");
	}

}
