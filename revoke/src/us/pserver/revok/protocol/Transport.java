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

package us.pserver.revok.protocol;

import java.io.InputStream;


/**
 * <code>Transport</code> é o objeto padrão
 * utilizado na transmissão de informações pelo 
 * canal de comunicação. Encapsula um objeto
 * embarcado e opcionalmente um <code>InputStream</code>, 
 * no caso de dados a serem transmitidos via stream.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class Transport {
  
  private Object object;
  
  private InputStream input;
  
  private boolean hasContentEmbedded;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Transport() {
    object = null;
    input = null;
    hasContentEmbedded = false;
  }
  
  
  /**
   * Construtor que recebe o objeto a ser embarcado.
   * @param obj Objeto a ser embarcado.
   */
  public Transport(Object obj) {
    object = obj;
    input = null;
    hasContentEmbedded = false;
  }
  
  
  /**
   * Construtor que recebe o objeto a ser embarcado
   * e um <code>InputStream</code> com dados a serem
   * transmitidos via stream.
   * @param obj Objeto a ser embarcado.
   * @param input <code>InputStream</code>.
   */
  public Transport(Object obj, InputStream input) {
    object = obj;
    setInputStream(input);
  }
  
  
  /**
   * Retorna um objeto clonado de <code>Transport</code>
   * pronto para ser enviado pelo canal.
   * @return cópia de <code>Transport</code>
   */
  public Transport getWriteVersion() {
    Transport t = new Transport();
    t.object = object;
    t.hasContentEmbedded = hasContentEmbedded;
    return t;
  }


  /**
   * Retorna o objeto embarcado.
   * @return <code>Object</code>
   * ou <code>null</code>.
   */
  public Object getObject() {
    return object;
  }


  /**
   * Define o objeto a ser embarcado.
   * @param object <code>Object</code>
   * @return Esta instância modificada de 
   * <code>Transport</code>.
   */
  public Transport setObject(Object object) {
    this.object = object;
    return this;
  }


  /**
   * Retorna o <code>InputStream</code> de
   * dados a serem transmitidos via stream.
   * @return <code>InputStream</code>.
   */
  public InputStream getInputStream() {
    return input;
  }
  
  
  /**
   * Verifica se existe um <code>InputStream</code>
   * definido para transmissão de dados via stream.
   * @return <code>true</code> se foi definido um
   * <code>InputStream</code> para transmissão de dados 
   * via stream, <code>false</code> caso contrário.
   */
  public boolean hasContentEmbedded() {
    return hasContentEmbedded;
  }


  /**
   * Define o <code>InputStream</code> de
   * dados a serem transmitidos via stream.
   * @param in <code>InputStream</code>.
   * @return Esta instância modificada de <code>InputStream</code>.
   */
  public Transport setInputStream(InputStream in) {
    this.input = in;
    hasContentEmbedded = (in != null);
    return this;
  }
  
  
  /**
   * Verifica se o objeto embarcado pertence à classe 
   * especificada.
   * @param cls Classe a ser comparada com o tipo do 
   * objeto embarcado.
   * @return <code>true</code> se o objeto embarcado
   * pertencer à classe especificada, <code>false</code>
   * caso contrário.
   */
  public boolean isObjectFromType(Class cls) {
    if(object == null || cls == null)
      return false;
    return cls.isAssignableFrom(object.getClass());
  }
  
  
  /**
   * Converte o objeto embarcado para o tipo específico <code>T</code>.
   * @param <T> Tipo ao qual o objeto será convertido.
   * @return o objeto convertido para o tipo <code>T</code>
   * ou <code>null</code> caso ocorra erro 
   * (<code>ClassCastException</code>) na conversão.
   */
  public <T> T castObject() {
    try {
      return (T) object;
    } catch(Exception e) {
      return null;
    }
  }


  @Override
  public String toString() {
    return "Transport{" + "object=" + object + ", hasContentEmbedded=" + hasContentEmbedded + '}';
  }
  
}
