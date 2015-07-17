package org.gmjm.akka.webcrawler;

import org.gmjm.akka.util.FileOutput;

public class SearchResult extends FileOutput {

	public SearchResult(String filePath, String string) {
		super(filePath, string);
	}

	@Override
	public String toString() {
		return "SearchResult [filePath=" + filePath + ", string=" + string + "]";
	}
	
	

}
