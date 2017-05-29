/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.aplic.servlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;



/**
 *
 * @author juno
 */
public class LibClassLoader extends URLClassLoader {
	
	
	protected LibClassLoader(URL[] urls) {
		super(urls, LibClassLoader.class.getClassLoader());
	}
	
	
	protected LibClassLoader(Path ... paths) {
		this(path2url(paths));
	}
	
	
	public LibClassLoader() {
		this(Paths.get("./../lib/disecLib.jar"));
	}
	
	
	private static URL[] path2url(Path ... paths) {
		if(paths == null || paths.length < 1) {
			return null;
		}
		URL[] urls = new URL[paths.length];
		for(int i = 0; i < paths.length; i++) {
			urls[i] = toURL(paths[i]);
		}
		return urls;
	}
	
	
	private static URL toURL(Path p) {
		if(p == null) return null;
		try {
			return p.toUri().toURL();
		} catch(MalformedURLException e) {
			return null;
		}
	}
	
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> cls = null;
		try {	cls = findClass(name); } 
		catch(ClassNotFoundException e) {
			System.out.println("  ### Error loading class: "+ name);
		}
		if(cls == null) {
			cls = super.loadClass(name);
		}
		if(cls != null) {
			this.resolveClass(cls);
		}
		return cls;
	}
	
	
	public LibClassLoader load(String name) throws ClassNotFoundException {
		System.out.println(" *** LibClassLoader.load: "+ name);
		this.loadClass(name);
		return this;
	}
	
}
