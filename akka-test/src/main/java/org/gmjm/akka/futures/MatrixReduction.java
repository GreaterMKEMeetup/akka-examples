package org.gmjm.akka.futures;

import java.util.concurrent.Callable;

import org.gmjm.matrix.Matrix;

public class MatrixReduction implements Callable<Matrix>{

	public final Matrix a;
	public final Matrix b;
	
	
	
	public MatrixReduction(Matrix a, Matrix b) {
		super();
		this.a = a;
		this.b = b;
	}



	@Override
	public Matrix call() throws Exception {
		return Matrix.multiply(a, b);
	}

}
