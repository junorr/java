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

package us.pserver.xprops;

/**
 * Represents a XML unit (element).
 * 
 * @author Juno Roesler - juno@pserver.us
 */
public interface XUnit {
  
  /**
   * Get the id of this unit.
   * @return XID
   */
  public XID id();
  
  /**
   * Set the id of this unit.
   * @param id XID
   * @return This modified instance of XUnit.
   */
  public XUnit setID(XID id);
  
  /**
   * Return the XML string representation of this unit.
   * @return String.
   */
  public String toXml();
  
  /**
   * Return the value of this unit.
   * @return String
   */
  public String value();
  
  /**
   * Get a XValue instance of this unit value.
   * @return XValue
   */
  public XValue xvalue();
  
}
