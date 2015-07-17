package org.gmjm.akka.webcrawler;


public class SearchCrawl extends Crawl {

	public final String search;
	
	public SearchCrawl(String url, int urlDepth, int maxDepth, String search) {
		super(url, urlDepth, maxDepth);
		this.search = search;
	}
	
	public SearchCrawl(String url, int urlDepth, int maxDepth, String search, boolean printFullCrawl) {
		super(url, urlDepth, maxDepth, printFullCrawl);
		this.search = search;
	}
	
	protected SearchCrawl(Crawl previous, String url, int urlDepth, int maxDepth, String search) {
		super(previous, url, urlDepth, maxDepth);
		this.search = search;
	}
	
	protected SearchCrawl(Crawl previous, String url, int urlDepth, int maxDepth, String search, boolean printFullCrawl) {
		super(previous, url, urlDepth, maxDepth, printFullCrawl);
		this.search = search;
	}

	@Override
	public String toString() {
		return super.toString() + String.format(" [search = %s]",search);		
	}

	@Override
	public Crawl createLevelDeeper(String url) {
		return new SearchCrawl(this,url, urlDepth + 1, maxDepth, search, printFullCrawl);
	}

	
	
	
}
