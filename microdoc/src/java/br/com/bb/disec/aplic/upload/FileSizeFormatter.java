package br.com.bb.disec.aplic.upload;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FileSizeFormatter {
	
	private static final double[] sizes = new double[]{1, 1024, 1024*1024, 1024*1024*1024};
	
	private static final String[] units = new String[]{" Bytes", " KB", " MB", " GB"};
	
	private final DecimalFormat fmt;
	
	
	public FileSizeFormatter() {
		fmt = new DecimalFormat("#,##0.00");
		DecimalFormatSymbols ds = fmt.getDecimalFormatSymbols();
		ds.setDecimalSeparator(',');
		ds.setGroupingSeparator('.');
		fmt.setDecimalFormatSymbols(ds);
	}
	
	
	public String format(long size) {
		for(int i = 0; i < sizes.length -1; i++) {
			if(size <= sizes[i+1]) {
				return fmt.format(size/sizes[i])+ units[i];
			}
		}
		return fmt.format(size/sizes[sizes.length-1])+ units[units.length-1];
	}
	
}
