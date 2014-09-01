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
import us.pserver.smail.Message;


/**
 * <p style="font-size: medium;">
 * Filtro de assunto da mensagem.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class SubjectFilter implements Filter<Message> {

  private String subject;
  
  /**
   * Construtor padrão que recebe o assunto de
   * email a ser pesquisado.
   */
  public SubjectFilter(String subject) {
    if(subject == null || subject.trim().equals(""))
      throw new IllegalArgumentException("Invalid subject to search");
    this.subject = subject;
  }
  
  
  /**
   * Define o assunto a ser pesquisado no email.
   * @param subject Assunto do email.
   * @return Esta instância modificada de SubjectFilter.
   */
  public SubjectFilter setSubject(String subject) {
    if(subject != null && !subject.trim().equals(""))
      this.subject = subject;
    return this;
  }
  
  
  /**
   * Retorna o assunto a ser pesquisado no email.
   * @return Assunto do email.
   */
  public String getSubject() {
    return this.subject;
  }
  
  
  /**
   * Verifica se o título da mensagem informada
   * corresponde ao titulo da mensagem modelo.
   * @param e Mensagem pesquisada.
   * @return <code>true</code> se o título da
   * mensagem informada corresponde ao título
   * da mensagem modelo, <code>false</code>
   * caso contrário.
   */
  @Override
  public boolean match(Message e) {
    if(e == null || e.getSubject() == null)
      return false;
    return Search.search(subject, e.getSubject());
  }
  
}
