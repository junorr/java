/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
	
	public static final String RGX_LENGTH = "\\{(\\d+)\\}";
	
	public static final String RGX_DATE = "\\{([y|M|d|H|h|m|s|S|\\-|/|:|,|\\.|\\s]+)\\}";
	
	
	private String pattern;
	
	private SimpleDateFormat datef;
	
	
	public PatternFormatter() {
		pattern = LogManager.getLogManager()
				.getProperty(this.getClass().getName());
	}
	
	
	public PatternFormatter(String pattern) {
		if(pattern == null || pattern.trim().isEmpty())
			throw new IllegalArgumentException("Invalid pattern: '"+ pattern+ "'");
		this.pattern = pattern;
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
	
	
	private String formatLen(String str, int len) {
		if(str == null 
				|| str.length() <= len
				|| len <= 0) {
			return str;
		}
		return str.substring(str.length() - len);
	}
	
	
	private String replaceLen(String key, String str, String repl) {
		if(str == null || str.length() == 0)
			return str;
		if(key == null || key.length() == 0)
			return str;
		try {
			Matcher mt = Pattern.compile(
					key + RGX_LENGTH
			).matcher(str);
			if(mt.find()) {
				int len = Integer.parseInt(mt.group(1));
				str = str.replace(mt.group(), formatLen(repl, len));
			} else {
				str = str.replace(key, repl);
			}
		} catch(Exception e) {
			throw new IllegalStateException(
					"Invalid format pattern for '"+ key
							+ "' in '"+ str+ "'", e
			);
		}
		return str;
	}
			
			
	private String replaceClass(String str, String repl) {
		if(str == null || str.length() == 0)
			return str;
		if(repl == null || repl.length() == 0)
			return str;
		try {
			Matcher mt = Pattern.compile(
					KEY_CLASS + RGX_LENGTH
			).matcher(str);
			if(mt.find()) {
				int len = Integer.parseInt(mt.group(1));
				String[] cls = repl.split("\\.");
				String sc = "";
				for(int i = cls.length - 1; i >= cls.length - len - 1; i--) {
					sc = "." + cls[i] + sc;
				}
				str = str.replace(mt.group(), sc);
			} else {
				str = str.replace(KEY_CLASS, repl);
			}
		} catch(Exception e) {
			throw new IllegalStateException(
					"Invalid format pattern for '"+ KEY_CLASS
							+ "' in '"+ str+ "'", e
			);
		}
		return str;
	}
			
			
	private String replaceDate(String key, String str, Date date) {
		if(str == null || str.length() == 0)
			return str;
		if(key == null || key.length() == 0)
			return str;
		try {
			Matcher mt = Pattern.compile(
					key + RGX_DATE
			).matcher(str);
			if(mt.find()) {
				datef = new SimpleDateFormat(mt.group(1));
				str = str.replace(mt.group(), datef.format(date));
			} else {
				datef = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
				str = str.replace(key, datef.format(date));
			}
		} catch(Exception e) {
			throw new IllegalStateException(
					"Invalid format pattern for '"+ key
							+ "' in '"+ str+ "'"
			);
		}
		return str;
	}
	
	
	private String replaceThread(String str, int id) {
		if(str == null || str.length() == 0)
			return str;
		if(id <= 0) return str;
		Optional<Thread> opt = Thread.getAllStackTraces()
				.keySet().stream().filter(
						t->t.getId() == id
				).findFirst();
		if(opt.isPresent()) {
			str = str.replace(KEY_THREAD, opt.get().getName());
		}
		return str;
	}
	
	
	private String appendThrown(Throwable th, String str) {
		if(th == null || str == null) return str;
		StackTraceElement[] elts = th.getStackTrace();
		str += "\n" + th.getMessage();
		for(StackTraceElement e : elts) {
			str += "\n  " + e.toString();
		}
		return str;
	}
			
			
	@Override
	public String format(LogRecord rec) {
		String format = pattern;
		if(format.contains(KEY_LEVEL)) {
			format = format.replace(KEY_LEVEL, 
					level(rec.getLevel())
			);
		}
		if(format.contains(KEY_LOG_NAME)) {
			format = replaceLen(KEY_LOG_NAME, 
					format, rec.getLoggerName()
			);
		}
		if(format.contains(KEY_MSG)) {
			format = replaceLen(KEY_MSG, 
					format, String.format(rec.getMessage(), 
							rec.getParameters())
			);
		}
		if(format.contains(KEY_DATE)) {
			format = replaceDate(KEY_DATE, 
					format, new Date(rec.getMillis())
			);
		}
		if(format.contains(KEY_CLASS)) {
			format = replaceClass( 
					format, rec.getSourceClassName()
			);
		}
		if(format.contains(KEY_METHOD)) {
			format = replaceLen(KEY_METHOD, 
					format, rec.getSourceMethodName()
			);
		}
		if(format.contains(KEY_THREAD)) {
			format = replaceThread(format, rec.getThreadID());
		}
		if(rec.getThrown() != null) {
			format = appendThrown(rec.getThrown().getCause(), format);
			format = appendThrown(rec.getThrown(), format);
		}
		return format;
	}
	
	
	public static void main(String[] args) {
		LogRecord rec = new LogRecord(Level.FINE, "This is a log message");
		rec.setMillis(System.currentTimeMillis());
		rec.setSourceClassName(PatternFormatter.class.getName());
		rec.setSourceMethodName("format");
		rec.setThreadID((int) Thread.currentThread().getId());
		PatternFormatter pf = new PatternFormatter();
		System.out.println(pf.format(rec));
	}
	
}
