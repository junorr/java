/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.misc;


/**
 *
 * @author juno
 */
public class Sleeper {
	
	private boolean success;
	
	private InterruptedException error;
	
	private final long millis;
	
	
	public Sleeper(long millis) {
		success = true;
		error = null;
		this.millis = millis;
	}
	
	
	public static Sleeper of(long millis) {
		return new Sleeper(millis);
	}
	
	
	public boolean isSuccessful() {
		return success;
	}
	
	
	public InterruptedException getException() {
		return error;
	}
	
	
	public long getTimeMillis() {
		return millis;
	}
	
	
	public boolean sleep() {
		if(millis < 1) return true;
		try {
			Thread.sleep(millis);
			success = true;
		} catch(InterruptedException e) {
			error = e;
			success = false;
		}
		return success;
	}
	
}
