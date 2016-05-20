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

package us.pserver.insane.impl;

import java.util.function.Predicate;
import us.pserver.insane.Panic;
import us.pserver.insane.Sanity;
import us.pserver.insane.test.SanityPredicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/05/2016
 */
public class DefaultSanity<T> implements Sanity<T> {
  
  public static final String DEFAULT_MSG = "Invalid Argument Value";
  
  private final T value;
  
  private final Panic panic;
  
  private final Predicate<T> predicate;
  
  private final String message;
  
  
  private DefaultSanity(T value, Panic panic, Predicate<T> predicate, String message) {
    this.value = value;
    this.predicate = predicate;
    this.panic = panic;
    this.message = message;
  }
  
  
  public DefaultSanity(T value) {
    this(value, m -> {throw new IllegalArgumentException(m);}, v -> true, null);
  }
  

  @Override
  public T check() throws RuntimeException {
    if(!predicate.test(value)) {
      String msg = DEFAULT_MSG;
      if(message != null) {
        msg = message;
      }
      else if(predicate instanceof SanityPredicate) {
        msg = ((SanityPredicate)predicate).message();
      }
      panic.panic(msg);
    }
    return value;
  }
  

  @Override
  public T check(Predicate<T> test) throws RuntimeException {
    return (T) this.with(test).check();
  }

  
  @Override
  public Sanity<T> with(Predicate predicate) {
    return new DefaultSanity(value, panic, predicate, message);
  }


  @Override
  public Sanity<T> with(Panic panic) {
    return new DefaultSanity(value, panic, predicate, message);
  }


  @Override
  public Sanity<T> with(String message) {
    return new DefaultSanity(value, panic, predicate, message);
  }

}
