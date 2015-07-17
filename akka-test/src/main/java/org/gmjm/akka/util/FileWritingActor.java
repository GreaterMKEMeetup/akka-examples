package org.gmjm.akka.util;

import java.io.BufferedWriter;
import java.io.FileWriter;

import akka.actor.UntypedActor;

public class FileWritingActor extends UntypedActor{

	private BufferedWriter bw = null;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof FileOutput) {
			FileOutput fo = (FileOutput)message;
			
			if(bw == null)
				bw = new BufferedWriter(new FileWriter(fo.filePath));
			
			bw.write(fo.string);
			
		} 
		
	}
	
	@Override
	public void postStop() throws Exception {
		super.postStop();
		bw.close();
	}

}
