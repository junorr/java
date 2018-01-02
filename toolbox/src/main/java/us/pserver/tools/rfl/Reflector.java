package us.pserver.tools.rfl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import us.pserver.tools.NotMatch;

/**
 * Reflection utils
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public class Reflector {
  
	private final Class cls;
	
	private Object obj;
	
	private Method mth;
	
	private Field fld;
	
	private Constructor cct;
	
	
	public Reflector(Object obj) {
		if(obj == null) {
			throw new IllegalArgumentException(
					"Object must be not null"
			);
		}
    if(obj instanceof Class) {
      this.cls = (Class) obj;
      this.obj = null;
    }
    else {
      this.cls = obj.getClass();
      this.obj = obj;
    }
	}
	
	
	public static Reflector of(Class cls) {
		return new Reflector(cls);
	}
	
	
	public static Reflector of(Object obj) {
		return new Reflector(obj);
	}
  
  
  public Object getTarget() {
    return (obj == null ? cls : obj);
  }
	
	
	/**
	 * Procura pelo construtor da classe com os
	 * argumentos especificados 
	 * (<code>null</code> quando sem argumentos).
	 * @param args Argumentos do construtor
	 * (<code>null</code> quando sem argumentos).
	 * @return A instância de Reflector modificada.
	 */
	public Reflector selectConstructor(Class ... args) {
  	if(cls != null) {
    	try {
      	cct = cls.getDeclaredConstructor(args);
        if(cct == null) cct = cls.getConstructor(args);
			} catch(NoSuchMethodException | SecurityException ex) {
  			throw new ReflectorException(ex);
      }
		}
    return this;
	}
	
	
	/**
	 * Procura pelo construtor sem argumentos.
	 * @return A instância de Reflector modificada.
	 */
	public Reflector selectConstructor() {
    return Reflector.this.selectConstructor((Class[])null);
	}
	
	
	private void guessConstructor(Object ... args) {
		Optional<Constructor> opt = Arrays.asList(this.constructors())
				.stream()
				.filter(c->c.getParameterCount() == args.length)
				.filter(c->{
					return Arrays.asList(c.getParameters())
							.stream()
							.allMatch(p->Arrays.asList(args)
									.stream()
									.anyMatch(o->p.getType().equals(o.getClass())));
				}
		).findFirst();
		cct = opt.orElse(null);
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
		if(cls == null)	return null;
		if(cct == null) guessConstructor(args);
		if(cct == null) return null;
   	try {
     	if(!cct.isAccessible())
       	cct.setAccessible(true);
 			obj = cct.newInstance(args);
      return obj;
   	} catch(Exception ex) {
     	throw new ReflectorException(ex);
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
    
     if(cct == null)
      this.selectConstructor();
   	try {
     	if(!cct.isAccessible())
       	cct.setAccessible(true);
 			obj = cct.newInstance((Object[]) null);
      return obj;
   	} catch(Exception ex) {
     	throw new ReflectorException(ex);
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
 		if(mth == null) throw new ReflectorException("No method selected");
    if(args == null) 
      args = new Object[0];
		try {
  		if(!mth.isAccessible())
    		mth.setAccessible(true);
      return mth.invoke(obj, args);
		} catch(Exception ex) {
  		throw new ReflectorException(ex);
		}
	}
	
	
	/**
	 * Invoca o método sem argumentos..
	 * @return O objeto de retorno do método invocado.
	 */
	public Object invoke() {
    return invoke((Object[]) null);
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
	public Reflector selectMethod(String method, Class ... args) {
    if(args == null) return Reflector.this.selectMethod(method);
    if(cls != null && method != null) {
      try {
        mth = cls.getDeclaredMethod(method, args);
        if(mth == null) mth = cls.getMethod(method, args);
			} catch(NoSuchMethodException ex) {
  			throw new ReflectorException(ex);
      }
		}
		return this;
	}
	
	
	/**
	 * Procura pelo m�todo especificado.
	 * @param meth M�todo a ser selecionado.
	 * @return Esta inst�ncia modificada de Reflector.
	 */
	public Reflector selectMethod(Method meth) {
    if(meth != null) {
      Optional<Method> mop = Arrays.asList(this.methods())
          .stream()
          .filter(m->m.equals(meth))
          .findFirst();
      this.mth = mop.orElse(null);
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
	public Reflector selectMethod(String method) {
    if(method == null || method.isEmpty()) {
      throw new IllegalArgumentException("Bad empty Method name");
    }
  	if(cls != null) {
      mth = null;
      Method[] meths = methods();
      for(Method m : meths) {
        if(m.getName().equals(method)
            && m.getParameterTypes().length == 0)
          mth = m;
      }
      if(mth == null) {
        for(Method m : meths) {
          if(m.getName().equals(method))
            mth = m;
        }
      }
  	}
		return this;
	}
  
  
  public Method[] methods() {
    Method[] dec = cls.getDeclaredMethods();
    Method[] pub = cls.getMethods();
    List<Method> all = new ArrayList<>();
    Arrays.asList(dec).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Arrays.asList(pub).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Method[] mts = new Method[all.size()];
    return all.toArray(mts);
  }
	
	
  public Field[] fields() {
    Field[] dec = cls.getDeclaredFields();
    Field[] pub = cls.getFields();
    List<Field> all = new ArrayList<>();
    Arrays.asList(dec).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Arrays.asList(pub).stream().filter(
        m->!all.contains(m)).forEach(all::add);
    Field[] mts = new Field[all.size()];
    return all.toArray(mts);
  }
	
	
  public Constructor[] constructors() {
    Constructor[] dec = cls.getDeclaredConstructors();
    Constructor[] pub = cls.getConstructors();
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
	public Reflector selectField(String field) {
    if(field == null || field.isEmpty()) {
      throw new IllegalArgumentException("Bad empty Field name");
    }
    try {
      fld = cls.getDeclaredField(field);
      if(fld == null) cls.getField(field);
    } catch(Exception ex) {
      throw new ReflectorException(ex);
    }
		return this;
	}
  
  
  /**
   * Retorna todos os campos da classe/objeto.
   * @return Array com todos os campos da classe/objeto.
   */
  public String[] fieldNames() {
  	if(cls != null) {
    	try {
      	Field[] fields = fields();
        String[] names = new String[fields.length];
        for(int i = 0; i < fields.length; i++) {
          names[i] = fields[i].getName();
        }
        return names;
			} catch(Exception ex) {
  			throw new ReflectorException(ex);
    	}
    }
		return null;
  }
	
	
	/**
	 * Define o valor do campo (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>) com o valor
	 * informado.
	 * @param value Novo a ser definido para o campo.
   * @return 
	 */
	public Reflector set(Object value) {
		if(fld == null) throw new ReflectorException("No field selected");
  	try {
    	if(!fld.isAccessible())
      	fld.setAccessible(true);
			fld.set(obj, value);
  	} catch(Exception ex) {
    	throw new ReflectorException(ex.toString(), ex);
		}
    return this;
	}
	
	
	/**
	 * Retorna o valor contido no campo 
	 * (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>).
	 * @return Valor contido no campo.
	 */
	public Object get() {
    NotMatch.notNull(fld).failIfNotMatch("Field not selected");
		try {
  		if(!fld.isAccessible())
    		fld.setAccessible(true);
      if(Modifier.isStatic(fld.getModifiers())) {
        return fld.get(null);
      }
      else {
        NotMatch.notNull(obj).failIfNotMatch("Object field owner not selected");
        return fld.get(obj);
      }
		} catch(Exception ex) {
  		throw new ReflectorException(ex);
		}
	}
	
	
  public boolean isFieldPresent() {
    if(cls == null || fld == null) 
      return false;
    Field[] fs = fields();
    for(int i = 0; i < fs.length; i++) {
      if(fs[i].equals(fld))
        return true;
    }
    return false;
  }
	
	
  public boolean isMethodPresent() {
    if(cls == null || mth == null) 
      return false;
    Method[] ms = methods();
    for(int i = 0; i < ms.length; i++) {
      if(ms[i].equals(mth))
        return true;
    }
    return false;
  }
	
	
  public boolean isConstructorPresent() {
    if(cls == null || cct == null) 
      return false;
    Constructor[] cs = constructors();
    for(int i = 0; i < cs.length; i++) {
      if(cs[i].equals(cct))
        return true;
    }
    return false;
  }
  
  
  public Field field() {
    return fld;
  }
  
  
  public Method method() {
    return mth;
  }
  
  
  public Constructor constructor() {
    return cct;
  }
	
}
