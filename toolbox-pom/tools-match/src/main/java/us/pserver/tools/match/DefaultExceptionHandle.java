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

package us.pserver.tools.match;

import us.pserver.tools.Reflect;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/01/2018
 */
public class DefaultExceptionHandle<T extends Throwable> implements ExceptionHandle<T> {
  
  private final Class<T> exClass;
  
  private final String message;
  
  private final Throwable root;
  

  public DefaultExceptionHandle(Class<T> exClass, String message, Throwable root) {
    if(exClass == null) {
      throw new IllegalArgumentException("Bad null exception class");
    }
    this.exClass = exClass;
    this.message = message;
    this.root = root;
  }
  
  public DefaultExceptionHandle(Class<T> exClass, String message) {
    this(exClass, message, null);
  }
  
  public DefaultExceptionHandle(Class<T> exClass, Throwable root) {
    this(exClass, null, root);
  }
  
  public DefaultExceptionHandle(Class<T> exClass) {
    this(exClass, null, null);
  }
  
  @Override
  public ExceptionHandle<T> withMessage(String msg) {
    return new DefaultExceptionHandle(exClass, msg, root);
  }


  @Override
  public ExceptionHandle<T> causedBy(Throwable root) {
    return new DefaultExceptionHandle(exClass, message, root);
  }
  
  
  private T create() {
    return (T) Reflect.of(exClass).create();
  }

  
  private T create(String message) {
    return (T) Reflect.of(exClass).create(message);
  }

  
  private T create(Throwable root) {
    return (T) Reflect.of(exClass).create(root);
  }

  
  private T create(String message, Throwable root) {
    return (T) Reflect.of(exClass).create(message, root);
  }

  
  @Override
  public void doThrow() throws T {
    T th;
    if(message == null && root == null) {
      th = create(); 
    }
    else if(message == null) {
      th = create(root);
    }
    else if(root == null) {
      th = create(message);
    }
    else {
      th = create(message, root);
    }
    throw th;
  }


  @Override
  public void doThrow(String msg) throws T {
    withMessage(msg).doThrow();
  }


  @Override
  public void doThrow(Throwable root) throws T {
    causedBy(root).doThrow();
  }


  @Override
  public void doThrow(String msg, Throwable root) throws T {
    withMessage(msg).causedBy(root).doThrow();
  }
  

}
