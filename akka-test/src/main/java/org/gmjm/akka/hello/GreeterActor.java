package org.gmjm.akka.hello;

import akka.actor.UntypedActor;

public class GreeterActor extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof String)
		{
			getContext().actorSelection("../sysout").tell("Hello " + message, getSelf());
			getSender().tell("Greeting Complete", getSelf());
			return;
		}
		
		unhandled(message);
	}

}
