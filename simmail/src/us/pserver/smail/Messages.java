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

import us.pserver.smail.filter.Filter;
import us.pserver.smail.internal.MailMessageConverter;
import us.pserver.smail.internal.TypeLinkedList;


/**
 * <p style="font-size: medium;">
 * Coleção de mensagens, suportada
 * por uma lista interna. Possui suporte
 * à filtros de mensagens, para obter
 * somente mensagens novas ou com anexos, por exemplo.
 * </p>
 *
 * @see us.pserver.smail.filter.Filter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class Messages extends TypeLinkedList<Message> {

  
  /**
   * Adiciona uma mensagem à lista.
   * @param m Mensagem a ser adicionada.
   * @return Esta instância modificada de Messages.
   */
  public Messages addMessage(Message m) {
    this.add(m);
    return this;
  }
  
  
  /**
   * Adiciona uma mensagem caso não exista na 
   * lista, convertendo-a para 
   * <code>com.jpower.simplemail.Message</code>.
   * @param mx Mensagem a ser adicionada.
   * @return Esta instância modificada de Messages.
   */
  public Message addNewMessage(javax.mail.Message mx) {
    if(mx == null || this.contains(mx)) 
      return null;
    try {
      Message m = new MailMessageConverter().convert(mx);
      this.addMessage(m);
      return m;
    } catch(SMailException ex) {}
    return null;
  }
  
  
  /**
   * Adiciona a mensagem informada, caso não exista 
   * na lista.
   * @param m Mensagem a ser adicionada.
   * @return Esta instância modificada de Messages.
   */
  public Messages addNewMessage(Message m) {
    if(m == null || this.contains(m))
      return this;
    return this.addMessage(m);
  }
  
  
  /**
   * Retorna um array contendo as mensagens da lista.
   * @return array de mensagens.
   */
  @Override
  public Message[] toArray() {
    if(list.isEmpty()) return null;
    return list.toArray(new Message[list.size()]);
  }
  
  
  /**
   * Passa as mensagens da lista pelo filtro informado,
   * retornando uma coleção contendo somente as
   * mesagens que atendem aos critérios do filtro.
   * @param f Filtro a ser aplicado.
   * @return Uma coleção contendo somente as mensagens
   * que atenderam os critérios do filtro.
   */
  public Messages filter(Filter<Message> f) {
    if(f == null || this.isEmpty()) return this;
    
    Messages res = new Messages();
    for(Message m : list)
      if(f.match(m)) res.add(m);
    
    return res;
  }
  
  
  /**
   * Verifica se a lista contém a mensagem informada.
   * @param m Mensagem a ser verificada.
   * @return <code>true</code> se a mensagem existir
   * na lista, <code>false</code> caso contrário.
   */
  public boolean contains(Message m) {
    if(m == null || this.isEmpty()) 
      return false;
    for(Message n : list)
      if(n.equals(m)) return true;
    return false;
  }
  
  
  /**
   * Verifica se a lista contém a mensagem informada.
   * @param m Mensagem a ser verificada.
   * @return <code>true</code> se a mensagem existir
   * na lista, <code>false</code> caso contrário.
   */
  public boolean contains(javax.mail.Message m) {
    if(m == null || this.isEmpty()) 
      return false;
    for(Message n : list)
      if(n.equals(m)) return true;
    return false;
  }
  
}
