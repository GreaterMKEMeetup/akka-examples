package org.gmjm.akka.webcrawler;

import org.gmjm.akka.util.FileOutput;

import akka.actor.UntypedActor;

public class CrawlLogger extends UntypedActor{
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Crawl)
		{
			Crawl crawl = (Crawl)message;
						
			getContext().actorSelection("akka://crawler/user/output_fileManager").tell(
					new FileOutput(String.format("C:/crawl/level_%s.txt",crawl.urlDepth),String.format("%s%n",message))
					, getSelf());
		}
	}

}
