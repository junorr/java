package us.pserver.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reflection utils
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public class Reflector {
  
  private static final Object LOCK = new Object();
	
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
    synchronized(LOCK) {
      cls = o.getClass();
      obj = o;
    }
		return this;
	}
	
	
	/**
	 * Class a ser trabalhada por Reflector
	 * @param o Class
	 * @return A instância de Reflector modificada.
	 */
	public Reflector on(Class c) {
    synchronized(LOCK) {
  		cls = c;
    }
		return this;
	}
  
  
  public Reflector onClass(String className) {
    synchronized(LOCK) {
      clearExc();
  		try {
        cls = Class.forName(className);
      } catch(Exception e) {
        e.printStackTrace();
        exc = e;
        cls = null;
      }
    }
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
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null) {
      	try {
        	cct = cls.getDeclaredConstructor(args);
          if(cct == null) cct = cls.getConstructor(args);
  			} catch(Exception ex) {
    			exc = ex;
          cct = null;
        }
  		}
    }
		return this;
	}
	
	
	/**
	 * Procura pelo construtor sem argumentos.
	 * @return A instância de Reflector modificada.
	 */
	public Reflector constructor() {
    return constructor((Class[])null);
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
    if(args == null) return create();
    synchronized(LOCK) {
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
	}
	
	
	/**
	 * Cria uma nova instância da classe (<code>on(Class)</code>),
	 * chamando o construtor padrão sem argumentos.
	 * Caso não exista construtor sem argumentos, 
   * retorna <code>null</code>;
	 * @return A nova instância criada da classe.
	 */
	public Object create() {
    synchronized(LOCK) {
  		if(cls == null)
    		return null;
    
      if(cct == null)
        this.constructor();
    	try {
      	if(!cct.isAccessible())
        	cct.setAccessible(true);
  			return cct.newInstance((Object[]) null);
    	} catch(Exception ex) {
      	exc = ex;
        return null;
  		}
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
    synchronized(LOCK) {
  		if(mth == null || obj == null)
    		return null;

      if(args == null) 
        args = new Object[0];
    
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
	}
	
	
	/**
	 * Invoca o método sem argumentos..
	 * @return O objeto de retorno do método invocado.
	 */
	public Object invoke() {
    return invoke((Object[]) null);
	}
	
	
	private void clearExc() {
    synchronized(LOCK) {
  		exc = null;
    }
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
    synchronized(LOCK) {
      if(args == null) return method(method);
    	clearExc();
      if(cls != null && method != null) {
        try {
          mth = cls.getDeclaredMethod(method, args);
          if(mth == null) mth = cls.getMethod(method, args);
  			} catch(NoSuchMethodException ex) {
    			exc = ex;
          mth = null;
        }
  		}
    }
		return this;
	}
	
	
	/**
	 * Procura pelo método da classe
	 * (<code>on(Class)</code>), com o nome
	 * e argumentos informados
	 * (<code>null</code> quando sem argumentos).
	 * @param method Nome do método.
	 * @return A instância de Reflector modificada.
	 */
	public Reflector method(String method) {
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null && method != null) {
        mth = null;
        Method[] meths = getAllMethods(cls);
        for(Method m : meths) {
          if(m.getName().equals(method)
              && m.getParameterTypes().length == 0)
            mth = m;
        }
        if(mth == null)
          for(Method m : meths) {
            if(m.getName().equals(method))
              mth = m;
          }
    	}
    }
		return this;
	}
  
  
  public static Method[] getAllMethods(Class c) {
    if(c == null) return null;
    Method[] dec = c.getDeclaredMethods();
    Method[] pub = c.getMethods();
    List<Method> all = new ArrayList<>();
    Arrays.asList(dec).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Arrays.asList(pub).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Method[] mts = new Method[all.size()];
    return all.toArray(mts);
  }
	
	
  public static Field[] getAllFields(Class c) {
    if(c == null) return null;
    Field[] dec = c.getDeclaredFields();
    Field[] pub = c.getFields();
    List<Field> all = new ArrayList<>();
    Arrays.asList(dec).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Arrays.asList(pub).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Field[] mts = new Field[all.size()];
    return all.toArray(mts);
  }
	
	
  public static Constructor[] getAllConstructors(Class c) {
    if(c == null) return null;
    Constructor[] dec = c.getDeclaredConstructors();
    Constructor[] pub = c.getConstructors();
    List<Constructor> all = new ArrayList<>();
    Arrays.asList(dec).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Arrays.asList(pub).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Constructor[] mts = new Constructor[all.size()];
    return all.toArray(mts);
  }
	
	
	/**
	 * Procura pela propriedade da classe
	 * (<code>on(Class)</code>) com o nome 
	 * informado.
	 * @param field Nome do campo.
	 * @return A instância de Reflector modificada.
	 */
	public Reflector field(String field) {
    synchronized(LOCK) {
    	clearExc();
  		if(field != null)
      	try {
        	fld = cls.getDeclaredField(field);
          if(fld == null) cls.getField(field);
  			} catch(Exception ex) {
    			exc = ex;
          fld = null;
        }
    }
		return this;
	}
  
  
  /**
   * Retorna todos os campos da classe/objeto.
   * @return Array com todos os campos da classe/objeto.
   */
  public String[] fieldNames() {
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null)
      	try {
        	Field[] fields = getAllFields(cls);
          String[] names = new String[fields.length];
          for(int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
          }
          return names;
  			} catch(Exception ex) {
    			exc = ex;
      	}
    }
		return null;
  }
	
	
  /**
   * Retorna todos os campos da classe/objeto.
   * @return Array com todos os campos da classe/objeto.
   */
  public Field[] fields() {
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null)
      	try {
        	return getAllFields(cls);
  			} catch(Exception ex) {
    			exc = ex;
      	}
    }
		return null;
  }
	
	
  /**
   * Retorna todos os métodos da classe/objeto.
   * @return Array com todos os métodos da classe/objeto.
   */
  public Method[] methods() {
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null)
      	try {
        	return getAllMethods(cls);
  			} catch(Exception ex) {
    			exc = ex;
      	}
    }
		return null;
  }
	
	
  /**
   * Retorna todos os construtores da classe/objeto.
   * @return Array com todos os construtores da classe/objeto.
   */
  public Constructor[] getConstructors() {
    synchronized(LOCK) {
  		clearExc();
    	if(cls != null)
      	try {
        	return getAllConstructors(cls);
  			} catch(Exception ex) {
    			exc = ex;
      	}
    }
		return null;
  }
	
	
	/**
	 * Define o valor do campo (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>) com o valor
	 * informado.
	 * @param value Novo a ser definido para o campo.
	 */
	public void set(Object value) {
    synchronized(LOCK) {
  		if(fld == null || obj == null) 
        return;
		
  		clearExc();
    	try {
      	if(!fld.isAccessible())
        	fld.setAccessible(true);
  			fld.set(obj, value);
    	} catch(Exception ex) {
      	exc = ex;
  		}
    }
	}
	
	
	/**
	 * Retorna o valor contido no campo 
	 * (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>).
	 * @return Valor contido no campo.
	 */
	public Object get() {
    synchronized(LOCK) {
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
	}
	
	
	/**
	 * Retorna <code>true</code> caso tenha
	 * ocorrido erro em algum método, 
	 * <code>false</code> caso contrário.
	 */
	public boolean hasError() {
    synchronized(LOCK) {
  		return exc != null;
    }
	}
  
  
  public boolean isFieldPresent() {
    synchronized(LOCK) {
      if(cls == null || fld == null) 
        return false;
    
      Field[] fs = getAllFields(cls);
      for(int i = 0; i < fs.length; i++) {
        if(fs[i].equals(fld))
          return true;
      }
    }
    return false;
  }
	
	
  public boolean isMethodPresent() {
    synchronized(LOCK) {
      if(cls == null || mth == null) 
        return false;
    
      Method[] ms = getAllMethods(cls);
      for(int i = 0; i < ms.length; i++) {
        if(ms[i].equals(mth))
          return true;
      }
    }
    return false;
  }
	
	
  public boolean isConstructorPresent() {
    synchronized(LOCK) {
      if(cls == null || cct == null) 
        return false;
    
      Constructor[] cs = getAllConstructors(cls);
      for(int i = 0; i < cs.length; i++) {
        if(cs[i].equals(cct))
          return true;
      }
    }
    return false;
  }
  
  
  public Field field() {
    return fld;
  }
  
  
  public Method method() {
    return mth;
  }
  
  
  public Constructor getConstructor() {
    return cct;
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
		A a = (A) rfl.on(A.class).constructor().create();
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
    System.out.println("isMethodPresent(toString)? "+ rfl.on(a).method("toString").isMethodPresent());
    System.out.println("Invoking 'a.toString()'");
		System.out.println(rfl.on(a).method("toString").invoke());
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println("Setting PRIVATE field 'f = 5'...");
		rfl.on(a).field("f").set(5);
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
    System.out.println("Invoking 'a.toString()'");
		System.out.println(rfl.on(a).method("toString").invoke());
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
		System.out.println("Invoking PRIVATE method 'm(7)'...");
		rfl.on(a).method("m", int.class).invoke(7);
		if(rfl.hasError())
			rfl.getError().printStackTrace();
		
    System.out.println("Invoking 'a.toString()'");
		System.out.println(rfl.on(a).method("toString").invoke());
		if(rfl.hasError())
			rfl.getError().printStackTrace();
	}

}
