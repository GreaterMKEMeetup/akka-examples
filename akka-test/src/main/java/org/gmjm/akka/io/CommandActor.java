package org.gmjm.akka.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.UntypedActor;
import akka.io.TcpMessage;
import akka.io.Tcp.Connected;
import akka.io.Tcp.Received;
import akka.util.ByteString;
import scala.collection.mutable.StringBuilder;

public class CommandActor extends UntypedActor{

	private static final String prompt = "#>";
	
	private static final Pattern command = Pattern.compile(".*;\\s*\\n",Pattern.DOTALL);
	
	StringBuilder sb;
	
	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println(message);
		
		if(message instanceof Connected) {
			handle((Connected)message);
		}
		
		if(message instanceof Received) {
			handle(((Received)message).data());
		}
		
	}

	private void handle(Connected message) {
		getSender().tell(TcpMessage.write(ByteString.fromString("Connected\r\n"+prompt)),getSelf());
	}

	private void handle(ByteString message) {
		if(sb == null)
			sb = new StringBuilder();
		
		sb.append(new String(message.toArray()));
		
		String currentBuffer = sb.toString();
		
		if(commandComplete(currentBuffer))
		{
			processCommand(currentBuffer);
			getSender().tell(TcpMessage.write(ByteString.fromString("\r\n"+prompt)),getSelf());
			sb = null;
		}
	}

	
	private boolean commandComplete(String s) {

		Matcher m = command.matcher(s);
		return m.matches();
		
	}

	private void processCommand(String command) {
		System.out.println(command);
	}

}
