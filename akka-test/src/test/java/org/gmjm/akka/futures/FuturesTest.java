package org.gmjm.akka.futures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.gmjm.matrix.Matrix;
import org.gmjm.matrix.MatrixStreamParser;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;

public class FuturesTest {
	
	@Ignore
	@Test
	public void generateTestData() throws IOException {
		File testFile = new File("C:/matrix/testFile.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
		
		Random rand = new Random();
		
		int numOfMatricies = 12;
		
		int cols = 320;
		int rows = 320;
		
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
	public void testNonFuturesMatrixMultiplication() throws FileNotFoundException, IOException, InterruptedException {
		File testFile = new File("C:/matrix/testFile.txt");
		List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));
		
		MatrixReduction.reduce(matricies);
		
	}
	
	@Test
	public void testFuturesMatrixMultiplication() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(4);
	    ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
	    
	    File testFile = new File("C:/matrix/testFile.txt");
	  
	    List<Matrix> matricies = MatrixStreamParser.parseMatriciesStream(new FileInputStream(testFile));
		
	    List<Future<Matrix>> futures = Lists.partition(matricies, 3).stream()
	    	.map(matrixList -> new MatrixReduction(matrixList))
	    	.map(matrixReduction -> Futures.future(matrixReduction, ec))
	    	.collect(Collectors.toList());
	    
	   
	    
		Future<Iterable<Matrix>> futureSubProducts = Futures.sequence(futures, ec);
		
		Future<Matrix> finalProduct = futureSubProducts.map(new Mapper<Iterable<Matrix>,Matrix>(){
			@Override
			public Matrix apply(Iterable<Matrix> matricies) {
				return MatrixReduction.reduce(matricies);
			}
		}, ec);
		
		Await.result(finalProduct, new FiniteDuration(1000,TimeUnit.SECONDS));
		
	} 
}
