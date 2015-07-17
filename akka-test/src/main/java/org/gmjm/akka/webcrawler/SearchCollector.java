package org.gmjm.akka.webcrawler;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

public class SearchCollector extends UntypedActor {

	static class SearchCollectorCreator implements Creator<SearchCollector> {
		
		int numberToCollect;
		
		public SearchCollectorCreator(int numberToCollect) {
			this.numberToCollect = numberToCollect;
		}
		
	     @Override public SearchCollector create() {
	          return new SearchCollector(numberToCollect);
	     }
	}
	
	final int numberToCollect;
	int count = 0;
	public SearchCollector(int numberToCollect){
		this.numberToCollect = numberToCollect;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof SearchResult)
		{
			count++;
			getContext().system().log().info(String.format("Found Search Result: %n%s", message.toString()));
			if(count == numberToCollect){
				getContext().system().log().info(String.format("Desired number of search results reached: %s", count));
				getContext().system().shutdown();
			}
		}
	}

}
