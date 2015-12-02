/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;


/**
 *
 * @author juno
 */
public class PatternFormatter extends Formatter {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
			
	public static final String KEY_LEVEL = "%level";
			
	public static final String KEY_LOG_NAME = "%logname";
			
	public static final String KEY_MSG = "%msg";
			
	public static final String KEY_DATE = "%date";
			
	public static final String KEY_CLASS = "%class";
			
	public static final String KEY_METHOD = "%method";
			
	public static final String KEY_THREAD = "%thread";
	
	
	private String pattern;
	
	private SimpleDateFormat datef;
	
	
	public PatternFormatter(String pattern, String dateFormat) {
		if(pattern == null || pattern.trim().isEmpty())
			throw new IllegalArgumentException("Invalid pattern: '"+ pattern+ "'");
		if(dateFormat == null || dateFormat.trim().isEmpty()) 
			dateFormat = DEFAULT_DATE_FORMAT;
		this.pattern = pattern;
		this.datef = new SimpleDateFormat(dateFormat);
	}
	
	
	public PatternFormatter(String pattern) {
		this(pattern, null);
	}
	
	
	private String level(Level lv) {
		if(lv == null) return "<null>";
		switch(lv.intValue()) {
			case 1000: //SEVERE
				return "ERROR";
			case 900: //WARNING
				return "WARN ";
			case 800: //INFO
				return "INFO ";
			case 500: //FINE;
				return "DEBUG";
			case 700: //CONFIG
				return "CONF ";
			case 400: //FINER
				return "FINER";
			default:
				return lv.getName();
		}
	}
			
			
	@Override
	public String format(LogRecord rec) {
		String format = pattern;
		if(format.contains(KEY_LEVEL)) {
			format = format.replace(KEY_LEVEL, level(rec.getLevel()));
		}
		if(format.contains(KEY_LOG_NAME)) {
			format = format.replace(KEY_LOG_NAME, rec.getLoggerName());
		}
		if(format.contains(KEY_MSG)) {
			format = format.replace(KEY_MSG, String.format(
					rec.getMessage(), rec.getParameters())
			);
		}
		if(format.contains(KEY_DATE)) {
			format = format.replace(KEY_DATE, datef.format(
					new Date(rec.getMillis()))
			);
		}
		if(format.contains(KEY_CLASS)) {
			int ic = format.indexOf(KEY_CLASS) + KEY_CLASS.length();
			if(format.charAt(ic) == '{') {
				int ib = format.indexOf("}");
				String slim = format.substring(ic+1, ib);
				int limit = -1;
				try {
					limit = Integer.parseInt(slim);
				} catch(NumberFormatException e) {
					throw new IllegalStateException("Invalid format pattern at position #"+ ic+ ": {"+ slim+ "}");
				}
			}
			format = format.replace(KEY_CLASS, rec.getSourceClassName());
		}
		/*
			getLevel()
			getLoggerName()
			getMessage()
			getMillis()
			getParameters()
			getResourceBundle()
			getResourceBundleName()
			getSequenceNumber()
			getSourceClassName()
			getSourceMethodName()
			getThreadID()
			getThrown()		
		*/
	}
	
}
