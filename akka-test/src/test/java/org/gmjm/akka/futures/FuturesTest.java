package org.gmjm.akka.futures;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.gmjm.matrix.Matrix;
import org.gmjm.matrix.MatrixStreamParser;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import akka.dispatch.ExecutionContexts;

public class FuturesTest {
	
	 BufferedReader br;
	
	@Before
	public void setup() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * Test matrix multiplication using the not-future based reduction.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testNonFuturesMatrixMultiplication() throws Throwable {
		
		 waitForInput("load");
		
		byte[] testData = generateTestData(5000, 10, 10);
		List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new ByteArrayInputStream(testData));

		waitForInput("start");
		
		TimedResut tr = new TimedResut();
		
		FutureMatrixReduction.reduce(matricies);
		
		tr.onSuccess(null);
		
		waitForInput("exit");
	}
	
	/**
	 * Test matrix multiplication using the future based reduction.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFuturesMatrixMultiplication() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(16);
	    ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
	   	    
	    waitForInput("load");
	    
	    byte[] testData = generateTestData(5000, 10, 10);		
	    List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new ByteArrayInputStream(testData));
		
	    waitForInput("start");
	   		    
	    Future<Matrix> finalProduct = FutureMatrixReduction.create(matricies,3, ec);
		
	    finalProduct.onSuccess(new TimedResut<Matrix>(), ec);
		Await.result(finalProduct, new FiniteDuration(1000,TimeUnit.SECONDS));
		
		waitForInput("exit");
		
		
	} 
	
	
	/**
	 * Test matrix multiplication using the future based reduction.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCompareResults() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(16);
	    ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
	    
	    
	    waitForInput("load");
	    
	    byte[] testData = generateTestData(100, 10, 10);		
	    List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new ByteArrayInputStream(testData));
		
	    waitForInput("start");
	   		    
	    Future<Matrix> finalProduct = FutureMatrixReduction.create(matricies,3, ec);
		
	    
	    finalProduct.onSuccess(new TimedResut<Matrix>(), ec);
		Matrix a = Await.result(finalProduct, new FiniteDuration(1000,TimeUnit.SECONDS));
		Matrix b = FutureMatrixReduction.reduce(matricies);
		
		System.out.println(a);
		System.out.println(b);
		
		assertEquals(a,b);
		
		waitForInput("exit");
		
		
	} 
	
	/**
	 * This method allows the user to step through segments of the test via the command line.
	 * This gives the user the ability to connect to the JVM via jconsole or another monitoring tool before
	 * the interesting part of the test executes.
	 * 
	 * @param toMatch
	 * @param br
	 * @throws IOException
	 */
	private void waitForInput(String toMatch) throws IOException {
		System.out.println("Waiting for: " + toMatch);
		while(!br.readLine().equals(toMatch)) {
			System.out.println("Waiting for: " + toMatch);
		}
	}
	
	/** 
	 * Provides the ability to write a byte array to a file for inspection purposes.
	 * 
	 * @param fileLocation
	 * @param testData
	 * @throws IOException
	 */
	public void writeToFile(String fileLocation, byte[] testData) throws IOException {
		File testFile = new File(fileLocation);
		OutputStream fileOutput = new FileOutputStream(testFile);
		fileOutput.write(testData);
		fileOutput.close();
	}
	
	/**
	 * Generate a byte array that represents a list of matrices all of the same size.
	 * Generated values are random 0-9 integers.
	 * 
	 * Matrix format is numRows,numCols,val,val,val....  
	 * Values are specified by row, for example:<br><br>
	 * [1 2 3]<br>
	 * [0 8 2]<br>
	 * [3 4 1]<br>
	 * <br>
	 * Is represented as:<br>
	 * 3,3,1,2,3,0,8,2,3,4,1
	 * 
	 * A new line character separates each matrix.
	 * 
	 * @param numOfMatricies
	 * @param cols
	 * @param rows
	 * @return
	 * @throws IOException
	 */
	public byte[] generateTestData(int numOfMatricies, int cols, int rows) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(baos));
		
		Random rand = new Random();
			
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
	
		return baos.toByteArray();
	}
	

}
