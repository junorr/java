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

package us.pserver.scronv6.repeat;

import java.util.LinkedList;
import java.util.List;
import us.pserver.conc.ExclusiveList;

/**
 * <b>JobsManager</b> encapsula uma <code>List&lt;Pair&gt;</code>, 
 * externalizando funções absolutamente seguras para
 * ambientes multithread. <b>JobsManager</b> é utilizada por
 * <code>SCronV6</code> para armazenamento, organização
 * e agendamento de execução de <code>JobsManager e Schedules</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 * @see us.pserver.scronv6.repeat.Pair
 */
public class JobsManager {

  private final ExclusiveList<Pair> list;
  
 /**
  * Construtor padrão sem argumentos;
  */
  public JobsManager() {
    list = new ExclusiveList();
  }
  
  
  /**
   * Retorna a lista de trabalhos.
   * @return <code>ExclusiveList&lt;Pair&gt;</code>.
   */
  public ExclusiveList<Pair> list() {
    return list;
  }
  
  
  /**
   * Retorna a lista de trabalhos.
   * @return <code>List&lt;Pair&gt;</code>.
   */
  public List<Pair> getList() {
    return list;
  }
  
  
  /**
   * Compara dois objetos <code>Pair</code> para
   * fins de ordenação, levando em consideração apenas
   * o agendamento <code>Schedule</code>.
   * @param p1 <code>Pair</code> a ser comparado.
   * @param p2 <code>Pair</code> a ser comparado.
   * @return <code>0</code> se os dois objetos forem iguais,
   * <code>-1</code> se <code>p1.schedule</code> for menor
   * do que <code>p2.schedule</code>, <code>1</code> caso contrário.
   */
  public int compare(Pair p1, Pair p2) {
    if(p1 == null || p2 == null
        || p1.schedule() == null
        || p2.schedule() == null)
      return 0;
    else if(p1.schedule().getCountdown() 
        < p2.schedule().getCountdown())
      return -1;
    else if(p1.schedule().getCountdown() 
        > p2.schedule().getCountdown())
      return 1;
    else return 0;
  }
  
  
  /**
   * Ordena a lista por ordem de ocorrência dos 
   * agendamentos <code>Schedule</code>.
   * @return Esta instância modificada de <code>JobsManager</code>.
   */
  public synchronized JobsManager sort() {
    if(list.isEmpty()) return this;
    try {
      list.sort(this::compare);
    } catch(Exception e) {}
    return this;
  }
  
  
  /**
   * Remove todos os agendamentos inválidos.
   * @return Esta instância modificada de <code>JobsManager</code>.
   */
  public synchronized JobsManager removeInvalids() {
    if(list.isEmpty()) return this;
    List<Pair> l = new LinkedList();
    list.stream()
        .filter(p -> p.schedule().isValid())
        .forEach(l::add);
    list.clear();
    list.addAll(l);
    l.clear();
    l = null;
    return this;
  }
  
}
