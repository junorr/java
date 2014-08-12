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

package us.pserver.rob;

import us.pserver.rob.container.Credentials;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
  
  private String objName;
  
  private String mthName;
  
  private List args;
  
  private Class[] argTypes;
  
  private Class retType;
  
  private Credentials cred;
  
  
  /**
   * Construtor padrão sem argumentos, constrói
   * um objeto vazio.
   */
  public RemoteMethod() {
    objName = null;
    mthName = null;
    args = new LinkedList();
    argTypes = null;
    cred = null;
    retType = null;
  }

  
  /**
   * Construtor que recebe como argumentos o nome
   * do método e nome do objeto proprietário.
   * @param ownerObject Nome do objeto proprietário.
   * @param name Nome do método.
   */
  public RemoteMethod(String ownerObject, String name) {
    this.mthName = name;
    this.objName = ownerObject;
    args = new LinkedList();
  }
  
  
  public RemoteMethod setCredentials(Credentials c) {
    cred = c;
    return this;
  }
  
  
  public Credentials getCredentials() {
    return cred;
  }
  
  
  /**
   * Adiciona um argumento ao método remoto.
   * @param obj Argumento de invocação do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod addArgument(Object obj) {
    args.add(obj);
    return this;
  }
  
  
  /**
   * Limpa a lista de argumentos do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod clearArguments() {
    args.clear();
    return this;
  }
  
  
  /**
   * Define a lista de classes dos tipos de argumentos do método.
   * @param cls Classes dos tipos de argumentos.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod setArgTypes(Class ... cls) {
    argTypes = cls;
    return this;
  }
  
  
  /**
   * Extrai as classes a partir dos argumentos 
   * informados para o método.
   * @return Array de classes de tipos de argumentos.
   */
  public Class[] extractTypesFromArgs() {
    if(args.isEmpty()) return null;
    argTypes = new Class[args.size()];
    for(int i = 0; i < args.size(); i++) {
      argTypes[i] = args.get(i).getClass();
    }
    return argTypes;
  }
  
  
  /**
   * Retorna um array com as classes dos tipos
   * de argumentos do método.
   * @return array com as classes dos tipos
   * de argumentos do método.
   */
  public Class[] getArgTypes() {
    return argTypes;
  }
  
  
  /**
   * Retorna o nome do método.
   * @return nome do método.
   */
  public String method() {
    return mthName;
  }


  /**
   * Define o nome do método.
   * @param name Nome do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod method(String name) {
    this.mthName = name;
    return this;
  }


  /**
   * Retorna a lista de argumentos do método.
   * @return Lista de argumentos do método.
   */
  public List getArgsList() {
    return args;
  }
  
  
  /**
   * Retorna um array com os argumentos do método.
   * @return Array com os argumentos do método.
   */
  public Object[] arguments() {
    return args.toArray();
  }


  /**
   * Define a lista de argumentos do método.
   * @param args Lista de argumentos do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod setArgsList(List args) {
    this.args = args;
    return this;
  }
  
  
  /**
   * Define os argumentos do método.
   * @param objs Argumentos do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod arguments(Object ... objs) {
    if(objs != null && objs.length > 0) {
      args.clear();
      args.addAll(Arrays.asList(objs));
    }
    return this;
  }


  /**
   * Retorna a classe do tipo de retorno do método.
   * @return classe de tipo de retorno do método.
   */
  public Class getReturnType() {
    return retType;
  }


  /**
   * Define a classe do tipo de retorno do método.
   * @param retType classe de tipo de retorno do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod setReturnType(Class retType) {
    this.retType = retType;
    return this;
  }


  /**
   * Retorna o nome do objeto proprietário do método.
   * @return nome do objeto proprietário do método.
   */
  public String objectName() {
    return objName;
  }


  /**
   * Define o nome do objeto proprietário do método.
   * @param objName nome do objeto proprietário do método.
   * @return Esta instância modificada de RemoteMethod.
   */
  public RemoteMethod forObject(String objName) {
    this.objName = objName;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.mthName);
    hash = 47 * hash + Objects.hashCode(this.objName);
    hash = 47 * hash + Arrays.deepHashCode(this.argTypes);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final RemoteMethod other = (RemoteMethod) obj;
    if (!Objects.equals(this.objName, other.objName))
      return false;
    if (!Objects.equals(this.mthName, other.mthName))
      return false;
    if (!Arrays.deepEquals(this.argTypes, other.argTypes))
      return false;
    return true;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(objName).append(".")
        .append(mthName).append("( ");
    if(!args.isEmpty()) {
      for(int i = 0; i < args.size(); i++) {
        sb.append(args.get(i));
        if(i < args.size() -1)
          sb.append(", ");
      }
    }
    else if(argTypes != null) {
      for(int i = 0; i < argTypes.length; i++) {
        sb.append(argTypes[i].getSimpleName());
        if(i < argTypes.length -1)
          sb.append(", ");
      }
    }
    sb.append(" )");
    if(retType != null)
      sb.append(" : ").append(retType.getCanonicalName());
    
    return sb.toString();
  }
  
}
