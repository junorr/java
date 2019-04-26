/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


/**
 *
 * @author juno
 */
public class ClassDefinition {
  
  public static final String LOOKUP_CODE = "public static MethodHandles.Lookup _lookup_() { return MethodHandles.lookup(); }";
  
  public static final String IMPORT_LOOKUP_CODE = "import java.lang.invoke.MethodHandles;";
  
  private final String LN = "\n";
  
  private final String name;
  
  private final StringBuilder code;
  
  private final AtomicReference<Class> clazz;
  
  
  public ClassDefinition(String name) {
    this.name = Objects.requireNonNull(name);
    this.code = new StringBuilder();
    this.clazz = new AtomicReference();
  }
  
  
  public ClassDefinition append(String s) {
    this.code.append(s);
    return this;
  }
  
  public ClassDefinition append(Object o) {
    this.code.append(o);
    return this;
  }
  
  public ClassDefinition append(String str, Object... args) {
    this.code.append(String.format(str, args));
    return this;
  }
  
  public ClassDefinition appendln(String s) {
    this.code.append(s).append(LN);
    return this;
  }
  
  public ClassDefinition appendln(Object o) {
    this.code.append(o).append(LN);
    return this;
  }
  
  public ClassDefinition appendln(String str, Object... args) {
    this.code.append(String.format(str, args)).append(LN);
    return this;
  }
  
  public String getSourceCode() {
    return this.code.toString();
  }
  
  
  private void insertLookupCode() {
    if(code.indexOf(LOOKUP_CODE) < 0) {
      int ipkg = code.indexOf("package");
      int afterpkg = code.indexOf(";", ipkg);
      code.insert(afterpkg + 1, IMPORT_LOOKUP_CODE);
      code.insert(code.length() -1, LOOKUP_CODE);
    }
  }
  
  
  public MethodHandles.Lookup definedClassLookup() {
    if(clazz.get() == null) throw new IllegalStateException("Class not compiled");
    return (MethodHandles.Lookup) Reflect.of(clazz.get())
        .selectMethod("_lookup_")
        .invoke();
  }
  
  
  public <T> Class<T> recompile() {
    clazz.set(null);
    return compile();
  }
  
  
  public <T> Class<T> compile() {
    if(clazz.get() != null) {
      return clazz.get();
    }
    if(code.indexOf(LOOKUP_CODE) < 0) {
      this.insertLookupCode();
    }
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    ClassFileManager manager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));
    compiler.getTask(null, manager, null, null, null, 
        Arrays.asList(new CharSequenceJavaFileObject(name, getSourceCode()))
    ).call();
    ClassLoader loader = new ClassLoader() {
      @Override
      protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = manager.getCompiledBytes();
        return defineClass(name, b, 0, b.length);
      }
    };
    clazz.compareAndSet(null, Unchecked.call(() -> loader.loadClass(name)));
    return clazz.get();
  }
  
  
  public <T> Reflect<T> reflectCompiled() {
    if(clazz.get() == null) compile();
    return Reflect.of(clazz.get(), definedClassLookup());
  }
  
  
  
  
  
  static final class JavaFileObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();

    JavaFileObject(String name, JavaFileObject.Kind kind) {
      super(URI.create("string:///" 
          + name.replace('.', '/') 
          + kind.extension), kind
      );
    }

    byte[] getBytes() {
      return os.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
      return os;
    }
    
  }
  
  
  
  static final class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private JavaFileObject fileobj;
    
    ClassFileManager(StandardJavaFileManager m) {
      super(m);
    }
    
    @Override
    public JavaFileObject getJavaFileForOutput(
        JavaFileManager.Location location, String className, 
        JavaFileObject.Kind kind, FileObject sibling
    ) {
      return fileobj = new JavaFileObject(className, kind);
    }
    
    public byte[] getCompiledBytes() {
      return Objects.requireNonNull(fileobj).getBytes();
    }
    
  }
  
  
  
  static final class CharSequenceJavaFileObject extends SimpleJavaFileObject {

    private final CharSequence content;
    
    public CharSequenceJavaFileObject(String className, CharSequence content) {
      super(URI.create("string:///" 
          + className.replace('.', '/') 
          + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE
      );
      this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return content;
    }

  }
  
  
}
