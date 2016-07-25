/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.log;

import java.io.PrintStream;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;


/**
 *
 * @author juno
 */
public class StandardConsoleHandler extends Handler {
	
	public static final String PROP_LEVEL = ".level";
	
	public static final String PROP_FILTER = ".filter";
	
	public static final String PROP_FORMATTER = ".formatter";
	
	public static final String PROP_ENCODING = ".encoding";
	
	private int stderrAbove;
	
	private PrintStream out;
	
	
	public StandardConsoleHandler() {
		super();
		stderrAbove = 1000;
		out = null;
		LogManager man = LogManager.getLogManager();
		String cls = getClass().getName();
		String prop = man.getProperty(cls + PROP_LEVEL);
		if(prop != null) {
			this.setLevel(Level.parse(prop));
		}
		prop = man.getProperty(cls + PROP_FILTER);
		if(prop != null) {
			Class<Filter> clazz = classOf(prop);
			this.setFilter(create(clazz));
		}
		prop = man.getProperty(cls + PROP_FORMATTER);
		if(prop != null) {
			Class<Formatter> clazz = classOf(prop);
			this.setFormatter(create(clazz));
		}
		prop = man.getProperty(cls + PROP_ENCODING);
		if(prop != null) {
			Class<Formatter> clazz = classOf(prop);
			this.setFormatter(create(clazz));
		}
	}
	
	
	private<T> Class<T> classOf(String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private <T> T create(Class<T> cls) {
		if(cls == null) return null;
		try {
			return cls.newInstance();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public StandardConsoleHandler setStderrAbove(Level lv) {
		if(lv != null) {
			stderrAbove = lv.intValue();
		}
		return this;
	}
	
	
	@Override
	public void publish(LogRecord record) {
		out = System.out;
		if(record.getLevel().intValue() >= stderrAbove) {
			out = System.err;
		}
		out.println(getFormatter().format(record));
	}


	@Override
	public void flush() {
		out.flush();
	}


	@Override	public void close() {}
	
}
