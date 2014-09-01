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

import us.pserver.smail.Message;
import java.util.Date;


/**
 * <p style="font-size: medium;">
 * Filtro para pesquisa de mensagens por data de expedição.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class DateMessageFilter implements Filter<Message> {

  
  /**
   * Método de comparação da data de envio de mensagens.
   */
  public static enum COMPARE {
    EQUALS, BEFORE, AFTER, BETWEEN;
  }
  
  
  /**
   * Método de comparação da data de envio de mensagens.
   */
  public static final COMPARE
      EQUALS = COMPARE.EQUALS,
      BEFORE = COMPARE.BEFORE,
      AFTER = COMPARE.AFTER,
      BETWEEN = COMPARE.BETWEEN;
  
  
  private COMPARE method = COMPARE.EQUALS;
  
  private Date d1, d2;
  
  
  /**
   * Construtor padrão que recebe uma data
   * para comparação com a data de expedição da
   * mensagem de email.
   * @param model Data a ser comparada com a data 
   * da mensagem.
   */
  public DateMessageFilter(Date model) {
    d1 = model;
    d2 = null;
  }
  
  
  /**
   * Construtor que recebe a data e o
   * método de comparação com a data de
   * expedição da mensagem.
   * @param model Data a ser comparada com a data 
   * da mensagem.
   * @param m Método de comparação da data.
   */
  public DateMessageFilter(COMPARE m, Date model) {
    this(model);
    this.compareMethod(m);
  }
  
  
  /**
   * Define a primeira data de comparação.
   * @param d Data para comparação.
   * @return Esta instância modificada de DateMessageFilter.
   */
  public DateMessageFilter date1(Date d) {
    d1 = d;
    return this;
  }
  
  
  /**
   * Retorna a primeira data de comparação.
   * @return Data para comparação.
   */
  public Date date1() {
    return d1;
  }
  
  
  /**
   * Define a segunda data de comparação.
   * @param d Data para comparação.
   * @return Esta instância modificada de DateMessageFilter.
   */
  public DateMessageFilter date2(Date d) {
    d2 = d;
    return this;
  }
  
  
  /**
   * Retorna a sergunda data de comparação.
   * @return Data para comparação.
   */
  public Date date2() {
    return d2;
  }
  
  
  /**
   * Define o Método de comparação da data de expedição da mensagem.
   * @param cm Método de comparação da data.
   * @return Esta instância modificada de DateMessageFilter.
   */
  public DateMessageFilter compareMethod(COMPARE cm) {
    method = cm;
    return this;
  }
  
  
  /**
   * Retorna o Método de comparação da data de expedição da mensagem.
   * @return Esta instância modificada de DateMessageFilter.
   */
  public COMPARE compareMethod() {
    return method;
  }
  
  
  @Override
  public boolean match(Message e) {
    if(e == null || d1 == null) return false;
    
    Date md = e.getSentDate();
    
    boolean compare = false;
    if(method == COMPARE.EQUALS)
      compare = d1.equals(md);
    else if(method == COMPARE.AFTER)
      compare = md.after(d1);
    else if(method == COMPARE.BEFORE)
      compare = md.before(d1);
    else
      if(d2 == null) compare = false;
      else compare = md.after(d1) && md.before(d2);
    
    return compare;
  }
  
}
