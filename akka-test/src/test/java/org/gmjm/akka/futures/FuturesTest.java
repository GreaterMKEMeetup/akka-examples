package org.gmjm.akka.futures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.gmjm.matrix.Matrix;
import org.gmjm.matrix.MatrixStreamParser;
import org.junit.Test;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

public class FuturesTest {
	
	@Test
	public void generateTestData() throws IOException {
		File testFile = new File("C:/matrix/testFile.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
		
		Random rand = new Random();
		
		int numOfMatricies = 1000;
		
		int cols = 50;
		int rows = 50;
		
		for(int i = 0; i < numOfMatricies; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(rows).append(",");
			sb.append(cols).append(",");
			for(int j = 0; j < cols * rows; j ++)
				sb.append(rand.nextInt()).append(",");
			sb.append("\n");
			bw.append(sb.toString());
			
		}
		
		bw.close();
	
			
		
	}
	
	@Test
	public void testNonFuturesMatrixMultiplication() throws FileNotFoundException, IOException {
		File testFile = new File("C:/matrix/testFile.txt");
		List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));
		
		Matrix result = matricies.remove(0);
		
		for(Matrix m : matricies)
		{
			result = Matrix.multiply(result, m);
		}
				
		//System.out.println(result);
		
	}
	
	@Test
	public void testFuturesMatrixMultiplication() throws FileNotFoundException, IOException, InterruptedException {
		
		ActorSystem as = ActorSystem.create();
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
	    ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
	    
	    File testFile = new File("C:/matrix/testFile.txt");
	  
	    List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));
		
	    
		Future<Matrix> mFuture = Futures.future(new MatrixReduction(matricies.get(0), matricies.get(1)),ec);
	    
		mFuture.onSuccess(new PrintResult(), as.dispatcher());
		
		Thread.sleep(10000);
	} 
}
