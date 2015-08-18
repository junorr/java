/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.tools;

/**
 * Utility class for argument validations and IllegalArgumentException throwing on fails.
 * @param <T> Type of the validated object.
 * @author Juno Roesler - juno@pserver.us
 */
public class Valid<T> extends ValidThrows<T, IllegalArgumentException> {

  
  /**
   * Default constructor, receives the object to be validated.
   * @param obj the object to be validated.
   */
  public Valid(T obj) {
    super(obj, IllegalArgumentException.class);
  }
  
  
  /**
   * Create a new Valid instance for the given object.
   * @param <X> Type of the object.
   * @param obj The object to be validated.
   * @return New Valid instance.
   */
  public static <X> Valid<X> off(X obj) {
    return new Valid(obj);
  }
  

  /**
   * Create a new Valid instance for the given object.
   * @param <X> Type of the object.
   * @param obj The object to be validated.
   * @return New Valid instance.
   */
  public <X> Valid<X> on(X obj) {
    return new Valid(obj);
  }
  

  @Override
  public Valid<T> fail() throws IllegalArgumentException {
    if(hasError()) throwException();
    return this;
  }
  
  
  @Override
  public Valid<T> fail(String msg) throws IllegalArgumentException {
    return this.message(msg).valid().fail();
  }
  
  
  @Override
  public Valid<T> fail(String msg, Object ... args) throws IllegalArgumentException {
    return this.message(msg, args).valid().fail();
  }
  
  
  @Override
  public Valid<T> fail(Class cls) throws IllegalArgumentException {
    return this.message(cls).valid().fail();
  }
  
}
