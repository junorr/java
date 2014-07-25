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

package us.pserver.remote;

import com.jpower.rfl.Reflector;


/**
 * Classe responsável pelo invocação de métodos
 * em objetos utilizando reflexão.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class Invoker {
  
  /**
   * Número padrão de tentativas na invocação do método em caso de erro
   * <br/><code>DEFAULT_INVOKE_TRIES = 5</code>.
   * Permite maior qualidade nos resultados das invocações que,
   * sob ambientes de múltiplos processos, podem gerar resultados
   * imprecisos.
   */
  public static final int DEFAULT_INVOKE_TRIES = 5;
  
  private Object obj;
  
  private RemoteMethod mth;
  
  private Reflector ref;
  
  private int tries;
  
  
  /**
   * Construtor padrão de <code>Invoker</code>,
   * recebe o objeto cujo método será invocado
   * e <code>RemoteMethod</code> contendo a 
   * descrição do método.
   * @param o Objeto cujo método será invocado.
   * @param r <code>RemoteMethod</code>
   * @see us.pserver.remote.RemoteMethod
   */
  public Invoker(Object o, RemoteMethod r) {
    if(o == null) 
      throw new IllegalArgumentException(
          "Invalid Object ["+ o+ "]");
    if(r == null || r.method() == null) 
      throw new IllegalArgumentException(
          "Invalid RemoteMethod ["+ r+ "]");
    obj = o;
    mth = r;
    ref = new Reflector();
    tries = DEFAULT_INVOKE_TRIES;
  }
  
  
  /**
   * Retorna o número máximo de tentativas 
   * de invocação do método em caso de erro.
   * @return <code>int</code>.
   * @see us.pserver.remote.Invoker#DEFAULT_INVOKE_TRIES
   */
  public int tries() {
    return tries;
  }
  
  
  /**
   * Define o número máximo de tentativas 
   * de invocação do método em caso de erro.
   * @param t <code>int</code>.
   * @see us.pserver.remote.Invoker#DEFAULT_INVOKE_TRIES
   */
  public Invoker setTries(int t) {
    if(t > 1) tries = t;
    return this;
  }
  
  
  /**
   * Invoca o método.
   * @return Objeto de retorno do método ou
   * <code>null</code> no caso <code>void</code>.
   * @throws MethodInvocationException caso
   * ocorra erro na invocação do método.
   */
  public Object invoke() throws MethodInvocationException {
    return invoke(0);
  }
  
  
  /**
   * Invoca o método em modo de recursão
   * até o número máximo de tentativas definidas.
   * @param currTry Número da tentativa atual.
   * @return Objeto de retorno do método ou
   * <code>null</code> no caso de <code>void</code>.
   * @throws MethodInvocationException Caso ocorra 
   * erro na invocação do método.
   * @see DEFAULT_INVOKE_TRIES
   */
  private Object invoke(int currTry) throws MethodInvocationException {
    if(obj == null || mth == null 
        || mth.method() == null 
        || tries < 1 || ref == null) 
      throw new IllegalStateException(
          "Invoker not properly configured");
    
    ref.on(obj).method(mth.method(), mth.getArgTypes());
    
    if(!ref.isMethodPresent()) {
      if(currTry < tries)
        return invoke(currTry+1);
      
      throw new MethodInvocationException("Method not found: "+ mth);
    }
      
    Object ret = ref.invoke(mth.arguments());
      
    if(ref.hasError()) {
      if(currTry < tries) 
        return invoke(currTry+1);
        
      throw new MethodInvocationException(
          "Invocation error ["
          + ref.getError().toString()+ "]", 
          ref.getError());
    }
      
    return ret;
  }
  
}
