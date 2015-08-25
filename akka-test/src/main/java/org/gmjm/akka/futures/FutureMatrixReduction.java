package org.gmjm.akka.futures;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gmjm.matrix.Matrix;

import com.google.common.collect.Lists;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;

public class FutureMatrixReduction {
	
	public final List<Future<Matrix>> toReduce;
	public final ExecutionContext ec;
	
	/**
	 * Create a FutureMatrixReduction object given the list of Future<Matrix> objects to reduce,
	 * and the given ExecutionContext.
	 * 
	 * @param toReduce
	 * @param ec
	 */
	private FutureMatrixReduction(List<Future<Matrix>> toReduce, ExecutionContext ec) {
		this.toReduce = toReduce;
		this.ec = ec;
	}

	/**
	 * Reduce the toReduce list of futures.
	 * 
	 * @return
	 */
	public Future<Matrix> futureReduce() {
		Future<Iterable<Matrix>> futureSubProducts = Futures.sequence(toReduce, ec);
		
		Future<Matrix> finalProduct = futureSubProducts.map(new Mapper<Iterable<Matrix>,Matrix>(){
			@Override
			public Matrix apply(Iterable<Matrix> matricies) {
				return reduce(matricies);
			}
		}, ec);
		
		return finalProduct;
	}

	/**
	 * Reduce an Iterable<Matrix> object to a single matrix
	 * by multiplying all Matrices.  
	 * <br>
	 * (A,B,C,D) ->
	 * ((A*B) * C) * D) -> E
	 * <br>
	 * E is the product of all matrices.
	 * 
	 * @param toReduce
	 * @return
	 */
	public static Matrix reduce(Iterable<Matrix> toReduce) {
		Matrix result = null;
		
		for(Matrix m : toReduce) {
			result = multiply(result, m);				
		}
		
		return result;
	}

	/**
	 * Multiply Matrix a by Matrix b.
	 * 
	 * If a == null, return b.
	 *  
	 * @param a
	 * @param b
	 * @return
	 */
	private static Matrix multiply(Matrix a, Matrix b) {
		if(a == null)
			return b;
		return Matrix.multiply(a, b);
	}
	
	/**
	 * Create a Future<Matrix> from a list of matrices.  This method recursively partitions the input list
	 * by <i>multiplicationsPerFuture</i> until there is only 1 Future<Matrix> remaining.  This creates a 
	 * tree structure of Futures that can be multiplied in parallel, but still retain their correct ordering.
	 * 
	 * 
	 * @param futures
	 * @param multiplicationsPerFuture
	 * @param ec
	 * @return
	 */
	public static Future<Matrix> create(List<Matrix> futures, int multiplicationsPerFuture, ExecutionContext ec) {
		List<Future<Matrix>> futuresToReduce = futures.stream().map(matrix -> Futures.future(new SingleMatrix(matrix),ec)).collect(Collectors.toList());
		return recursiveReduce(futuresToReduce, multiplicationsPerFuture,  ec).get(0);
	}
	
	/**
	 * Recursively reduce the futures until there is only one remaining element.
	 * 
	 * @param futures
	 * @param multiplicationsPerFuture
	 * @param ec
	 * @return
	 */
	private static List<Future<Matrix>> recursiveReduce(List<Future<Matrix>> futures, int multiplicationsPerFuture, ExecutionContext ec) {
		
		if(futures.size() <= 1) {
			return futures;
		}
		
		List<Future<Matrix>> reduced = new ArrayList<>();
		
		Lists.partition(futures, multiplicationsPerFuture).stream()
			.forEach(list -> reduced.add(new FutureMatrixReduction(list,ec).futureReduce()));
		
		return recursiveReduce(reduced, multiplicationsPerFuture, ec);
		
	}
}
