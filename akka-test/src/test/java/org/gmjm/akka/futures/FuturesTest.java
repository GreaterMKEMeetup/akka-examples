package org.gmjm.akka.futures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.gmjm.matrix.Matrix;
import org.gmjm.matrix.MatrixStreamParser;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

public class FuturesTest {
	
	@Test
	public void generateTestData() throws IOException {
		File testFile = new File("C:/matrix/testFile.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
		
		Random rand = new Random();
		
		int numOfMatricies = 50000;
		
		int cols = 10;
		int rows = 10;
		
		for(int i = 0; i < numOfMatricies; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(rows).append(",");
			sb.append(cols).append(",");
			for(int j = 0; j < cols * rows; j ++)
				sb.append(rand.nextInt()%10).append(",");
			sb.append("\n");
			bw.append(sb.toString());
			
		}
		
		bw.close();
	
		
	}
	
	@Test
	public void testNonFuturesMatrixMultiplication() throws Throwable {
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 waitForInput("load", br);
		
		File testFile = new File("C:/matrix/testFile.txt");
		List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));

		waitForInput("start", br);
		
		TimedResut tr = new TimedResut();
		
		FutureMatrixReduction.reduce(matricies);
		
		tr.onSuccess(null);
	}
	
	@Test
	public void testFuturesMatrixMultiplication() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(16);
	    ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    
	    waitForInput("load", br);
	    
	    File testFile = new File("C:/matrix/testFile.txt");
	  
	    List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));
		
	    waitForInput("start", br);
	   	    
	    List<Future<Matrix>> initial = matricies.stream().map(matrix -> Futures.future(new SingleMatrix(matrix),ec)).collect(Collectors.toList());
	    
	    Future<Matrix> finalProduct = FutureMatrixReduction.create(initial,2, ec);
		
	    finalProduct.onSuccess(new TimedResut<Matrix>(), ec);
		Await.result(finalProduct, new FiniteDuration(1000,TimeUnit.SECONDS));
		
		waitForInput("exit", br);
		
		
	} 
	
	private void waitForInput(String toMatch, BufferedReader br) throws IOException {
		System.out.println("Waiting for: " + toMatch);
		while(!br.readLine().equals(toMatch)) {
			System.out.println("Waiting for: " + toMatch);
		}
	}
	

}
