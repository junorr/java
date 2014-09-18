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
package com.jpower.nnet;

import java.lang.reflect.Constructor;


/**
 * Fábrica de Handlers por classes, instanciando 
 * os objetos através da classe fornecida.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-20
 */
public class ClassHandlerFactory implements ConnectionHandlerFactory {
  
  private Class<? extends ConnectionHandler> cls;
  
  /**
   * Construtor padrão, recebe a classe herdeira de 
   * <code>ConnectionHandler</code>, a partir da qual criará
   * novos handlers de conexão.
   * @param c Classe herdeira de <code>ConnectionHandler</code>
   */
  public ClassHandlerFactory(Class<? extends ConnectionHandler> c) {
    if(c == null)
      throw new IllegalArgumentException(
          "Invalid Class: "+ c);
    
    Constructor[] cs = c.getDeclaredConstructors();
    if(cs == null || cs.length == 0)
      throw new IllegalArgumentException(
          "Invalid Class. No arguments "
          + "constructor required.");
    
    boolean argless = false;
    for(int i = 0; i < cs.length; i++) {
      argless = cs[i].getParameterTypes().length == 0;
      if(argless) break;
    }
    
    if(!argless) 
      throw new IllegalArgumentException(
          "Invalid Class. No arguments "
          + "constructor required.");
    
    cls = c;
  }
  
  
  /**
   * Cria um novo handler de conexão, com base na classe 
   * fornacida no construtor.
   * @return <code>ConnectionHandler</code>.
   */
  @Override
  public ConnectionHandler create() {
    try {
      return cls.newInstance();
    } catch(Exception ex) {
      return null;
    }
  }
  
}
