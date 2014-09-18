/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package us.pserver.smail;

import us.pserver.smail.internal.EmbeddedObject;
import us.pserver.smail.internal.TypeLinkedList;


/**
 * <p style="font-size: medium;">
 * Coleção de objetos embarcados (<code>EmbeddedObject</code>) 
 * na mensagem.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class Attachments extends TypeLinkedList<EmbeddedObject> {
  
  
  /**
   * Retorna um array contendo todos os 
   * objetos da coleção.
   * @return array contendo os objetos da coleção.
   */
  @Override
  public EmbeddedObject[] toArray() {
    if(this.isEmpty()) return null;
    EmbeddedObject[] ats = new EmbeddedObject[list.size()];
    return list.toArray(ats);
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o == null || !(o instanceof Attachments))
      return false;
    return this.hashCode() == o.hashCode();
  }
  
  
  @Override
  public String toString() {
    if(list.isEmpty()) return "Attachments: 0";
    StringBuilder s = new StringBuilder();
    for(EmbeddedObject o : list) {
      s.append("     "); s.append(o.getType());
      s.append(": "); s.append(o.getName());
      s.append("\n");
    }
    return s.toString();
  }


  @Override
  public int hashCode() {
    return list.hashCode();
  }
  
}