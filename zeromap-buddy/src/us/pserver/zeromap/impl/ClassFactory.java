/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;


/**
 *
 * @author juno
 */
public abstract class ClassFactory {
	
	public static Class create(String name) {
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException(
					"Class name must be not null"
			);
		if(name.contains("|")) {
			name = name.substring(name.indexOf("|")+1);
		}
		Class cls = null;
		if(byte.class.getName().equals(name)) {
			cls = byte.class;
		}
		else if(char.class.getName().equals(name)) {
			cls = char.class;
		}
		else if(boolean.class.getName().equals(name)) {
			cls = boolean.class;
		}
		else if(short.class.getName().equals(name)) {
			cls = short.class;
		}
		else if(int.class.getName().equals(name)) {
			cls = int.class;
		}
		else if(long.class.getName().equals(name)) {
			cls = long.class;
		}
		else if(float.class.getName().equals(name)) {
			cls = float.class;
		}
		else if(double.class.getName().equals(name)) {
			cls = double.class;
		}
		else if(byte[].class.getName().equals(name)) {
			cls = byte[].class;
		}
		else if(char[].class.getName().equals(name)) {
			cls = char[].class;
		}
		else if(boolean[].class.getName().equals(name)) {
			cls = boolean[].class;
		}
		else if(short[].class.getName().equals(name)) {
			cls = short[].class;
		}
		else if(int[].class.getName().equals(name)) {
			cls = int[].class;
		}
		else if(long[].class.getName().equals(name)) {
			cls = long[].class;
		}
		else if(float[].class.getName().equals(name)) {
			cls = float[].class;
		}
		else if(double[].class.getName().equals(name)) {
			cls = double[].class;
		}
		else {
			try {
				cls = Class.forName(name);
			} catch(ClassNotFoundException e) {
				throw new IllegalArgumentException("Unknown class name: "+ name, e);
			}
		}
		return cls;
	}
	
}
