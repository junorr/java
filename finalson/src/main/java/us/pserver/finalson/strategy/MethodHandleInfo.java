/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.finalson.strategy;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class MethodHandleInfo {
  
  private final MethodHandle handle;

  private final Class retype;

  private final List<Parameter> params;

  private final List<Annotation> annots;

  private final String name;

  
  public MethodHandleInfo(MethodHandle mh, String name, Class returnType, List<Parameter> params, List<Annotation> annots) {
    this.handle = NotNull.of(mh).getOrFail("Bad null MethodHandle");
    this.name = NotNull.of(name).getOrFail("Bad null name");
    this.retype = NotNull.of(returnType).getOrFail("Bad null return Class");
    this.params = NotNull.of(params).getOrFail("Bad null parameters List");
    this.annots = NotNull.of(annots).getOrFail("Bad null annotations List");
  }
  
  
  public MethodHandleInfo bindTo(Object obj) {
    return new MethodHandleInfo(handle.bindTo(obj), name, retype, params, annots);
  }
  
  
  public MethodHandleInfo withName(String name) {
    return new MethodHandleInfo(handle, name, retype, params, annots);
  }

  
  public MethodHandle getMethodHandle() {
    return handle;
  }

  
  public List<Parameter> getParameters() {
    return params;
  }

  
  public List<Annotation> getAnnotations() {
    return annots;
  }
  

  public Class getReturnType() {
    return retype;
  }

  
  public boolean hasReturnType() {
    return retype != null 
        && retype != void.class 
        && retype != Void.class;
  }

  public String getName() {
    return name;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.retype);
    hash = 37 * hash + Objects.hashCode(this.params);
    hash = 37 * hash + Objects.hashCode(this.name);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MethodHandleInfo other = (MethodHandleInfo) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.retype, other.retype)) {
      return false;
    }
    return Objects.equals(this.params, other.params);
  }


  @Override
  public String toString() {
    return String.format("%s( %s ) : %s", name, params, retype);
  }
  
  
  public static MethodHandleInfo of(Method meth) {
    NotNull.of(meth).failIfNull("Bad null Method");
    try {
      MethodHandle mh = MethodHandles.lookup().unreflect(meth);
      return new MethodHandleInfo(mh, 
          meth.getName(), meth.getReturnType(), 
          Arrays.asList(meth.getParameters()), 
          Arrays.asList(meth.getAnnotations()));
    } catch(IllegalAccessException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public static MethodHandleInfo of(Constructor cct) {
    NotNull.of(cct).failIfNull("Bad null Method");
    try {
      MethodHandle mh = MethodHandles.lookup().unreflectConstructor(cct);
      return new MethodHandleInfo(mh, 
          cct.getName(), void.class, 
          Arrays.asList(cct.getParameters()), 
          Arrays.asList(cct.getAnnotations()));
    } catch(IllegalAccessException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
}
