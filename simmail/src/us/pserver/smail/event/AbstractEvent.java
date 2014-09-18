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

package us.pserver.smail.event;


/**
 * <p style="font-size: medium;">
 * Superclasse abstrata que implementa algumas
 * funcionalidades comuns à eventos.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public abstract class AbstractEvent<S> {
  
  protected long when;
  
  protected S source;
  
  
  /**
   * Construtor padrao sem argumentos.
   */
  protected AbstractEvent() {
    when = System.currentTimeMillis();
    source = null;
  }
  
  
  /**
   * Construtor que recebe o objeto gerador do evento.
   * @param source Fonte do evento.
   */
  protected AbstractEvent(S source) {
    this();
    this.source = source;
  }
  
  
  /**
   * Retorna o objeto gerador do evento.
   * @return Objeto gerador do evento.
   */
  public S getSource() {
    return source;
  }


  /**
   * Define o objeto gerador do evento.
   * @param source Objeto gerador do evento.
   */
  public void setSource(S source) {
    this.source = source;
  }


  /**
   * Retorna o instante em que o evento foi gerado.
   * @return instante em milisegundos em que o 
   * evento foi gerado.
   */
  public long getWhen() {
    return when;
  }


  /**
   * Define o instante em milisegundos em que o evento 
   * foi gerado.
   * @param when instante em milisegundos em que o 
   * evento foi gerado.
   */
  public void setWhen(long when) {
    this.when = when;
  }
  
}
