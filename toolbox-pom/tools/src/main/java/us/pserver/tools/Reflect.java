package us.pserver.tools;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reflection utils
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public class Reflect<T> {
  
  public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
  
	private final Class<T> cls;
	
	private final T obj;
	
	private final Method mth;
	
	private final Field fld;
	
	private final Constructor cct;
  
  private final MethodHandles.Lookup lookup;


  public Reflect(Class<T> cls, T obj, Method mth, Field fld, Constructor cct, MethodHandles.Lookup lookup) {
    this.cls = cls;
    this.obj = obj;
    this.mth = mth;
    this.fld = fld;
    this.cct = cct;
    this.lookup = Objects.requireNonNull(lookup);
  }
	
	
	public Reflect(T obj, MethodHandles.Lookup lookup) {
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
      this.cls = (Class<T>) obj.getClass();
      this.obj = obj;
    }
    this.mth = null;
    this.fld = null;
    this.cct = null;
    this.lookup = Objects.requireNonNull(lookup);
	}
	
	
	public static <U> Reflect<U> of(Class<U> cls) {
		return new Reflect(cls, LOOKUP);
	}
	
	
	public static <U> Reflect<U> of(U obj) {
		return new Reflect(obj, LOOKUP);
	}
  
  
	public static <U> Reflect<U> of(Class<U> cls, MethodHandles.Lookup lookup) {
		return new Reflect(cls, lookup);
	}
	
	
	public static <U> Reflect<U> of(U obj, MethodHandles.Lookup lookup) {
		return new Reflect(obj, lookup);
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
	public Reflect<T> selectConstructor(Class ... args) {
    Constructor cct = Unchecked.call(() -> cls.getDeclaredConstructor(args));
    if(cct == null) {
      cct = Unchecked.call(() -> cls.getConstructor(args));
    }
    return new Reflect(cls, obj, mth, fld, cct, lookup);
	}
	
	
	/**
	 * Procura pelo construtor sem argumentos.
	 * @return A instância de Reflector modificada.
	 */
	public Reflect<T> selectConstructor() {
    return Reflect.this.selectConstructor((Class[])null);
	}
	
	
	/**
	 * Seleciona o construtor informado.
	 * @return Reflector com o construtor selecionado.
	 */
	public Reflect<T> selectConstructor(Constructor c) {
    if(c == null || c.getDeclaringClass() != cls) {
      throw new IllegalArgumentException("Bad Constructor: " + c);
    }
    return new Reflect(cls, obj, mth, fld, c, lookup);
	}
	
	
	private Optional<Constructor> guessConstructor(Object ... args) {
		return Arrays.asList(this.constructors())
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
	}
  
  
	private Optional<Method> guessMethod(String name, Object ... args) {
		return Arrays.asList(this.methods())
				.stream()
				.filter(c->c.getParameterCount() == args.length)
				.filter(c->c.getName().equals(name))
				.filter(c->{
					return Arrays.asList(c.getParameters())
							.stream()
							.allMatch(p->Arrays.asList(args)
									.stream()
									.anyMatch(o->p.getType().equals(o.getClass())));
				}
		).findFirst();
	}
  
  
  private String method2string(String name, Object... args) {
    StringBuilder sct = new StringBuilder(name).append("(");
    int len = sct.length();
    Arrays.asList(args).stream().map(o -> o.getClass().getName()).forEach(s -> sct.append(" ").append(s).append(","));
    if(len != sct.length()) sct.deleteCharAt(sct.length() -1);
    return sct.append(")").toString();
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
	public T create(Object ... args) {
    if(args == null) return create();
    Optional<Constructor> opt = Optional.ofNullable(this.cct);
    Constructor cct =  opt.or(() -> guessConstructor(args))
        .orElseThrow(() -> new IllegalStateException("Constructor not found: " + method2string(cls.getSimpleName(), args)));
    return (T) Unchecked.call(() -> cct.newInstance(args));
	}
	
	
  public Reflect<T> reflectCreate(Object ... args) {
    return new Reflect(create(args), lookup);
  }
	
	
	/**
	 * Cria uma nova instância da classe (<code>on(Class)</code>),
	 * chamando o construtor padrão sem argumentos.
	 * Caso não exista construtor sem argumentos, 
   * retorna <code>null</code>;
	 * @return A nova instância criada da classe.
	 */
	public T create() {
    if(cct == null) {
      return (T) this.selectConstructor().create();
    }
    return (T) Unchecked.call(() -> cct.newInstance((Object[])null));
	}
  
  
  public Reflect<T> createReflect() {
    return new Reflect(create(), lookup);
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
 		if(obj == null) throw new IllegalStateException("Object target not found");
 		if(mth == null) throw new IllegalStateException("Method not selected");
    return Unchecked.call(() -> mth.invoke(obj, (args == null ? new Object[0] : args)));
	}
	
	
	/**
	 * Invoca o método alvo com os argumentos informados.
   * @param name Nome do método.
	 * @param args Argumentos para invocar o método
	 * (<code>null</code> quando sem argumentos).
	 * @return O objeto de retorno do método invocado.
	 */
	public Object invokeMethod(String name, Object ... args) {
 		if(obj == null) throw new IllegalStateException("Object target not found");
 		Method mth = guessMethod(name, args).orElseThrow(() -> new IllegalStateException("Method not found: " + method2string(name, args)));
    return Unchecked.call(() -> mth.invoke(obj, (args == null ? new Object[0] : args)));
	}
	
	
	/**
	 * Invoca o método alvo.
   * @param name Nome do método.
	 * @return O objeto de retorno do método invocado.
	 */
	public Object invokeMethod(String name) {
 		return selectMethod(name).invoke();
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
	public Reflect<T> selectMethod(String method, Class ... args) {
    if(args == null) return selectMethod(method);
    Optional<Method> opt = Optional.ofNullable(Unchecked.call(() -> cls.getDeclaredMethod(method, args)));
    Method mth = opt.or(() -> Optional.ofNullable(Unchecked.call(() -> cls.getMethod(method, args))))
        .orElseThrow(() -> new IllegalStateException("Method not found: " + method2string(method, args)));
    return new Reflect(cls, obj, mth, fld, cct, lookup);
	}
	
	
	/**
	 * Procura pelo m�todo especificado.
	 * @param meth M�todo a ser selecionado.
	 * @return Esta inst�ncia modificada de Reflector.
	 */
	public Reflect<T> selectMethod(Method meth) {
    if(meth == null || meth.getDeclaringClass() != cls) {
      throw new IllegalArgumentException("Bad method: " + meth);
    }
    return new Reflect(cls, obj, meth, fld, cct, lookup);
	}
	
	
	/**
	 * Procura pelo método da classe
	 * (<code>on(Class)</code>), com o nome
	 * e argumentos informados
	 * (<code>null</code> quando sem argumentos).
	 * @param method Nome do método.
	 * @return A instância de Reflector modificada.
	 */
	public Reflect<T> selectMethod(String method) {
    if(method == null || method.isEmpty()) {
      throw new IllegalArgumentException("Bad Method name: " + method);
    }
    Optional<Method> opt = Arrays.asList(methods()).stream()
        .filter(m -> m.getName().equals(method))
        .filter(m -> m.getParameterCount() == 0)
        .findAny();
    Method mth = opt.or(() -> Arrays.asList(methods()).stream()
        .filter(m -> m.getName().equals(method))
        .findAny())
        .orElseThrow(() -> new IllegalStateException("Method not found: " + method));
    return new Reflect(cls, obj, mth, fld, cct, lookup);
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
  
  
  public Stream<Method> streamMethods() {
    return Arrays.asList(methods()).stream();
  }
	
	
  public Stream<Constructor> streamConstructors() {
    return Arrays.asList(constructors()).stream();
  }
	
	
  public Stream<Field> streamFields() {
    return Arrays.asList(fields()).stream();
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
	public Reflect<T> selectField(String field) {
    if(field == null || field.isEmpty()) {
      throw new IllegalArgumentException("Bad empty Field name");
    }
    Optional<Field> opt = Optional.ofNullable(Unchecked.call(() -> cls.getDeclaredField(field)));
    Field fld = opt.or(() -> Optional.ofNullable(Unchecked.call(() -> cls.getField(field))))
        .orElseThrow(() -> new IllegalStateException("Field not found: " + field));
    return new Reflect(cls, obj, mth, fld, cct, lookup);
	}
  
  
  /**
   * Retorna todos os campos da classe/objeto.
   * @return Array com todos os campos da classe/objeto.
   */
  public String[] fieldNames() {
    return Arrays.asList(fields()).stream()
        .map(f -> f.getName())
        .collect(Collectors.toList())
        .toArray(new String[0]);
  }
	
	
	/**
	 * Define o valor do campo (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>) com o valor
	 * informado.
	 * @param value Novo a ser definido para o campo.
   * @return 
	 */
	public Reflect<T> set(Object value) {
		if(fld == null) throw new IllegalStateException("Field not selected");
    if(obj == null) throw new IllegalStateException("Object owner not found");
    Unchecked.call(() -> fld.set(obj, value));
    return this;
	}
	
	
	/**
	 * Retorna o valor contido no campo 
	 * (<code>field(String)</code>)
	 * do objeto (<code>on(Object)</code>).
	 * @return Valor contido no campo.
	 */
	public Object get() {
		if(fld == null) throw new IllegalStateException("Field not selected");
    if(obj == null) throw new IllegalStateException("Object owner not found");
    return Unchecked.call(() -> fld.get(obj));
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
  
  
  public Optional<Field> field() {
    return Optional.ofNullable(fld);
  }
  
  
  public Optional<Method> method() {
    return Optional.ofNullable(mth);
  }
  
  
  public Optional<Constructor> constructor() {
    return Optional.ofNullable(cct);
  }
  
  
  private Object invokeHandle(MethodHandle handle) {
    try {
      return handle.invoke();
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private Object invokeHandle(MethodHandle handle, T target) {
    try {
      return handle.invoke(target);
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private Supplier invokeSupplierHandle(MethodHandle handle) {
    try {
      return (Supplier) handle.invokeExact();
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private BiConsumer invokeBiConsumerHandle(MethodHandle handle) {
    try {
      return (BiConsumer) handle.invokeExact();
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private Function invokeFunctionHandle(MethodHandle handle) {
    try {
      return (Function) handle.invokeExact();
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private BiFunction invokeBiFunctionHandle(MethodHandle handle) {
    try {
      return (BiFunction) handle.invokeExact();
    }
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  /**
   * Create and return a lambda to invoke the selected constructor.
   * The constructor signature must be compatible with <code>Supplier</code> signature.
   * @param <T> Supplier return type
   * @return A lambda referencing the selected constructor.
   * @throws IllegalStateException if the selected constructor signature is not 
   * compatible with <code>Supplier</code> signature.
   */
  public Supplier<T> constructorAsSupplier() {
    if(cct == null) throw new IllegalStateException("Constructor not found");
    if(cct.getParameterCount() > 0) {
      throw new IllegalStateException("Bad constructor cast: " + cct + " >> " + Supplier.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflectConstructor(cct));
    MethodType methodType = MethodType.methodType(Object.class);
    MethodType actualMethType = MethodType.methodType(cls);
    MethodType lambdaType = MethodType.methodType(Supplier.class);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "get", lambdaType, methodType, handle, actualMethType));
    return (Supplier<T>) invokeSupplierHandle(cs.getTarget());
  }
	
  
  /**
   * Create and return a lambda to invoke the selected constructor.
   * The constructor signature must be compatible with <code>Function</code> signature.
   * @param <P> Function parameter type
   * @return A lambda referencing the selected constructor.
   * @throws IllegalStateException if the selected constructor signature is not 
   * compatible with <code>Function</code> signature.
   */
  public <P> Function<P,T> constructorAsFunction() {
    if(cct == null) throw new IllegalStateException("Constructor not found");
    if(cct.getParameterCount() != 1) {
      throw new IllegalStateException("Bad constructor cast: " + cct + " >> " + Function.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflectConstructor(cct));
    MethodType methodType = MethodType.methodType(Object.class, Object.class);
    MethodType actualMethType = MethodType.methodType(cls, cct.getParameterTypes()[0]);
    MethodType lambdaType = MethodType.methodType(Function.class);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "apply", lambdaType, methodType, handle, actualMethType));
    return (Function<P,T>) invokeFunctionHandle(cs.getTarget());
  }
  
  
  
	
  
  /**
   * Create and return a lambda to invoke the selected constructor.
   * The constructor signature must be compatible with <code>lambda</code> signature.
   * @param lambda Lambda Class
   * @param <L> Lambda type
   * @return A lambda referencing the selected constructor.
   * @throws IllegalStateException if the selected constructor signature is not 
   * compatible with <code>lambda</code> signature.
   */
  public <L> L constructorAsLambda(Class<L> lambda) {
    if(cct == null) throw new IllegalStateException("Constructor not found");
    Optional<Method> lmth = Arrays.asList(Reflect.of(lambda).methods()).stream()
        .filter(m -> m.getParameterCount() == cct.getParameterCount())
        .filter(m -> Modifier.isPublic(m.getModifiers()))
        .filter(m -> Modifier.isAbstract(m.getModifiers()))
        .filter(m -> m.getReturnType() != void.class)
        .filter(m -> !m.isDefault())
        .findAny();
    if(!lmth.isPresent()) throw new IllegalArgumentException("Bad lambda type: " + lambda);
    if(cct.getParameterCount() != lmth.get().getParameterCount() 
        || lmth.get().getReturnType() == void.class) {
      throw new IllegalStateException("Bad constructor cast: " + cct + " >> " + lmth.get());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflectConstructor(cct));
    MethodType methodType = MethodType.methodType(lmth.get().getReturnType(), lmth.get().getParameterTypes());
    MethodType actualMethType = MethodType.methodType(cls, cct.getParameterTypes());
    MethodType lambdaType = MethodType.methodType(lambda);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, lmth.get().getName(), lambdaType, methodType, handle, actualMethType));
    return lambda.cast(invokeHandle(cs.getTarget()));
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.
   * The method signature must be compatible with <code>Supplier</code> signature.
   * @param <S> Supplier return type
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Supplier</code> signature.
   */
  public <S> Supplier<S> methodAsSupplier() {
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() > 0) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Supplier.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(Object.class);
    MethodType actualMethType = MethodType.methodType(mth.getReturnType());
    MethodType lambdaType = isStatic
        ? MethodType.methodType(Supplier.class)
        : MethodType.methodType(Supplier.class, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "get", lambdaType, methodType, handle, actualMethType));
    return (Supplier<S>) invokeHandle(cs.getTarget(), obj);
  }
	
  
  /**
   * Create and return a lambda to dynamic invoke the selected method on the informed object.
   * The method signature must be compatible with <code>Supplier</code> signature.
   * @param <S> Supplier return type
   * @return A function lambda referencing the dynamic method invokation.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Supplier</code> signature.
   */
  public <S> Function<T,S> dynamicSupplierMethod() {
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() > 0) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Supplier.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(Object.class, Object.class);
    MethodType lambdaType = MethodType.methodType(Function.class);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "apply", lambdaType, methodType, handle, handle.type()));
    return (Function<T,S>) invokeFunctionHandle(cs.getTarget());
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.
   * The method signature must be compatible with <code>Runnable</code> signature.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Runnable</code> signature.
   * @return A lambda referencing the selected method.
   */
  public Runnable methodAsRunnable() {
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() > 0 || mth.getReturnType() != void.class) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Runnable.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(void.class);
    MethodType lambdaType = isStatic
        ? MethodType.methodType(Runnable.class)
        : MethodType.methodType(Runnable.class, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "run", lambdaType, methodType, handle, methodType));
    return (Runnable) invokeHandle(cs.getTarget(), obj);
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.
   * The method signature must be compatible with <code>Consumer</code> signature.
   * @param <C> Consumer parameter type
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Consumer</code> signature.
   */
  public <C> Consumer<C> methodAsConsumer() {
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() != 1 || mth.getReturnType() != void.class) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Consumer.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(void.class, Object.class);
    MethodType actualMethType = MethodType.methodType(mth.getReturnType(), mth.getParameterTypes()[0]);
    MethodType lambdaType = isStatic
        ? MethodType.methodType(Consumer.class)
        : MethodType.methodType(Consumer.class, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "accept", lambdaType, methodType, handle, actualMethType));
    return (Consumer<C>) invokeHandle(cs.getTarget(), obj);
  }
	
  
  /**
   * Create and return a <code>BiConsumer</code> lambda to dynamic invoke the selected method.
   * The method signature must be compatible with <code>Consumer</code> signature.
   * @param <C> Consumer parameter type
   * @return A <code>BiConsumer</code> lambda referencing the dynamic method invokation.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Consumer</code> signature.
   */
  public <C> BiConsumer<T,C> dynamicConsumerMethod() {
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() != 1 || mth.getReturnType() != void.class) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Consumer.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(void.class, Object.class, Object.class);
    MethodType lambdaType = MethodType.methodType(BiConsumer.class);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "accept", lambdaType, methodType, handle, handle.type()));
    return (BiConsumer<T,C>) invokeBiConsumerHandle(cs.getTarget());
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.
   * The method signature must be compatible with <code>Function</code> signature.
   * @param <P> Function parameter type
   * @param <R> Function return type
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Function</code> signature.
   */
  public <P,R> Function<P,R> methodAsFunction() {
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() != 1 || mth.getReturnType() == void.class) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Function.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(Object.class, Object.class);
    MethodType actualMethType = MethodType.methodType(mth.getReturnType(), mth.getParameterTypes()[0]);
    MethodType lambdaType = isStatic
        ? MethodType.methodType(Function.class)
        : MethodType.methodType(Function.class, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "apply", lambdaType, methodType, handle, actualMethType));
    return (Function<P,R>) invokeHandle(cs.getTarget(), obj);
  }
	
  
  /**
   * Create and return a BiFunction lambda to dynamic invoke the selected method.
   * The method signature must be compatible with <code>Function</code> signature.
   * @param <P> Function parameter type
   * @param <R> Function return type
   * @return A BiFunction lambda referencing the dynamic method invokation.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>Function</code> signature.
   */
  public <P,R> BiFunction<T,P,R> dynamicFunctionMethod() {
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() != 1) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + Function.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(Object.class, Object.class, Object.class);
    MethodType lambdaType = MethodType.methodType(BiFunction.class);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "apply", lambdaType, methodType, handle, handle.type()));
    return (BiFunction<T,P,R>) invokeBiFunctionHandle(cs.getTarget());
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.
   * The method signature must be compatible with <code>BiFunction</code> signature.
   * @param <A> BiFunction first parameter type
   * @param <B> BiFunction second parameter type
   * @param <C> BiFunction return type
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>BiFunction</code> signature.
   */
  public <A,B,C> BiFunction<A,B,C> methodAsBiFunction() {
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    if(mth.getParameterCount() != 2 || mth.getReturnType() == void.class) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + BiFunction.class.getName());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(Object.class, Object.class, Object.class);
    MethodType actualMethType = MethodType.methodType(mth.getReturnType(), mth.getParameterTypes()[0], mth.getParameterTypes()[1]);
    MethodType lambdaType = isStatic
        ? MethodType.methodType(BiFunction.class)
        : MethodType.methodType(BiFunction.class, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, "apply", lambdaType, methodType, handle, actualMethType));
    return (BiFunction<A,B,C>) invokeHandle(cs.getTarget(), obj);
  }
	
  
  /**
   * Create and return a lambda to invoke the selected method.The method signature must be compatible with <code>lambda</code> signature.
   * @param <L> Lambda type
   * @param lambda Lambda class
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with <code>lambda</code> signature.
   */
  public <L> L methodAsLambda(Class<L> lambda) {
    if(lambda == null || !Modifier.isInterface(lambda.getModifiers())) {
      throw new IllegalArgumentException("Bad lambda type: " + lambda);
    }
    boolean isStatic = Modifier.isStatic(mth.getModifiers());
    if(!isStatic && obj == null) throw new IllegalStateException("Target object not found");
    if(mth == null) throw new IllegalStateException("Method not found");
    Optional<Method> lmth = Arrays.asList(Reflect.of(lambda).methods()).stream()
        .filter(m -> m.getParameterCount() == mth.getParameterCount())
        .filter(m -> Modifier.isPublic(m.getModifiers()))
        .filter(m -> Modifier.isAbstract(m.getModifiers()))
        .filter(m -> mth.getReturnType() == void.class ? m.getReturnType() == void.class : m.getReturnType() != void.class)
        .filter(m -> !m.isDefault())
        .findAny();
    if(!lmth.isPresent()) throw new IllegalArgumentException("Bad lambda type: " + lambda);
    if(mth.getParameterCount() != lmth.get().getParameterCount() 
        || (mth.getReturnType() == void.class 
        ? lmth.get().getReturnType() != void.class 
        : lmth.get().getReturnType() == void.class)) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + lmth.get());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(lmth.get().getReturnType(), lmth.get().getParameterTypes());
    MethodType actualMethType = MethodType.methodType(mth.getReturnType(), mth.getParameterTypes());
    MethodType lambdaType = isStatic
        ? MethodType.methodType(lambda)
        : MethodType.methodType(lambda, cls);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, lmth.get().getName(), lambdaType, methodType, handle, actualMethType));
    return lambda.cast(invokeHandle(cs.getTarget(), obj));
  }
	
  
  /**
   * Create and return a lambda to dynamic invoke the selected method.
   * The method signature must be compatible with the <code>lambda</code> signature.
   * @param <L> Lambda type
   * @param lambda Lambda class
   * @return A lambda referencing the selected method.
   * @throws IllegalStateException if the selected method signature is not 
   * compatible with the <code>lambda</code> signature.
   */
  public <L> L dynamicLambdaMethod(Class<L> lambda) {
    if(lambda == null || !Modifier.isInterface(lambda.getModifiers())) {
      throw new IllegalArgumentException("Bad lambda type: " + lambda);
    }
    if(mth == null) throw new IllegalStateException("Method not found");
    Optional<Method> lmth = Arrays.asList(Reflect.of(lambda).methods()).stream()
        .filter(m -> m.getParameterCount() -1 == mth.getParameterCount())
        .filter(m -> Modifier.isPublic(m.getModifiers()))
        .filter(m -> Modifier.isAbstract(m.getModifiers()))
        .filter(m -> mth.getReturnType() == void.class ? m.getReturnType() == void.class : m.getReturnType() != void.class)
        .filter(m -> !m.isDefault())
        .findAny();
    if(!lmth.isPresent()) throw new IllegalArgumentException("Bad lambda type: " + lambda);
    if(mth.getParameterCount() != lmth.get().getParameterCount() -1 
        || (mth.getReturnType() == void.class 
        ? lmth.get().getReturnType() != void.class 
        : lmth.get().getReturnType() == void.class)) {
      throw new IllegalStateException("Bad method cast: " + mth + " >> " + lmth.get());
    }
    MethodHandle handle = Unchecked.call(() -> lookup.unreflect(mth));
    MethodType methodType = MethodType.methodType(lmth.get().getReturnType(), lmth.get().getParameterTypes());
    MethodType lambdaType = MethodType.methodType(lambda);
    CallSite cs = Unchecked.call(() -> LambdaMetafactory.metafactory(lookup, lmth.get().getName(), lambdaType, methodType, handle, handle.type()));
    return lambda.cast(invokeHandle(cs.getTarget()));
  }
  
  
  public static ClassDefinition defineClass(String cname) {
    return new ClassDefinition(cname);
  }

}
