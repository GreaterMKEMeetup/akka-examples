package org.gmjm.akka.io;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class IoTest {
	@Test
	public void testConnectTcp() {
		ActorSystem as = ActorSystem.create("tcp-system");
		
		as.actorOf(Props.create(TelnetActor.class),"telnetActor");
		
		as.awaitTermination(new FiniteDuration(1000,TimeUnit.SECONDS));
	}
}
