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

package us.pserver.revok.container;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static us.pserver.chk.Checker.nullarg;


/**
 * Armazena os objetos no servidor, vinculando-os 
 * à uma chave de recuperação <code>String</code>.
 * <code>ObjectContainer</code> é seguro para ambientes
 * com múltiplos processos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class ObjectContainer {
  
  public static final String NAMESPACE_GLOBAL = "global";
  
  private final Map<String, Map<String, Object>> space;
  
  private Authenticator auth;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public ObjectContainer() {
    space = new ConcurrentHashMap<>();
    space.put(NAMESPACE_GLOBAL, new ConcurrentHashMap<>());
    space.get(NAMESPACE_GLOBAL).put("ObjectContainer", this);
  }
  
  
  public ObjectContainer(Authenticator a) {
    this();
    nullarg(Authenticator.class, a);
    auth = a;
  }
  
  
  public ObjectContainer setAuthenticator(Authenticator a) {
    auth = a;
    return this;
  }
  
  
  public Authenticator getAuthenticator() {
    return auth;
  }
  
  
  public boolean isAuthEnabled() {
    return auth != null;
  }
  
  
  /**
   * Insere um objeto a ser armazenado com
   * uma chave <code>String</code> de recuperação.
   * @param name Chave <code>String</code> de recuperação 
   * do objeto armazenado.
   * @param obj Objeto a ser armazenado.
   * @return Esta instância modificada de <code>ObjectContainer</code>.
   */
  public ObjectContainer put(String name, Object obj) {
    if(name != null && obj != null) {
      if(!name.contains("."))
        throw new IllegalArgumentException(
            "[ObjectContainer.put( String, Object )] "
                + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
      String[] names = name.split("\\.");
      put(names[0], names[1], obj);
    }
    return this;
  }
  
  
  public ObjectContainer put(String namespace, String name, Object obj) {
    if(namespace == null)
      throw new IllegalArgumentException(
          "[ObjectContainer.put( String, String, Object )] "
              + "Invalid Namespace {"+ namespace+ "}");
    if(name == null)
      throw new IllegalArgumentException(
          "[ObjectContainer.put( String, String, Object )] "
              + "Invalid name {"+ name+ "}");
    if(obj == null)
      throw new IllegalArgumentException(
          "[ObjectContainer.put( String, String, Object )] "
              + "Invalid Object {"+ obj+ "}");
    if(!space.containsKey(namespace)) {
      space.put(namespace, new ConcurrentHashMap<>());
    }
    space.get(namespace).put(name, obj);
    return this;
  }
  
  
  /**
   * Remove um objeto do container através da 
   * chave de identificação.
   * @param name Chave <code>String</code>.
   * @return O objeto removido do container.
   */
  public Object remove(String name) throws AuthenticationException {
    if(isAuthEnabled())
      throw new AuthenticationException("[ObjectContainer.remove( String )] Authentication needed");
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.remove( String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      return space.get(names[0]).remove(names[1]);
    }
    return null;
  }
  
  
  public Object remove(Credentials c, String name) throws AuthenticationException {
    if(isAuthEnabled()) {
      auth.authenticate(c);
    }
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.remove( Credentials, String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      return space.get(names[0]).remove(names[1]);
    }
    return null;
  }
  
  
  /**
   * Verifica se existe um objeto armazenado,
   * identificado pela chave fornecida.
   * @param name Chave <code>String</code>.
   * @return <code>true</code> se existir um
   * objeto armazenado identificado pela chave fornecida,
   * <code>false</code> caso contrário.
   */
  public boolean contains(String name) {
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.contains( String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      return space.get(names[0]).containsKey(names[1]);
    }
    return false;
  }
  
  
  /**
   * Retorna o objeto armazenado no container,
   * identificado pela chave fornecida.
   * @param name Chave <code>String</code>.
   * @return O Objeto armazenado ou <code>null</code>
   * caso a chave de identificação não exista no container.
   */
  public Object get(String name) throws AuthenticationException {
    if(isAuthEnabled())
      throw new AuthenticationException("[ObjectContainer.get( String )] Authentication needed");
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.get( String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      return space.get(names[0]).get(names[1]);
    }
    return null;
  }
  
  
  public Object get(Credentials c, String name) throws AuthenticationException {
    if(isAuthEnabled()) {
      auth.authenticate(c);
    }
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.get( Credentials, String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      return space.get(names[0]).get(names[1]);
    }
    return null;
  }
  
  
  /**
   * Retorna a quantidade de objetos armazenados.
   * @return <code>int</code>.
   */
  public int namespaceSize() {
    return space.size();
  }
  
  
  public int sizeOf(String namespace) {
    if(namespace == null || !space.containsKey(namespace))
      return -1;
    return space.get(namespace).size();
  }
  
  
  public List<String> namespaces() {
    List<String> nsp = new LinkedList<>();
    space.keySet().stream().forEach(m->nsp.add(m));
    return nsp;
  }
  
  
  public List<String> objects(String namespace) {
    List<String> nsp = new LinkedList<>();
    if(namespace == null || !space.containsKey(namespace))
      return nsp;
    space.get(namespace).keySet().stream().forEach(no->nsp.add(no));
    return nsp;
  }
  
  
  public List<Method> listMethods(String name) {
    if(!name.contains("."))
      throw new IllegalArgumentException(
          "[ObjectContainer.get( Credentials, String )] "
              + "Namespace missing. Name argument must be provided like: <namespace>.<object_name>");
    List<Method> mts = new LinkedList<>();
    String[] names = name.split("\\.");
    if(space.containsKey(names[0])) {
      Object o = space.get(names[0]).get(names[1]);
      if(o != null) {
        Reflector ref = new Reflector();
        mts.addAll(Arrays.asList(ref.on(o).methods()));
      }
    }
    return mts;
  }

}
