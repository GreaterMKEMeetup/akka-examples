package org.gmjm.akka.webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.gmjm.akka.util.FileWritingManager;
import org.gmjm.akka.util.SysOutActor;
import org.junit.Test;

import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import akka.routing.SmallestMailboxPool;

public class UrlCrawlUtilTest {


	static {
		System.setProperty("http.proxyHost", "internet.ground.fedex.com");
		System.setProperty("http.proxyPort", "80");
	}
	
	@Test
	public void testCrawl() throws InterruptedException, IOException {
		long startTime = System.currentTimeMillis();
		
		ActorSystem as = ActorSystem.create("crawler");
		
		Props crawlPropertiesSmallestMailbox = new SmallestMailboxPool(4).props(Props.create(CrawlActor.class));
		Props crawlPropertiesRoundRobin = new RoundRobinPool(4).props(Props.create(CrawlActor.class));
		
		as.actorOf(crawlPropertiesSmallestMailbox,"crawl_crawlActor");
		as.actorOf(Props.create(CrawlLogger.class),"crawl_crawlLogger");
		as.actorOf(Props.create(FileWritingManager.class),"output_fileManager");
		as.actorOf(Props.create(SysOutActor.class),"crawl_sysout");
		as.actorOf(Props.create(new SearchCollector.SearchCollectorCreator(1)),"output_searchCollector");
		
		as.actorSelection("akka://crawler/user/crawl*").tell(new SearchCrawl("http://aglassman.github.io",0,4,"indiana jones",true), null);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(!br.readLine().equals("stop")) {
			System.out.println("Runtime: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
		}
		
		br.close();
		
		as.shutdown();
		as.awaitTermination(new FiniteDuration(1000,TimeUnit.SECONDS));
		
		System.out.println("Terminated: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
	}
	
}
