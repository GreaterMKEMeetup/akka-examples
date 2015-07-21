package org.gmjm.matrix;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix {
	private final BigInteger[] values;
	private final int rows;
	private final int cols;
	
	public Matrix(int rows, int cols, BigInteger[] matrix) {
		this.values = matrix.clone();
		this.rows = rows;
		this.cols = cols;
	}

	/**
	 * Creates a matrix
	 * 
	 * [0] = number of rows
	 * [1] = number of columns
	 * [2..length] = matrix data.
	 * 
	 * @param matrix
	 */
	public Matrix(BigInteger[] matrix) {
		this.rows = matrix[0].intValue();
		this.cols = matrix[1].intValue();
		this.values = Arrays.copyOfRange(matrix, 2, matrix.length);
	}
	
	/**
	 * Creates a matrix
	 * 
	 * [0] = number of rows
	 * [1] = number of columns
	 * [2..length] = matrix data.
	 * 
	 * @param matrix
	 */
	public Matrix(int[] matrix) {
		this.rows = matrix[0];
		this.cols = matrix[1];
				
		this.values = new BigInteger[matrix.length - 2];
		
		for(int i = 0 ; i < values.length; i++)
			this.values[i] = BigInteger.valueOf(matrix[i+2]);
	}
	
	public int rows() {
		return rows;
	}
	
	public int cols() {
		return cols;
	}
	
	public BigInteger getValue(int row, int col) {
		return getValue(row,col,this);
	}
	
	static void setValue(int row, int col, int rows, int cols, BigInteger value, BigInteger[] values)
	{
		values[calcuateIndex(row,col,rows,cols)] = value;
	}
	
	static BigInteger getValue(int row, int col, Matrix matrix) {
		return matrix.values[calcuateIndex(row, col, matrix.rows, matrix.cols)];
	}

	private static int calcuateIndex(int row, int col, int rows, int cols) {
		return (row+1/rows) * cols + col % cols;
	}
	
	public static Matrix multiply(Matrix a, Matrix b) {
		if(a.cols != b.rows)
		{
			throw new IllegalArgumentException(String.format("Cannot multiply. A.cols must equal B.rows. (%s != %s).",a.cols,b.rows));  
		}
		
		BigInteger[] newMatrix = new BigInteger[a.rows * b.cols];
		
		for(int i = 0; i < a.cols; i ++) {
			for(int j = 0; j < b.rows; j++) {
				BigInteger value = new BigInteger("0");
				for(int k = 0; k < a.rows; k++) {
					value = value.add(a.getValue(i, k).multiply(b.getValue(k, j)));
				}
				setValue(i, j, a.rows, b.cols, value, newMatrix);
			}
			
		}		
		
		return new Matrix(a.rows, b.cols, newMatrix);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Matrix)
		{
			Matrix m = (Matrix)object;
			if(rows == m.rows && cols == m.cols){
				for(int row = 0; row < rows; row++)
					for(int col = 0; col < cols; col++)
						if(!m.getValue(row, col).equals(getValue(row, col)))
							return false;
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (rows * 3) + (cols * 7) + values.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				sb.append(String.format("[%s]",getValue(row, col)));
			}
			sb.append("\n");
		}
		return sb.toString();	
	}
}
