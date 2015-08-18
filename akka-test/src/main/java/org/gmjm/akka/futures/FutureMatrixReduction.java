package org.gmjm.akka.futures;


import java.util.ArrayList;
import java.util.List;

import org.gmjm.matrix.Matrix;

import com.google.common.collect.Lists;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;

public class FutureMatrixReduction {
	public final List<Future<Matrix>> toReduce;
	public final ExecutionContext ec;

	public static volatile int count = 0;

	public final int id;
	
	public FutureMatrixReduction(List<Future<Matrix>> toReduce, ExecutionContext ec) {
		id = count++;
		this.toReduce = toReduce;
		this.ec = ec;
	}

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

	public static Matrix reduce(Iterable<Matrix> toReduce) {
		Matrix result = null;
		
		for(Matrix m : toReduce) {
			result = multiply(result, m);				
		}
		
		return result;
	}

	private static Matrix multiply(Matrix result, Matrix m) {
		if(result == null)
			return m;
		return Matrix.multiply(result, m);
	}
	
	public static Future<Matrix> create(List<Future<Matrix>> futures, int multiplicationsPerFuture, ExecutionContext ec) {
		return recursiveReduce(futures, multiplicationsPerFuture,  ec).get(0);
	}
	
	private static List<Future<Matrix>> recursiveReduce(List<Future<Matrix>> futures, int multiplicationsPerFuture, ExecutionContext ec) {
		
		if(futures.size() <= 1) {
			return futures;
		}
		
		List<Future<Matrix>> reduced = new ArrayList<>();
		
		Lists.partition(futures, 2).stream()
			.forEach(list -> reduced.add(new FutureMatrixReduction(list,ec).futureReduce()));
		
		return recursiveReduce(reduced, multiplicationsPerFuture, ec);
		
	}
}
