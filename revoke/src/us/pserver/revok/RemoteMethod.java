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

package us.pserver.revok;

import us.pserver.revok.container.Credentials;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import us.pserver.revok.http.HttpConsts;
import us.pserver.revok.server.Invoker;

/**
 * Representa um método remoto a ser invocado
 * em um objeto remoto. Contêm informações sobre o nome 
 * do método, nome do objeto proprietário, lista de argumentos 
 * e suas classes de tipo, além da classe do tipo de 
 * retorno do método.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class RemoteMethod {
  
  private String objname;
  
  private String method;
  
  private List params;
  
  private List<Class> types;
  
  private Credentials cred;
  
  private String returnVar;
  
  
  /**
   * Construtor padrão sem argumentos, constrói
   * um objeto vazio.
   */
  public RemoteMethod() {
    objname = null;
    method = null;
    params = new LinkedList();
    types = new LinkedList<>();
    cred = null;
    returnVar = null;
  }

  
  /**
   * Construtor que recebe como argumentos o nome
   * do método e nome do objeto proprietário.
   * @param ownerObject Nome do objeto proprietário.
   * @param name Nome do método.
   */
  public RemoteMethod(String ownerObject, String name) {
    method = name;
    objname = ownerObject;
    params = new LinkedList();
    types = new LinkedList<>();
    cred = null;
  }
  
  
  public RemoteMethod credentials(Credentials c) {
    cred = c;
    return this;
  }
  
  
  public Credentials credentials() {
    return cred;
  }
  
  
  /**
   * Adiciona um argumento ao método remoto.
   * @param obj Argumento de invocação do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod addParam(Object obj) {
    params.add(obj);
    return this;
  }
  
  
  public RemoteMethod addType(Class cls) {
    if(cls != null)
      types.add(cls);
    return this;
  }
  
  
  public RemoteMethod returnVar(String var) {
    if(var == null || !var.startsWith(Invoker.VAR_MARK))
      throw new IllegalArgumentException("[RemoteMethod.returnVar( String )] "
          + "Invalid var name {"+ var+ "}. Variables must starts with '"+ Invoker.VAR_MARK+ "'");
    returnVar = var;
    return this;
  }
  
  
  public String returnVar() {
    return returnVar;
  }
  
  
  /**
   * Limpa a lista de argumentos do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod clearParams() {
    params.clear();
    return this;
  }
  
  
  public RemoteMethod clearTypes() {
    types.clear();
    return this;
  }
  
  
  public List<Class> types() {
    return types;
  }
  
  
  public List params() {
    return params;
  }
  
  
  public Class[] typesArray() {
    Class[] cls = new Class[types.size()];
    if(types.isEmpty()) return cls;
    return types.toArray(cls);
  }
  
  
  /**
   * Define a lista de classes dos tipos de argumentos do método.
   * @param cls Classes dos tipos de argumentos.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod types(Class ... cls) {
    types.addAll(Arrays.asList(cls));
    return this;
  }
  
  
  /**
   * Extrai as classes a partir dos argumentos 
   * informados para o método.
   * @return Array de classes de tipos de argumentos.
   */
  public RemoteMethod extTypesParams() {
    if(params.isEmpty()) return null;
    types.clear();
    for(int i = 0; i < params.size(); i++) {
      types.add(params.get(i).getClass());
    }
    return this;
  }
  
  
  /**
   * Retorna o nome do método.
   * @return nome do método.
   */
  public String method() {
    return method;
  }


  /**
   * Define o nome do método.
   * @param name Nome do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod method(String name) {
    this.method = name;
    return this;
  }


  /**
   * Define os argumentos do método.
   * @param objs Argumentos do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod params(Object ... objs) {
    if(objs != null && objs.length > 0) {
      params.clear();
      params.addAll(Arrays.asList(objs));
    }
    return this;
  }


  /**
   * Retorna o nome do objeto proprietário do método.
   * @return nome do objeto proprietário do método.
   */
  public String objectName() {
    return objname;
  }


  /**
   * Define o nome do objeto proprietário do método.
   * @param objName nome do objeto proprietário do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod forObject(String objName) {
    this.objname = objName;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.method);
    hash = 47 * hash + Objects.hashCode(this.objname);
    if(this.types != null)
      hash = 47 * hash + Arrays.deepHashCode(this.types.toArray());
    else
      hash = 47 * hash + Objects.hashCode(this.types);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final RemoteMethod other = (RemoteMethod) obj;
    if (!Objects.equals(this.objname, other.objname))
      return false;
    if (!Objects.equals(this.method, other.method))
      return false;
    if (this.types == null || other.types == null
        || !Arrays.deepEquals(this.types.toArray(), 
            other.types.toArray()))
      return false;
    return true;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(returnVar != null) {
      sb.append(returnVar)
          .append(HttpConsts.SP)
          .append(HttpConsts.EQ)
          .append(HttpConsts.SP);
    }
    if(objname != null) {
      sb.append(objname);
    }
    sb.append(".").append(method).append("( ");
    if(!params.isEmpty()) {
      for(int i = 0; i < params.size(); i++) {
        sb.append(params.get(i));
        if(i < params.size() -1)
          sb.append(", ");
      }
    }
    else if(types != null) {
      for(int i = 0; i < types.size(); i++) {
        sb.append(types.get(i).getSimpleName());
        if(i < types.size() -1)
          sb.append(", ");
      }
    }
    return sb.append(" )").toString();
  }
  
}
