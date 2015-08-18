package org.gmjm.akka.futures;

import java.util.concurrent.Callable;

import org.gmjm.matrix.Matrix;

public class SingleMatrix implements Callable<Matrix>{

	public final Matrix matrix;
	
	public SingleMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	@Override
	public Matrix call() throws Exception {
		return matrix;
	}

}
