package org.gmjm.akka;

import java.util.concurrent.TimeUnit;

import org.gmjm.akka.hello.GreeterActor;
import org.gmjm.akka.util.SysOutActor;
import org.junit.Test;

import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;

public class HelloWorldTest {
	
	
	@Test
	public void greetingTest() throws InterruptedException {
		ActorSystem as = ActorSystem.create("greeter-system");
		Inbox inbox = Inbox.create(as);
		
		ActorRef greeter = as.actorOf(Props.create(GreeterActor.class),"greeter");
		as.actorOf(Props.create(SysOutActor.class),"sysout");
				
		//non blocking =)
		inbox.send(greeter, "Andy");
		
		//blocking =(
		String message = (String)inbox.receive(new FiniteDuration(100, TimeUnit.MILLISECONDS));
		
		System.out.println(message);
		
		as.shutdown();
	}
	
}
