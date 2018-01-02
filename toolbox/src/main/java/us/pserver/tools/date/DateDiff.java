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

package us.pserver.tools.date;

import java.util.Date;

/**
 * DateDiff representa a diferença de tempo entre duas
 * datas.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see us.pserver.tools.date.SimpleDate
 * @version 3.0 - 2011.11.16
 */
public class DateDiff {
	
	private int years;
	private int months;
	private long days;
	private int hours;
	private int mins;
	private int secs;
	private long millis;
	private int lastDayOfMonth;
	private int factor;
	private long total;

	private boolean dst, dst1, dst2;

	private SimpleDate date1;
	private SimpleDate date2;
	
	
	/**
	 * Construtor padrão sem argumentos.
	 */
	public DateDiff() {
		date1 = null;
		date2 = null;
		this.reset();
	}
	
	
	/**
	 * Construtor que recebe as duas datas para cálculo
	 * da diferença de tempo.
	 * @param d1 Primeira data
	 * @param d2 Segunda data
	 */
	public DateDiff(Date d1, Date d2) {
		this();
		date1 = new SimpleDate(d1);
		date2 = new SimpleDate(d2);
    this.calculate();
	}


	private void reset() {
		dst = true;
		dst1 = false;
		dst2 = false;
		years = 0;
		months = 0;
		days = 0;
		hours = 0;
		mins = 0;
		secs = 0;
		millis = 0;
		lastDayOfMonth = 0;
		factor = 1;
	}


	/**
	 * Configura a diferença de horas do cálculo,
	 * considerando ou não o horário de verão.
	 */
	private void configureDST() {
		if(date1 == null || date2 == null) return;
		
    date1.setUseDaylightTime(dst);
    date2.setUseDaylightTime(dst);
	}
	
	
	/**
	 * Define a primeira data utilizada para cálculo 
	 * da diferença.
	 * @param d Primeira data.
	 * @return esta instância de DateDiff modificada.
	 */
	public DateDiff setDate1(Date d) {
		date1 = new SimpleDate(d);
		return this;
	}
	
	
	/**
	 * Define a segunda data utilizada para cálculo 
	 * da diferença.
	 * @param d Segunda data.
	 * @return esta instância de DateDiff modificada.
	 */
	public DateDiff setDate2(Date d) {
		date2 = new SimpleDate(d);
		return this;
	}
	
	
	/**
	 * Retorna a primeira data utilizada no cálculo 
	 * da diferença.
	 * @return Primeira data.
	 */
	public Date date1() {
		return date1;
	}
	
	
	/**
	 * Retorna a segunda data utilizada no cálculo 
	 * da diferença.
	 * @return Segunda data.
	 */
	public Date date2() {
		return date2;
	}


	/**
	 * Retorna a diferença de anos entre as datas.
	 * @return diferença em anos.
	 */
	public int years() {
		return years;
	}


	/**
	 * Retorna a diferença de meses entre as datas.
	 * @return diferença em meses.
	 */
	public int months() {
		return months;
	}


	/**
	 * Retorna a diferença de dias entre as datas.
	 * @return diferença em dias.
	 */
	public int days() {
		return (int) days;
	}


	/**
	 * Retorna a diferença de horas entre as datas.
	 * @return diferença em horas.
	 */
	public int hours() {
		return hours;
	}


	/**
	 * Retorna a diferença de minutos entre as datas.
	 * @return diferença em minutos.
	 */
	public int minutes() {
		return mins;
	}


	/**
	 * Retorna a diferença de segundos entre as datas.
	 * @return diferença em segundos.
	 */
	public int seconds() {
		return secs;
	}


	/**
	 * Retorna a diferença de milisegundos entre as datas.
	 * @return diferença em milisegundos.
	 */
	public long millis() {
		return millis;
	}
	
	
	/**
	 * Calcula a diferença de tempo entre as datas.
   * Este método deve ser chamado a cada alteração
   * nas configurações de DateDiff.
   * @return Esta intância modificada de <code>DateDiff</code>
	 */
	public DateDiff calculate() {
		if(date1 == null || date2 == null) return null;
		
		this.reset();

    SimpleDate old1 = date1, 
        old2 = date2;
    
		//se date2 for antes de date1, inverte
		if(date2.before(date1)) {
			factor = -1;
			SimpleDate temp = date1;
			date1 = date2;
			date2 = temp;
		}
		//calcula a diferença de tempo em milisegundos
		millis = date2.getTime() - date1.getTime();

		//resto dos segundos
		secs = (int) (millis / 1000 % 60);
		//resto dos minutos
		mins = (int) (millis / 1000 / 60 % 60);
		//resto das horas
		hours = (int) (millis / 1000 / 60 / 60 % 24);
		//resto dos dias
		days = (millis / 1000 / 60 / 60 / 24);
		//resto dos milisegundos
		millis = millis % 1000;
		
		SimpleDate date = date1.clone();
		lastDayOfMonth = date.lastDayOfMonth();
		
		while(days >= lastDayOfMonth) {
			days -= lastDayOfMonth;
			months++;
			date.addMonth(1);
			lastDayOfMonth = date.lastDayOfMonth();
			if(months >= 12) {
				months = 0;
				years++;
			}
		}
    
    date1 = old1;
    date2 = old2;
    
		return this;
	}
	
	
  /**
   * Retorna a diferença de tempo
   * somente em milisegundos entre as datas.
   * @return Diferença somente em milisegundos
   * entre as datas.
   */
	public long toMillis() {
    if(date1 == null || date2 == null)
      return -1;
    
    long totalMillis = 0;
    if(date1.after(date2))
      totalMillis = date1.getTime() - date2.getTime();
    else if(date1.before(date2))
      totalMillis = date2.getTime() - date1.getTime();
    
    return totalMillis;
	}
  
  
  /**
   * Retorna a diferença de tempo
   * somente em segundos entre as datas.
   * @return Diferença somente em segundos
   * entre as datas.
   */
  public long toSeconds() {
    long ml = this.toMillis();
    if(ml <= 0) return ml;
    
    return ml / 1000;
  }
  
  
  /**
   * Retorna a diferença de tempo
   * somente em minutos entre as datas.
   * @return Diferença somente em minutos
   * entre as datas.
   */
  public long toMinutes() {
    long sec = this.toSeconds();
    if(sec <= 0) return sec;
    
    return sec / 60;
  }


  /**
   * Retorna a diferença de tempo
   * somente em horas entre as datas.
   * @return Diferença somente em horas
   * entre as datas.
   */
  public long toHours() {
    long min = this.toMinutes();
    if(min <= 0) return min;
    
    return min / 60;
  }


  /**
   * Retorna a diferença de tempo
   * somente em dias entre as datas.
   * @return Diferença somente em dias
   * entre as datas.
   */
  public long toDays() {
    long hrs = this.toHours();
    if(hrs <= 0) return hrs;
    
    return hrs / 24;
  }


  /**
   * Retorna a diferença de tempo somente
   * em meses entre as datas.
   * @return Diferença somente em meses
   * entre as datas.
   */
  public int toMonths() {
    if(date1 == null || date2 == null)
      return -1;
    
    this.calculate();
    return this.months() + this.years() * 12;
  }


	/**
	 * Configura se se o horário de verão
	 * deverá ser considerado no cálculo da diferença
	 * de tempo entre as datas.
	 * Por exemplo, com a data1 em <code>01/09/2011 20:00:00</code>
	 * e a data2 em <code>01/11/2011 22:00:00</code>, se
	 * <code>useDaylightTime()</code> estiver configurado,
	 * a diferença de tempo será de <code>2 meses e 1 hora</code>,
	 * porque a data2 está dentro do horário de verão.
	 * @param use usar o horário de verão no cálculo da diferença.
	 * @return a instância modificada de DateDiff.
	 */
	public DateDiff setUseDaylightTime(boolean use) {
		dst = use;
		this.configureDST();
		return this;
	}


	/**
	 * Retorna <code>true</code> se o horário de verão
	 * deverá ser considerado no cálculo da diferença
	 * de tempo entre as datas.
	 * @return <code>true</code> se o horário de verão
	 * será considerado no cálculo.
	 */
	public boolean useDaylightTime() {
		return dst;
	}


	@Override
	public String toString() {
		String s = "";
		if(years != 0) s += years + " year(s), ";
		if(months != 0) s += months + " month(s), ";
		if(days != 0) s += days + " day(s), ";

		s += hours + ":" + mins + ":" + secs + "." + millis;
		
		if(factor < 0) {
			s = s.replaceAll("-", "");
			s = "-" + s;
		}
		
		return s;
	}

}
