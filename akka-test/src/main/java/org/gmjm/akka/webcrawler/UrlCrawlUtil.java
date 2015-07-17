package org.gmjm.akka.webcrawler;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

public class UrlCrawlUtil {
	

	
	//public static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("internet.ground.fedex.com", 80));
	

	
	public static Document crawl(String url) throws SAXException, IOException {
		return Jsoup.parse(new URL(url).openStream(), "UTF-8", url);		
	}
}
