package org.gmjm.akka.futures;

import java.util.concurrent.Callable;

import org.gmjm.matrix.Matrix;

public class MatrixReduction implements Callable<Matrix>{

	public final Iterable<Matrix> toReduce;
	
	
	
	public MatrixReduction(Iterable<Matrix> toReduce) {
		super();
		this.toReduce = toReduce;
	}

	@Override
	public Matrix call() throws Exception {
		
		return reduce(toReduce);
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

}
