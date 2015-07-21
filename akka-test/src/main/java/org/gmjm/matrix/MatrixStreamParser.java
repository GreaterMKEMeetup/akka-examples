package org.gmjm.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


public class MatrixStreamParser {
	
	public static List<Matrix> parseMatriciesStream(InputStream is) throws IOException {		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		List<Matrix> matricies = br.lines()
		.map(line -> {
			String[] values = line.split(",");
			BigInteger[] intValues = new BigInteger[values.length];
			
			for(int i = 0; i < values.length; i++)
				intValues[i] = new BigInteger(values[i]);
			
			return intValues;})
		.map(intArr -> new Matrix(intArr))
		.collect(Collectors.toList());	
		
		return matricies;
	}
	
}
