package br.com.bb.dinop.arqRedeDinop.util;

public class ConvertArrays {
	
	public static int[] convertStringArraytoIntArray(String[] sarray) throws Exception {
		if (sarray != null) {
			int intarray[] = new int[sarray.length];
			for (int i = 0; i < sarray.length; i++) {
				intarray[i] = Integer.parseInt(sarray[i]);
			}
		return intarray;
		}
	return null;
	}
}
