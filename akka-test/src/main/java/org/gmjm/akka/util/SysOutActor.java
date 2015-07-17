package org.gmjm.akka.util;

import akka.actor.UntypedActor;

public class SysOutActor extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println(message.toString());
	}

}
