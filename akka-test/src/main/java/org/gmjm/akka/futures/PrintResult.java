package org.gmjm.akka.futures;

import org.gmjm.matrix.Matrix;

import akka.dispatch.OnSuccess;

public class PrintResult extends OnSuccess<Matrix>{

	@Override
	public void onSuccess(Matrix matrix) throws Throwable {
		System.out.println(matrix);
	}

}
