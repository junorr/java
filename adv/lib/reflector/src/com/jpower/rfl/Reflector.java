package com.jpower.rfl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflection utils
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public class Reflector {
	
	private Class cls;
	
	private Object obj;
	
	private Throwable exc;
	
	private Method mth;
	
	private Field fld;
	
	private Constructor cct;
	
	
	/**
	 * Construtor padrão.
	 */
	public Reflector() {
		cls = null;
		obj = null;
		exc = null;
		mth = null;
		cct = null;
	}
	
	
	/**
	 * Objeto a ser trabalhado por Reflector
	 * @param o Object
	 * @return A instância de Reflector modificada.
	 */
	public Reflector on(Object o) {
		cls = o.getClass();
		obj = o;
		return this;
	}
	
	
	/**
	 * Class a ser trabalhada por Reflector
	 * @param o Class
	 * @return A instância de Reflector modificada.
	 */
	public Reflector on(Class c) {
		cls = c;
		return this;
	}
	
	
	/**
	 * Procura pelo construtor da classe com os
	 * argumentos especificados 
	 * (<code>null</code> quando sem argumentos).
	 * @param args Argumentos do construtor
	 * (<code>null</code> quando sem argumentos).
	 * @return A instância de Reflector modificada.
	 */
	public Reflector constructor(Class ... args) {
		clearExc();
		if(cls != null) {
			try {
				cct = cls.getDeclaredConstructor(args);
			} catch(Exception ex) {
				exc = ex;
			}
		}
		return this;
	}
	
	
	/**
	 * Cria uma nova instância da classe (<code>on(Class)</code>),
	 * chamando o construtor (<code>constructor(Class[])</code>)
	 * com os argumentos informados (<code>null</code> quando
	 * sem argumentos).
	 * @param args Argumentos do construtor
	 * (<code>null</code> quando sem argumentos).
	 * @return A nova instância criada da classe.
	 */
	public Object create(Object ... args) {
		if(cls == null || cct == null)
			return null;
		
		clearExc();
		try {
			if(!cct.isAccessible())
				cct.setAccessible(true);
			return cct.newInstance(args);
		} catch(Exception ex) {
			exc = ex;
			return null;
		}
	}
	
	
	/**
	 * Cria uma nova instância da classe (<code>on(Class)</code>),
	 * chamando o construtor padrão sem argumentos.
	 * Caso não exista construtor sem argumentos, 
   * retorna <code>null</code>;
	 * @return A nova instância criada da classe.
	 */
	public Object create() {
		if(cls == null)
			return null;
    
    this.constructor(null);
		
		try {
			if(!cct.isAccessible())
				cct.setAccessible(true);
			return cct.newInstance(null);
		} catch(Exception ex) {
			exc = ex;
			return null;
		}
	}
	
	
	/**
	 * Invoca o método (<code>method(String, Class[])</code>) 
	 * do objeto (<code>on(Object)</code>) com os argumentos
	 * informados (<code>null</code> quando sem argumentos).
	 * @param args Argumentos para invocar o método
	 * (<code>null</code> quando sem argumentos).
	 * @return O objeto de retorno do método invocado.
	 */
	public Object invoke(Object ... args) {
		if(mth == null || obj == null)
			return null;

		clearExc();
		try {
			if(!mth.isAccessible())
				mth.setAccessible(true);
			return mth.invoke(obj, args);
		} catch(Exception ex) {
			exc = ex;
			return null;
		}
	}
	
	
	private void clearExc() {
		exc = null;
	}
	
	
	/**
	 * Procura pelo método da classe
	 * (<code>on(Class)</code>), com o nome
	 * e argumentos informados
	 * (<code>null</code> quando sem argumentos).
	 * @param method Nome do método.
	 * @param args Classes dos argumentos do método
	 * (<code>null</code> quando sem argumentos).
	 * @return A instância de Reflector modificada.
	 */
	public Reflector method(String method, Class ... args) {
		clearExc();
		if(cls != null && method != null) {
			try {
				mth = cls.getDeclaredMethod(method, args);
			} catch(NoSuchMethodException ex) {
				exc = ex;
			}
		}
		return this;
	}
	
	
	/**
	 * Procura pela propriedade da classe
	 * (<code>on(Class)</code>) com o nome 
	 * informado.
	 * @param field Nome do campo.
	 * @return A instância de Reflector modificada.
	 */
	public Reflector field(String field) {
		clearExc();
		if(field != null)
			try {
				fld = cls.getDeclaredField(field);
			} catch(Exception ex) {
				exc = ex;
			}
		return this;
	}
	
	
	/**
	 * Define o valor do campo (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>) com o valor
	 * informado.
	 * @param value Novo a ser definido para o campo.
	 */
	public void set(Object value) {
		if(fld == null || value == null
				|| obj == null) return;
		
		clearExc();
		try {
			if(!fld.isAccessible())
				fld.setAccessible(true);
			fld.set(obj, value);
		} catch(Exception ex) {
			exc = ex;
		}
	}
	
	
	/**
	 * Retorna o valor contido no campo 
	 * (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>).
	 * @return Valor contido no campo.
	 */
	public Object get() {
		clearExc();
		if(obj == null || fld == null) 
			return null;
		
		try {
			if(!fld.isAccessible())
				fld.setAccessible(true);
			return fld.get(obj);
		} catch(Exception ex) {
			exc = ex;
			return null;
		}
	}
	
	
	/**
	 * Retorna <code>true</code> caso tenha
	 * ocorrido erro em algum método, 
	 * <code>false</code> caso contrário.
	 */
	public boolean hasError() {
		return exc != null;
	}
	
	
	/**
	 * Retorna o erro ocorrido durante a
	 * chamada de algum método, ou 
	 * <code>null</code> caso não tenha ocorrido
	 * erro.
	 */
	public Throwable getError() {
		return exc;
	}
	
	
	public static void main(String[] args) {
		class A {
			private int f;
			protected A() {
				m(0);
			}
			public A(int v) {
				m(v);
			}
			public int m() {
				return f;
			}
			private void m(int v) {
				f = v;
			}
			@Override
			public String toString() {
				return "A: "+f;
			}
		}
		
		Reflector rfl = new Reflector();

		System.out.println("Creating new A with PROTECTED constructor 'A()'...");
		A a = (A) rfl.on(A.class).constructor(null).create(null);
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println(rfl.on(a).method("toString", null).invoke(null));
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println("Setting PRIVATE field 'f = 5'...");
		rfl.on(a).field("f").set(5);
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println(rfl.on(a).method("toString", null).invoke(null));
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println("Invoking PRIVATE method 'm(7)'...");
		rfl.on(a).method("m", int.class).invoke(7);
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println(rfl.on(a).method("toString", null).invoke(null));
		if(rfl.hasError())
			rfl.getError().printStackTrace();
	}

}
