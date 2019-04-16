/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;


/**
 *
 * @author juno
 */
public class ClassDefinition {
  
  private final String LN = "\n";
  
  private final String name;
  
  private final StringBuilder code;
  
  
  public ClassDefinition(String name) {
    this.name = Objects.requireNonNull(name);
    this.code = new StringBuilder();
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
  
  
  public <T> Class<T> compile() {
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
    return (Class<T>) Unchecked.call(() -> loader.loadClass(name));
  }
  
  
  public <T> Reflect<T> reflectCompiled() {
    return Reflect.of(compile());
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
