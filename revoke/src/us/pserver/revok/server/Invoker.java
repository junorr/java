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

package us.pserver.revok.server;

import com.jpower.rfl.Reflector;
import java.util.List;
import java.util.stream.Collectors;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.log.LogProvider;
import us.pserver.revok.MethodInvocationException;
import us.pserver.revok.RemoteMethod;
import us.pserver.revok.container.AuthenticationException;
import us.pserver.revok.container.Credentials;
import us.pserver.revok.container.ObjectContainer;
import static us.pserver.revok.server.Invoker.DEFAULT_INVOKE_TRIES;


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
  
  public static final String VAR_MARK = "$";
  
  private ObjectContainer container;
  
  private Credentials credentials;
  
  private Reflector ref;
  
  private Object target;
  
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
  public Invoker(ObjectContainer cont, Credentials cred) throws MethodInvocationException {
    if(cont == null) 
      throw new IllegalArgumentException("[Invoker( ObjectContainer, RemoteMethod )] "
          + "Invalid ObjectContainer ["+ cont+ "]");
    container = cont;
    if(container.isAuthEnabled()) {
      LogProvider.getSimpleLog().info(
          "Authentication Enabled: "+ cred);
      if(cred == null) throw new MethodInvocationException(
          "[Invoker( ObjectContainer, RemoteMethod )] "
              + "Invalid Credentials ["+ cred+ "]");
    }
    target = null;
    credentials = cred;
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
  
  
  public Object getObject(RemoteMethod rm) 
      throws MethodInvocationException, AuthenticationException {
    nullarg(RemoteMethod.class, rm);
    if(!container.contains(rm.objectName())) {
      throw new MethodInvocationException("[Invoker.getObject( RemoteMethod )] "
          + "Object not found ["+ rm.objectName()+ "]");
    }
    return getObject(rm.objectName());
  }
  
  
  private Object getObject(String name) throws MethodInvocationException, AuthenticationException {
    Object o = null;
    if(container.isAuthEnabled()) {
      o = container.get(credentials, name);
    }
    else {
      o = container.get(name);
    }
    return o;
  }
  
  
  private Object invokeAndSave(RemoteMethod mth) throws MethodInvocationException, AuthenticationException {
    Object res = invoke(mth, 0);
    if(res != null) {
      container.put(mth.returnVar().substring(1), res);
    }
    return res;
  }
    
    
  /**
   * Invoca o método.
   * @return Objeto de retorno do método ou
   * <code>null</code> no caso <code>void</code>.
   * @throws MethodInvocationException caso
   * ocorra erro na invocação do método.
   */
  public Object invoke(RemoteMethod mth) throws MethodInvocationException, AuthenticationException {
    if(mth.returnVar() != null) {
      return invokeAndSave(mth);
    }
    else {
      return invoke(mth, 0);
    }
  }
  
  
  private void processArgs(RemoteMethod mth) throws MethodInvocationException, AuthenticationException {
    List args = (List) mth.params().stream()
        .filter(o->o.toString().startsWith(VAR_MARK))
        .collect(Collectors.toList());
    for(Object o : args) {
      Object x = getObject(o.toString().substring(1));
      int idx = mth.params().indexOf(o);
      mth.params().set(idx, x);
    }
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
  private Object invoke(RemoteMethod mth, int currTry) throws MethodInvocationException, AuthenticationException {
    if(container == null || mth == null 
        || mth.method() == null 
        || tries < 1 || ref == null) 
      throw new IllegalStateException(
          "Invoker not properly configured");
    
    if(target == null && mth.objectName() == null) {
      throw new MethodInvocationException("[Invoker.invoke( RemoteMethod )] "
          + "Invalid Target Object Name {"+ mth.objectName()+ "}");
    }
    if(mth.objectName() != null) {
      target = getObject(mth);
    }
    
    processArgs(mth);
    Class[] cls = (mth.types().isEmpty() ? null : mth.typesArray());
    ref.on(target).method(mth.method(), cls);
    
    if(!ref.isMethodPresent()) {
      if(currTry < tries)
        return invoke(mth, currTry+1);
      
      throw new MethodInvocationException("Method not found: "+ mth);
    }
    
    Object ret = ref.invoke((mth.params().isEmpty() 
        ? null : mth.params().toArray()));
      
    if(ref.hasError()) {
      if(currTry < tries) 
        return invoke(mth, currTry+1);
        
      throw new MethodInvocationException(
          "Invocation error ["
          + ref.getError().toString()+ "]", 
          ref.getError());
    }
      
    return ret;
  }
  
}
