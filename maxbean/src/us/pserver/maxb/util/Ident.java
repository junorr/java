/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.util;

import java.util.concurrent.atomic.AtomicInteger;



/**
 *
 * @author juno
 */
public class Ident {
	
	private final char ident;
	
	private final int start;
	
	private final AtomicInteger level;
	
	
	public Ident(char identChar, int startLevel) {
		this(identChar, startLevel, new AtomicInteger());
	}
	
	
	private Ident(char identChar, int startLevel, AtomicInteger level) {
		this.ident = identChar;
		this.start = startLevel;
		this.level = level;
	}
	
	
	public char getIdentChar() {
		return ident;
	}
	
	
	public int getStartLevel() {
		return start;
	}
	
	
	public int getLevel() {
		return level.get();
	}
	
	
	public Ident increment() {
		level.incrementAndGet();
		return this;
	}
	
	
	public Ident decrement() {
		level.decrementAndGet();
		return this;
	}
	
	
	@Override
	public Ident clone() {
		return new Ident(ident, start, level);
	}
	
	
	public String getIdentation() {
		StringBuilder sb = new StringBuilder();
		String si = repeat(String.valueOf(ident), start);
		return sb.append(si)
				.append(repeat(si, level.get()))
				.toString();
	}
	
	
	private String repeat(String s, int times) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < times; i++) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		return getIdentation();
	}
	
}
