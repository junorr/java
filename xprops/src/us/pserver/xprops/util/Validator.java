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

package us.pserver.xprops.util;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2015
 */
public class Validator<T> extends ThrowsValidator<T, IllegalArgumentException> {

  
  public Validator(T obj) {
    super(obj, IllegalArgumentException.class);
  }
  
  
  public static <X> Validator<X> off(X obj) {
    return new Validator(obj);
  }
  

  public <X> Validator<X> on(X obj) {
    return new Validator(obj);
  }
  

  @Override
  public Validator<T> fail() throws IllegalArgumentException {
    if(hasError()) throwException();
    return this;
  }
  
  
  @Override
  public Validator<T> fail(String msg) throws IllegalArgumentException {
    return this.message(msg).validator().fail();
  }
  
  
  @Override
  public Validator<T> fail(String msg, Object ... args) throws IllegalArgumentException {
    return this.message(msg, args).validator().fail();
  }
  
  
  @Override
  public Validator<T> fail(Class cls) throws IllegalArgumentException {
    return this.message(cls).validator().fail();
  }
  
}
