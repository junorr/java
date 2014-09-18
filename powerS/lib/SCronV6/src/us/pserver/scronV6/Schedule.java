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

package us.pserver.scronV6;

import us.pserver.scronV6.hide.MonthRepeater;
import us.pserver.scronV6.hide.SecondRepeater;
import us.pserver.scronV6.hide.YearRepeater;
import us.pserver.scronV6.hide.Repeater;
import us.pserver.scronV6.hide.HourRepeater;
import us.pserver.scronV6.hide.MillisRepeater;
import us.pserver.scronV6.hide.DayRepeater;
import us.pserver.scronV6.hide.MinuteRepeater;
import java.util.Objects;
import us.pserver.date.SimpleDate;

/**
 * <b>Schedule</b> representa um agendamento futuro
 * com funções para configurar repetições sucessivas por 
 * determinados períodos de tempo.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 */
public class Schedule {

  private SimpleDate date;
  
  private Repeater rept;
  
  private int amount;
  
  private long delay;
  
  
  /**
   * Construtor padrão sem argumentos,
   * constrói um <code>Schedule</code> inválido
   * que deve ser configurado.
   */
  public Schedule() {
    date = SimpleDate.now();
    rept = null;
    amount = 0;
    delay = -1;
  }
  
  
  @Override
  public Schedule clone() {
    Schedule s = new Schedule();
    s.date = date.clone();
    s.rept = rept;
    s.amount = amount;
    s.delay = delay;
    return s;
  }
  
  
  /**
   * Retorna a data interna que representa o momento
   * de execução deste agendamento.
   * @return Data <code>SimpleDate</code>.
   * @see us.pserver.date.SimpleDate
   */
  public SimpleDate getDate() {
    return date;
  }
  
  
  /**
   * Define a data interna que representa o momento
   * de execução deste agendamento.
   * @param sd Data <code>SimpleDate</code>.
   * @return Esta instância modificada de <code>Schedule</code>.
   * @see us.pserver.date.SimpleDate
   */
  public Schedule setDate(SimpleDate sd) {
    if(sd != null) {
      date = sd;
      delay = -1;
    }
    return this;
  }
  
  
  /**
   * Retorna o objeto responsável por ajustar
   * a repetição na data de agendamento.
   * @return <code>Repeater</code>.
   * @see us.pserver.scronV6.hide.Repeater
   */
  public Repeater getRepeater() {
    return rept;
  }
  
  
  /**
   * Retorna a quantidade específica de tempo
   * em que <code>Schedule</code> irá repetir 
   * o evento agendado.
   * @return <code>int</code>.
   */
  public int getRepeatValue() {
    return amount;
  }
  
  
  /**
   * Agenda este <code>Schedule</code> para início imediato.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule startNow() {
    delay = 0;
    return this;
  }
  
  
  /**
   * Agenda <code>Schedule</code> para a data informada.
   * @param sd Data de agendamento.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule startAt(SimpleDate sd) {
    return this.setDate(sd);
  }
  
  
  /**
   * Agenda <code>Schedule</code> para iniciar após
   * o período em milisegundos informado.
   * @param millis Período em milisegundos.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule startDelayed(int millis) {
    if(millis > 0) {
      delay = millis;
      date = new MillisRepeater().set(date, millis);
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> milisegundos.
   * @param n Período em milisegundos.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInMillis(int n) {
    if(n > 0) {
      rept = new MillisRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> segundos.
   * @param n Período em segundos.
   * @return Esta instância modificada de <code>Schedule</code>e.
   */
  public Schedule repeatInSeconds(int n) {
    if(n > 0) {
      rept = new SecondRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> minutos.
   * @param n Período em minutos.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInMinutes(int n) {
    if(n > 0) {
      rept = new MinuteRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> horas.
   * @param n Período em horas.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInHours(int n) {
    if(n > 0) {
      rept = new HourRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> dias.
   * @param n Período em dias.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInDays(int n) {
    if(n > 0) {
      rept = new DayRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> meses.
   * @param n Período em meses.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInMonths(int n) {
    if(n > 0) {
      rept = new MonthRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Programa <code>Schedule</code> para repetição em 
   * <code>n</code> anos.
   * @param n Período em anos.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule repeatInYears(int n) {
    if(n > 0) {
      rept = new YearRepeater();
      amount = n;
    }
    return this;
  }
  
  
  /**
   * Verifica se a repetição está definida para
   * este agendamento.
   * @return <code>true</code> se a repetição
   * foi configurada para este Schedule,
   * <code>false</code> caso contrário.
   */
  public boolean isRepeatEnabled() {
    return rept != null && amount > 0;
  }
  
  
  /**
   * Desabilita a repetição deste <code>Schedule</code>
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule disableRepeat() {
    rept = null;
    amount = 0;
    return this;
  }
  
  
  /**
   * Verifica se este agendamento é válido,
   * ou seja, se está agendado para um momento
   * futuro ou possui uma repetição programada.
   * @return <code>true</code> se este <code>Schedule</code>
   * é válido, <code>false</code> caso contrário.
   */
  public boolean isValid() {
    return delay >= 0 
        || (date.getTime() 
        - System.currentTimeMillis() >= 0) 
        || isRepeatEnabled();
  }
  
  
  /**
   * Verifica se o tempo restante para início 
   * do agendamento está entre os dois valores informados.
   * @param min Tempo mínimo em milisegundos.
   * @param max Tempo máximo em milisegundos.
   * @return <code>true</code> se o tempo restante
   * estiver entre os dois valores informados,
   * <code>false</code> caso contrário.
   * @see us.pserver.scronV6.Schedule#getCountdown() 
   */
  public boolean isCountdownBeteewn(int min, int max) {
    long cd = this.getCountdown();
    return cd >= min && cd <= max;
  }
  
  
  /**
   * Invalida esta instância de <code>Schedule</code>,
   * desabilitando seu agendamento até que seja reagendado, 
   * se reagendamento estiver habilitado.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule invalidate() {
    date = SimpleDate.now();
    rept = null;
    amount = 0;
    delay = -1;
    return this;
  }
  
  
  /**
   * Retorna o tempo restante em milisegundos
   * para início do agendamento.
   * @return Tempo em milisegundos.
   */
  public long getCountdown() {
    if(!isValid()) 
      return -1;
    else if(delay >= 0) 
      return delay;
    else if(date != null)
      return date.getTime() 
          - System.currentTimeMillis();
    else 
      return -1;
  }
  
  
  /**
   * Reagenda este <code>Schedule</code>, caso possua 
   * repetição programada.
   * @return Esta instância modificada de <code>Schedule</code>.
   */
  public Schedule reeschedule() {
    if(isRepeatEnabled()) {
      delay = -1;
      SimpleDate now = SimpleDate.now();
      do date = rept.set(date, amount); 
      while(date.before(now));
    }
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.date);
    hash = 97 * hash + Objects.hashCode(this.delay);
    hash = 97 * hash + Objects.hashCode(this.rept);
    hash = 97 * hash + this.amount;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Schedule other = (Schedule) obj;
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.rept, other.rept)) {
      return false;
    }
    if (this.delay != other.delay) {
      return false;
    }
    if (this.amount != other.amount) {
      return false;
    }
    return true;
  }
  
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    SimpleDate sd = date;
    if(sd == null) sd = new SimpleDate();
    sb.append(sd.format(
          SimpleDate.DDMMYYYY_SLASH))
        .append(" ")
        .append(sd.format(
          SimpleDate.HHMMSSNNN))
        .append(" [Repeat: ");
    
    if(rept == null)
      sb.append("disabled");
    else
      sb.append(String.valueOf(amount))
          .append(" ").append(rept.name());
    
    return sb.append("]").toString();
  }
  
}
