package org.gmjm.akka.io;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.gmjm.akka.util.SysOutActor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Inet;
import akka.io.Tcp;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;
import akka.io.TcpSO;
import akka.util.ByteString;

public class TelnetActor extends UntypedActor {

	ActorRef tcp;
	
	@Override
	public void aroundPreStart() {
		super.aroundPreStart();
		
		tcp = Tcp.get(getContext().system()).manager();
		
		InetSocketAddress localAddr = new InetSocketAddress("127.0.0.1", 23);
		
		final List<Inet.SocketOption> options = new ArrayList<Inet.SocketOption>();
		options.add(TcpSO.reuseAddress(true));
		options.add(TcpSO.receiveBufferSize(1000));
		options.add(TcpSO.sendBufferSize(1000));
		options.add(TcpSO.tcpNoDelay(false));
		tcp.tell(TcpMessage.bind(getSelf(), localAddr, 10, options,false), getSelf());
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Connected) {
			accept((Connected)message);
		}
	}

	private void accept(Connected connected) {
		ActorRef commandActor = getContext().actorOf(Props.create(CommandActor.class),"commandActor");
		getSender().tell(TcpMessage.register(commandActor), getSelf());
		commandActor.tell(connected,getSender());
	}

}
