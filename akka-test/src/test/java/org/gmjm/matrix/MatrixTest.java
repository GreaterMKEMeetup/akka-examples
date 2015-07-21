package org.gmjm.matrix;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class MatrixTest {
	@Test
	public void testCreate2x2() {
		Matrix a = new Matrix(new int[]{2,2,1,2,3,4});
		System.out.println(a);
		assertEquals(BigInteger.valueOf(1),a.getValue(0, 0));
		assertEquals(BigInteger.valueOf(2),a.getValue(0, 1));
		assertEquals(BigInteger.valueOf(3),a.getValue(1, 0));
		assertEquals(BigInteger.valueOf(4),a.getValue(1, 1));
	}
	
	@Test
	public void testCreate2x3() {
		Matrix a = new Matrix(new int[]{2,3,1,2,3,4,5,6});
		System.out.println(a);
		assertEquals(BigInteger.valueOf(1),a.getValue(0, 0));
		assertEquals(BigInteger.valueOf(2),a.getValue(0, 1));
		assertEquals(BigInteger.valueOf(3),a.getValue(0, 2));
		assertEquals(BigInteger.valueOf(4),a.getValue(1, 0));
		assertEquals(BigInteger.valueOf(5),a.getValue(1, 1));
		assertEquals(BigInteger.valueOf(6),a.getValue(1, 2));
	}
	
	@Test
	public void testCreate3x2() {
		Matrix a = new Matrix(new int[]{3,2,1,2,3,4,5,6});
		System.out.println(a);
		assertEquals(BigInteger.valueOf(1),a.getValue(0, 0));
		assertEquals(BigInteger.valueOf(2),a.getValue(0, 1));
		assertEquals(BigInteger.valueOf(3),a.getValue(1, 0));
		assertEquals(BigInteger.valueOf(4),a.getValue(1, 1));
		assertEquals(BigInteger.valueOf(5),a.getValue(2, 0));
		assertEquals(BigInteger.valueOf(6),a.getValue(2, 1));
	}
	
	@Test
	public void testEquals2x2() {
		Matrix a = new Matrix(new int[]{3,2,1,2,3,4,5,6});
		Matrix b = new Matrix(new int[]{3,2,1,2,3,4,5,6});
		assertEquals(a,b);
	}	
	
	@Test
	public void testNotEquals2x2() {
		Matrix a = new Matrix(new int[]{3,2,1,2,3,4,5,6});
		Matrix b = new Matrix(new int[]{3,2,1,2,3,3,5,6});
		assertNotEquals(a,b);
	}
	
	@Test
	public void testEquals2x3() {
		Matrix a = new Matrix(new int[]{2,3,1,2,3,4,5,6});
		Matrix b = new Matrix(new int[]{2,3,1,2,3,4,5,9});
		assertNotEquals(a,b);
	}
	
	@Test
	public void testEquals3x2() {
		Matrix a = new Matrix(new int[]{3,2,1,2,3,4,5,6});
		Matrix b = new Matrix(new int[]{3,2,1,8,3,4,5,6});
		assertNotEquals(a,b);
	}
	
	@Test
	public void testMultiply() {
		Matrix a = new Matrix(new int[]{2,2,2,5,3,7});
		Matrix b = new Matrix(new int[]{2,2,33,15,77,2});
		Matrix expected = new Matrix(new int[]{2,2,451,40,638,59});
		
		Matrix ab = Matrix.multiply(a, b);
		
		assertEquals(expected,ab);
	}
	
}
