package org.gmjm.akka.webcrawler;

import org.apache.commons.lang3.StringUtils;



public class Crawl {
	
	public final Crawl previous;
	
	public final String url;
	public final int urlDepth;
	public final int maxDepth;
	
	protected boolean printFullCrawl;
	
	public Crawl(String url, int urlDepth, int maxDepth) {
		this.previous = null;
		this.url = url;
		this.urlDepth = urlDepth;
		this.maxDepth = maxDepth;
		this.printFullCrawl = false;
	}
	
	public Crawl(String url, int urlDepth, int maxDepth, boolean printFullCrawl) {
		this(url,urlDepth,maxDepth);
		this.printFullCrawl = printFullCrawl;
	}
	
	protected Crawl(Crawl previous, String url, int urlDepth, int maxDepth) {
		this.previous = previous;
		this.url = url;
		this.urlDepth = urlDepth;
		this.maxDepth = maxDepth;
		this.printFullCrawl = false;
	}
	
	protected Crawl(Crawl previous, String url, int urlDepth, int maxDepth, boolean printFullCall) {
		this(previous,url,urlDepth,maxDepth);
		this.printFullCrawl = printFullCall;
	}
	
	
	
	@Override
	public String toString() {
		if(printFullCrawl)
			return recursiveToString(0);
		return urlDepth + " : " + url;
	}

	private String recursiveToString(int i) {
		String indent = StringUtils.repeat('\t', i);
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%sCrawl [%n",indent));
		sb.append(String.format("%s\tprevious = %s%n",indent,previous != null ? previous.recursiveToString(i+1) : "null"));
		sb.append(String.format("%s\turl = %s%n",indent,url));
		sb.append(String.format("%s\turlDepth = %s%n",indent,urlDepth));
		sb.append(String.format("%s\tmaxDepth = %s ]",indent,maxDepth));
		
		return sb.toString();
	}
	
	public Crawl createLevelDeeper(String url) {
		return new Crawl(this, url, urlDepth + 1, maxDepth,printFullCrawl);
	}
	
	public boolean hasVisited(String url) {
		return recursiveHasVisited(this,url);
	}

	private static boolean recursiveHasVisited(Crawl crawl, String url) {
		if(crawl == null)
			return false;
		
		if(crawl.url.equals(url))
			return true;
		
		return recursiveHasVisited(crawl.previous, url); 
	}
}
