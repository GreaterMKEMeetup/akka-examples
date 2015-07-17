package org.gmjm.akka.util;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileWritingManager extends UntypedActor{

	Map<String,ActorRef> fileMap = new HashMap<String,ActorRef>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof FileOutput)
		{
			FileOutput fo = (FileOutput)message;
			
			if(!fileMap.containsKey(fo.filePath)) {
				fileMap.put(fo.filePath, getContext().actorOf(Props.create(FileWritingActor.class)));
			}
			
			fileMap.get(fo.filePath).tell(fo, getSelf());
		}
	}

}
