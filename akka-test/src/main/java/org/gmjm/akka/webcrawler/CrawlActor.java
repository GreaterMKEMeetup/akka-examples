package org.gmjm.akka.webcrawler;

import java.io.IOException;

import org.gmjm.akka.util.FileOutput;
import org.gmjm.akka.util.UrlCrawlUtil;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import akka.actor.UntypedActor;

public class CrawlActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		try {
			if (message instanceof Crawl) {
					handleCrawl((Crawl)message);
			}
		} catch (Exception e) {
			handleException(message, e);
		}
	}



	private void handleCrawl(Crawl crawl) throws SAXException, IOException {
		
		if (crawl.urlDepth < crawl.maxDepth) {
			Document d = UrlCrawlUtil.crawl(crawl.url);

			d.getElementsByTag("a")
					.stream()
					.map(a -> a.attr("href"))
					.filter(url -> url.startsWith("http://") || url.startsWith("https://"))
					.distinct()
					.filter(url -> !crawl.hasVisited(url))
					.map(url -> crawl.createLevelDeeper(url))
					.forEach(c -> getContext().actorSelection("akka://crawler/user/crawl*").tell(c, getSelf()) );

			if (crawl instanceof SearchCrawl) {
				handleSearchCrawl((SearchCrawl)crawl, d);
			}
			
		}
	}

	private void handleSearchCrawl(SearchCrawl searchCrawl, Document d) {
		d.getElementsContainingOwnText(searchCrawl.search).stream()
			.map(e -> new SearchResult("C:/crawl/searchFound.txt",String.format("%s : %s%n",searchCrawl,e)))
			.forEach(c -> getContext().actorSelection("akka://crawler/user/output*").tell(c, getSelf()));
	}
	
	private void handleException(Object message, Exception e) {
		FileOutput fileOutput = new FileOutput("C:/crawl/err.txt", String.format("%n%s : %n%s", e.getMessage(),message));
		getContext().actorSelection("akka://crawler/user/output*").tell(fileOutput, getSelf());
	}

}
