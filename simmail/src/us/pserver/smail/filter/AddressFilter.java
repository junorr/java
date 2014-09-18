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

package us.pserver.smail.filter;

import com.jpower.jst.Search;
import com.jpower.jst.SearchList;
import us.pserver.smail.Message;
import java.util.Arrays;


/**
 * <p style="font-size: medium;">
 * Filtro para pesquisa de endereço da mensagem.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class AddressFilter implements Filter<Message> {
  
  
  /**
   * Tipos de endereços do email.
   */
  public static enum ADDRESS {
    FROM, TO, CC, BCC
  }
  
  
  /**
   * Tipo de endereçamento de email.
   */
  public static final ADDRESS
      FROM = ADDRESS.FROM,
      TO = ADDRESS.TO,
      CC = ADDRESS.CC,
      BCC = ADDRESS.BCC;
  
  
  private ADDRESS type = ADDRESS.TO;
  
  
  private String address;
  
  
  /**
   * Construtor padrão recebe o endereço ou nome do 
   * destinatário de email a ser pesquisado na mensagem.
   * @param address Endereço ou nome do destinatário a
   * ser pesquisado.
   */
  public AddressFilter(String address) {
    if(address == null || address.trim().equals(""))
      throw new IllegalArgumentException("Invalid address/name to search");
    this.address = address;
  }
  
  
  /**
   * Construtor que recebe o tipo de endereço 
   * a ser pesquisado e a mensagem modelo que 
   * servirá de parâmetro para a pesquisa.
   * @param ad Tipo de endereço.
   * @param address Endereço a ser pesquisado.
   */
  public AddressFilter(ADDRESS ad, String address) {
    this(address);
    this.setAddressType(ad);
  }
  
  
  /**
   * Define o tipo de endereço de email a ser pesquisado.
   * @param ad Tipo de endereço a ser pesquisado.
   * @return Esta instância modificada de AddressFilter.
   */
  public AddressFilter setAddressType(ADDRESS ad) {
    if(ad != null)
      type = ad;
    return this;
  }
  
  
  /**
   * Verifica se a mensagem informada corresponde aos
   * parâmetros do filtro.
   * @param e Mensagem a ser comparada.
   * @return <code>true</code> se a mensagem corresponde
   * aos parâmetros do filtro, <code>false</code> 
   * caso contrário.
   */
  @Override
  public boolean match(Message e) {
    if(type == ADDRESS.FROM)
      return this.compareFrom(e);
    else if(type == ADDRESS.TO)
      return this.compareTO(e);
    else if(type == ADDRESS.CC)
      return this.compareCC(e);
    else if(type == ADDRESS.BCC)
      return this.compareBCC(e);
    else return false;
  }
  
  
  /**
   * Compara o remetente da mensagem.
   * @param m Mensagem a ser comparada.
   * @return <code>true</code> se o endereço
   * corresponde ao filtro, <code>false</code>
   * caso contrário.
   */
  private boolean compareFrom(Message m) {
    if(address == null || m == null) return false;
    return Search.search(address, m.from());
  }
  
  
  /**
   * Compara o destinatário da mensagem.
   * @param m Mensagem a ser comparada.
   * @return <code>true</code> se o endereço
   * corresponde ao filtro, <code>false</code>
   * caso contrário.
   */
  public boolean compareTO(Message m) {
    if(address == null || m == null) return false;
    SearchList sl = new SearchList(
        Arrays.asList(m.to().toArray()));
    return sl.searchFirst(address) != null;
  }
  
  
  /**
   * Compara o destinatário da mensagem.
   * @param m Mensagem a ser comparada.
   * @return <code>true</code> se o endereço
   * corresponde ao filtro, <code>false</code>
   * caso contrário.
   */
  public boolean compareCC(Message m) {
    if(address == null || m == null) return false;
    SearchList sl = new SearchList(
        Arrays.asList(m.cc().toArray()));
    return sl.searchFirst(address) != null;
  }
  
  
  /**
   * Compara o destinatário da mensagem.
   * @param m Mensagem a ser comparada.
   * @return <code>true</code> se o endereço
   * corresponde ao filtro, <code>false</code>
   * caso contrário.
   */
  public boolean compareBCC(Message m) {
    if(address == null || m == null) return false;
    SearchList sl = new SearchList(
        Arrays.asList(m.bcc().toArray()));
    return sl.searchFirst(address) != null;
  }
  
}
