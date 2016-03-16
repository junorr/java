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

package us.pserver.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SimpleDate armazena e manipula datas
 * de forma simples e compatível com 
 * <code>java.util.Date e java.util.Calendar</code>.
 * <br><br>
 * Exemplos de uso de <code>SimpleDate</code>:
 * <br><br>
 * 
 * <span style="color: lightblue;">
 * <code>
 * //Criação de SimpleDate com a data/hora atual
 * </code>
 * </span>
 * 
 * <pre>
 * SimpleDate agora = new SimpleDate();</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//ou</code>
 * </span>
 * 
 * <pre>
 * agora = SimpleDate.now();</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Conversão de String para data/hora</code>
 * </span>
 * 
 * <pre>
 * agora = SimpleDate.parseDate("16/11/2011");
 * agora = SimpleDate.parseDate("2011/11/16 13:50:00");
 * 
 * agora = SimpleDate.parseDate("2011-11-16 13:50:00.100", SimpleDate.SQL_DATE_TIME);
 * agora = SimpleDate.parseDate("Quarta-feira, 16 de Novembro de 2011, 13:50:00");</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Adicionar/Subtrair valores aos campos da data</code>
 * </span>
 * 
 * <pre>
 * agora.addDay(5);
 * agora.addMonth(2);
 * agora.subHour(22);</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Comparação com outras datas</code>
 * </span>
 * 
 * <pre>
 * java.util.Date d = new java.util.Date();
 * agora.before(d);
 * agora.after(d);
 * agora.equals(d);
 * agora.equalsLess(d);
 * agora.equalsDate(d);
 * agora.isOnTime();
 * agora.isPast();
 * agora.isFuture();</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Definição de campos de data/hora</code>
 * </span>
 * 
 * <pre>
 * agora.year(2012);
 * agora.setDay(1);
 * agora.month(3); //equivalente a setMonth(3);
 * agora.date(d);
 * agora.setDate(2012, 5, 21, 13, 50, 0, 100);
 * agora.date(2011, 11, 16, 13);</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Recuperação dos dados da data/hora</code>
 * </span>
 * 
 * <pre>
 * int d = agora.day();
 * d = agora.getDay();
 * agora.setMonth( (agora.month() + 1) );
 * int sec = agora.getSecond();</pre>
 * 
 * <span style="color: lightblue;">
 * <code>//Compatibilidade</code>
 * </span>
 * 
 * <pre>
 * java.sql.Date sqldate = agora.toSqlDate();
 * java.sql.Timestamp timestamp = agora.toSqlTimestamp();
 * String sqlFormat = agora.toSqlString();
 * String timestampFormat = agora.toSqlTimeString();
 * 
 * agora.setDate(sqldate);
 * 
 * agora = SimpleDate.from(timestamp);
 * 
 * agora.setNow();</pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/util/Date.html">java.util.Date</a>
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/util/Calendar.html">java.util.Calendar</a>
 * @version 3.0 - 2011.11.16
 */
public class SimpleDate extends Date {
	
	/**
	 * String scape to format (DD/MM/YYYY).
	 */
	public static final String DDMMYYYY_SLASH = "dd/MM/yyyy";

	/**
	 * String scape to format (DD.MM.YYYY).
	 */
	public static final String DDMMYYYY_DOT = "dd.MM.yyyy";

	/**
	 * String scape to format (DD-MM-YYYY).
	 */
	public static final String DDMMYYYY_DASH = "dd-MM-yyyy";

	/**
	 * String scape to format (DD/MM/YYYY hh:mm:ss).
	 */
	public static final String DDMMYYYY_HHMMSS_SLASH = "dd/MM/yyyy HH:mm:ss";

	/**
	 * String scape to format (hh:mm:ss.SSS).
	 */
	public static final String HHMMSSNNN = "HH:mm:ss.SSS";
	
	/**
	 * String scape to format (DD.MM.YYYY hh:mm:ss).
	 */
	public static final String DDMMYYYY_HHMMSS_DOT = "dd.MM.yyyy HH:mm:ss";

	/**
	 * String scape to format (DD-MM-YYYY hh:mm:ss).
	 */
	public static final String DDMMYYYY_HHMMSS_DASH = "dd-MM-yyyy HH:mm:ss";
	
	/**
	 * String scape to format (YYYY/MM/DD).
	 */
	public static final String YYYYMMDD_SLASH = "yyyy/MM/dd";

	/**
	 * String scape to format (YYYY.MM.DD).
	 */
	public static final String YYYYMMDD_DOT = "yyyy.MM.dd";

	/**
	 * String scape to format (YYYY-MM-DD).
	 */
	public static final String YYYYMMDD_DASH = "yyyy-MM-dd";

	/**
	 * String scape to format (YYYY/MM/DD hh:mm:ss).
	 */
	public static final String YYYYMMDD_HHMMSS_SLASH = "yyyy/MM/dd HH:mm:ss";

	/**
	 * String scape to format (YYYY.MM.DD hh:mm:ss).
	 */
	public static final String YYYYMMDD_HHMMSS_DOT = "yyyy.MM.dd HH:mm:ss";

	/**
	 * String scape to format (YYYY-MM-DD hh:mm:ss).
	 */
	public static final String YYYYMMDD_HHMMSS_DASH = "yyyy-MM-dd HH:mm:ss";

	/**
	 * String scape to format (EEEE, dd 'de' MMMM 'de' yyyy).
	 */
	public static final String LONG_DATE = "EEEE, dd 'de' MMMM 'de' yyyy";

	/**
	 * String scape to format (EEEE, dd 'de' MMMM 'de' yyyy, HH:mm:ss).
	 */
	public static final String LONG_DATE_TIME = "EEEE, dd 'de' MMMM 'de' yyyy, HH:mm:ss";
	
	/**
	 * String scape to format (YYYY-MM-DD).
	 */
	public static final String SQL_DATE = "yyyy-MM-dd";
	
	/**
	 * String scape to format (YYYY-MM-DD hh:mm:ss.fffffffff).
	 */
	public static final String SQL_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";

  /**
   * Day of week <code>SUNDAY = 1</code>.
   */
  public static final int SUNDAY = 1;
  
  /**
   * Day of week <code>MONDAY = 2</code>.
   */
  public static final int MONDAY = 2;
  
  /**
   * Day of week <code>TUESDAY = 3</code>.
   */
  public static final int TUESDAY = 3;
  
  /**
   * Day of week <code>WEDNESDAY = 4</code>.
   */
  public static final int WEDNESDAY = 4;
  
  /**
   * Day of week <code>THURSDAY = 5</code>.
   */
  public static final int THURSDAY = 5;
  
  /**
   * Day of week <code>FRIDAY = 6</code>.
   */
  public static final int FRIDAY = 6;
  
  /**
   * Day of week <code>SATURDAY = 7</code>.
   */
  public static final int SATURDAY = 7;
  
	
	private TimeZone tzone;
	
	private Locale loc;
	
	private Calendar cal;
	
	private String format;
	
	
	/**
	 * Construtor padrão, cria uma instância de
	 * SimpleDate configurada com a data/hora atual.
   * @see us.pserver.date.SimpleDate#now() 
	 */
	public SimpleDate() {
		super();
		tzone = TimeZone.getDefault();
		loc = Locale.getDefault();
		cal = Calendar.getInstance(tzone, loc);
		cal.setTime(this);
		format = DDMMYYYY_HHMMSS_SLASH;
	}
	

	/**
	 * Construtor que recebe um objeto <code>java.util.Date</code> 
   * como argumento, criando uma data compatível.
	 * @param d <code>java.util.Date</code> ou <code>null</code>.
	 */
	public SimpleDate(Date d) {
    this();
    if(d != null)
      this.setTime(d.getTime());
	}
  
  
  /**
   * Cria uma instância de <code>SimpleDate</code>
   * definida com a data e hora atual.
   * @return <code>SimpleDate</code> definido com data e hora atual.
   */
  public static SimpleDate now() {
    return from(new Date());
  }
  
  
  /**
   * Configura <code>SimpleDate</code> com a data e hora atual.
   * @return Esta instância modificada de <code>SimpleDate</code> 
   * definida com data e hora atual.
   */
  public SimpleDate setNow() {
    return this.date(new Date());
  }
  
  
	/**
	 * Retorna o dia do mês.
	 * @return dia do mês.
	 */
  @Override
	public int getDay() {
		cal.setTime(this);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	/**
	 * Retorna o mês do ano.
	 * @return mês do ano.
	 */
  @Override
	public int getMonth() {
		cal.setTime(this);
		return cal.get(Calendar.MONTH)+1;
	}
	
	
	/**
	 * Retorna o ano da data.
	 * @return ano.
	 */
  @Override
	public int getYear() {
		cal.setTime(this);
		return cal.get(Calendar.YEAR);
	}
	
	
	/**
	 * Retorna a hora do dia (0-23).
	 * @return hora do dia (0-23).
	 */
	public int getHour() {
		cal.setTime(this);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	
	/**
	 * Retorna os minutos da hora atual.
	 * @return minuto.
	 */
	public int getMinute() {
		cal.setTime(this);
		return cal.get(Calendar.MINUTE);
	}
	
	
	/**
	 * Retorna os segundos da hora atual.
	 * @return segundo.
	 */
	public int getSecond() {
		cal.setTime(this);
		return cal.get(Calendar.SECOND);
	}
	
	
	/**
	 * Retorna a fração de milisegundos do segundo atual.
	 * @return milisegundos.
	 */
	public long getMillis() {
		return this.getTime() % 1000;
	}
  
  
	/**
	 * Retorna o dia do mês.
	 * @return dia do mês.
	 */
	public int day() {
		cal.setTime(this);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	/**
	 * Retorna o dia da semana.
	 * @return dia da semana.
	 */
	public int dayOfWeek() {
		cal.setTime(this);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
  
  
	/**
	 * Retorna o dia da semana.
	 * @return dia da semana.
   * @see us.pserver.date.DayOfWeek
	 */
	public DayOfWeek getDayOfWeek() {
		return DayOfWeek.fromInt(dayOfWeek());
	}
  
  
  /**
   * Verifica se a data atual representa um 
   * dia �til <code>(diferente de sábado e domingo)</code>.
   * @return <code>true</code> se a data atual 
   * não for sábado nem domingo, 
   * <code>false</code> caso contrário.
   */
  public boolean isBusinessDay() {
    int dow = dayOfWeek();
    return Calendar.SUNDAY != dow
        && Calendar.SATURDAY != dow;
  }
  
  
  /**
   * Atualiza a data/hora para o momento atual.
   * @return Esta instância modificada de SimpleDate.
   */
  public SimpleDate update() {
    return this.time(System.currentTimeMillis());
  }
	
	
	/**
	 * Retorna o mês do ano.
	 * @return mês do ano.
	 */
	public int month() {
		cal.setTime(this);
		return cal.get(Calendar.MONTH)+1;
	}
	
	
	/**
	 * Retorna o ano da data.
	 * @return ano.
	 */
	public int year() {
		cal.setTime(this);
		return cal.get(Calendar.YEAR);
	}
	
	
	/**
	 * Retorna a hora do dia (0-23).
	 * @return hora do dia (0-23).
	 */
	public int hour() {
		cal.setTime(this);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	
	/**
	 * Retorna os minutos da hora atual.
	 * @return minuto.
	 */
	public int minute() {
		cal.setTime(this);
		return cal.get(Calendar.MINUTE);
	}
	
	
	/**
	 * Retorna os segundos da hora atual.
	 * @return segundo.
	 */
	public int second() {
		cal.setTime(this);
		return cal.get(Calendar.SECOND);
	}
	
	
	/**
	 * Retorna a fração de milisegundos do segundo atual.
	 * @return milisegundos.
	 */
	public long millis() {
		return this.getTime() % 1000;
	}
	
	
	/**
	 * Define o dia do mês.
   * @param d dia do mês.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate day(int d) {
    this.setDay(d);
		return this;
	}
	
	
	/**
	 * Define o mês do ano.
   * @param m mês do ano.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate month(int m) {
    this.setMonth(m);
		return this;
	}
	
	
	/**
	 * Define o ano da data.
   * @param y ano da data.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate year(int y) {
		this.setYear(y);
		return this;
	}
	
	
	/**
	 * Define a hora do dia (0-23).
   * @param h hora do dia.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate hour(int h) {
		this.setHour(h);
		return this;
	}
	
	
	/**
	 * Define os minutos da hora atual.
   * @param m minuto da hora.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate minute(int m) {
    this.setMinute(m);
		return this;
	}
	
	
	/**
	 * Define os segundos da hora atual.
   * @param s segundo da hora.
	 * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate second(int s) {
		this.setSecond(s);
		return this;
	}
	
	
	/**
	 * Define a fração em milisegundos do segundo atual, 
   * não afetando a definição da data.
   * @param m fração em milisegundos do segundo atual.
   * @return Esta instância modificada de <code>SimpleDate</code>.
	 */
	public SimpleDate millis(int m) {
		this.setMillis(m);
		return this;
	}
  
  
  /**
   * Define a data/hora de acordo com a instância
   * <code>java.util.Date</code> informada.
   * @param d <code>java.util.Date</code>.
   * @return Esta instância modificada de <code>SimpleDate</code>.
   */
  public SimpleDate date(Date d) {
    if(d == null) return this;
    this.setTime(d.getTime());
    return this;
  }
  
  
  /**
   * Define SimpleDate, informando cada elemento da 
   * data/hora na ordem 
   * <code>{ yyyy, MM, dd, hh, mm, ss, nnn }</code>.
   * @param date elementos da data/hora a ser configurada.
   * Mínimo de 1 (definindo apenas o ano) e máximo de 7
   * elementos (definindo até os milisegundos da hora).
   * @return Esta instância modificada de <code>SimpleDate</code>.
   */
  public SimpleDate date(int ... date) {
    this.setDate(date);
    return this;
  }
  
  
  /**
   * Define a data/hora para representar um ponto 
   * no tempo, que é o tempo em milisegundos 
   * decorrido desde 01/01/1970 00:00:00 GMT.
   * @param t milisegundos decorridos desde
   * 01/01/1970 00:00:00 GMT.
   * @return Esta instância modificada de <code>SimpleDate</code>.
   * @see java.util.Date#setTime(long) 
   */
  public SimpleDate time(long t) {
    this.setTime(t);
    return this;
  }
	
	
	/**
	 * Retorna o último dia do mês atual.
	 * @return último dia do mês atual.
	 */
	public int lastDayOfMonth() {
		cal.setTime(this);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	
	/**
	 * Define o dia do mês.
	 * @param d dia do mês.
	 */
	public void setDay(int d) {
		if(d <= 0 || d > 31) return;

		cal.setTime(this);
		cal.set(Calendar.DAY_OF_MONTH, d);
		this.setTime(cal.getTimeInMillis());
	}
	
	
	/**
	 * Define o mês do ano.
	 * @param m mês do ano.
	 */
  @Override
	public void setMonth(int m) {
		if(m < 1 || m > 12) return;
    
    m -= 1;
    cal.setTime(this);
    cal.clear(Calendar.MONTH);
    cal.add(Calendar.MONTH, m);
    this.setTime(cal.getTimeInMillis());
	}
	
	
	/**
	 * Define o ano da data.
	 * @param y ano.
	 */
  @Override
	public void setYear(int y) {
    if(y < 1970) return;
    
		cal.setTime(this);
    cal.clear(Calendar.YEAR);
    y -= cal.get(Calendar.YEAR);
		cal.add(Calendar.YEAR, y);
		this.setTime(cal.getTimeInMillis());
	}
	
	
	/**
	 * Define a hora do dia (0-23).
	 * @param h hora do dia (0-23).
	 */
	public void setHour(int h) {
		if(h < 0 || h > 23) return;

    cal.clear();
		cal.setTime(this);
		cal.set(Calendar.HOUR_OF_DAY, h);
		this.setTime(cal.getTimeInMillis());
	}
	
	
	/**
	 * Define o minuto da hora atual.
	 * @param m minuto.
	 */
	public void setMinute(int m) {
		if(m < 0 || m > 59) return;

    cal.clear();
		cal.setTime(this);
		cal.set(Calendar.MINUTE, m);
		this.setTime(cal.getTimeInMillis());
	}
	
	
	/**
	 * Define o segundo da hora atual.
	 * @param s segundo.
	 */
	public void setSecond(int s) {
		if(s < 0 || s > 59) return;

    cal.clear();
		cal.setTime(this);
		cal.set(Calendar.SECOND, s);
		this.setTime(cal.getTimeInMillis());
	}


	/**
	 * Define a fração em milisegundos do segundo atual, 
   * não afetando a definição da data.
	 * @param m milisegundo.
	 */
	public void setMillis(int m) {
    if(m < 0) return;
    
    cal.clear();
		long total = this.getTime();
    long timeMillis = total % 1000;
    total -= timeMillis;
    total += m;
    this.setTime(total);
    cal.setTime(this);
	}
  
  
  /**
   * Define a data/hora para representar um ponto 
   * no tempo, que é o tempo em milisegundos 
   * decorrido desde 01/01/1970 00:00:00 GMT.
   * @param t milisegundos decorridos desde
   * 01/01/1970 00:00:00 GMT.
   * @see java.util.Date#setTime(long) 
   *
  @Override
  public void setTime(long t) {
    if(t < 0) return;
    super.setTime(t);
    cal.setTime(this);
  }*/
  
  
  /**
   * Define a data/hora de acordo com a instância
   * <code>java.util.Date</code> informada.
   * @param d <code>java.util.Date</code>.
   */
  public void setDate(Date d) {
    if(d == null) return;
    this.setTime(d.getTime());
  }
  
  
  /**
   * Define SimpleDate, informando cada elemento da 
   * data/hora na ordem 
   * <code>{ yyyy, MM, dd, hh, mm, ss, nnn }</code>.
   * @param date elementos da data/hora a ser configurada.
   * Mínimo de 1 (definindo apenas o ano) e máximo de 7
   * elementos (definindo até os milisegundos da hora).
   */
  public void setDate(int ... date) {
    if(date == null || date.length < 1) return;
    
    if(date.length == 1)
      this.setYear(date[0]);
    
    else if(date.length >= 2) {
      if(date.length >= 3)
        this.setDay(date[2]);
    
      if(date.length >= 4)
        this.setHour(date[3]);
    
      if(date.length >= 5)
        this.setMinute(date[4]);
    
      if(date.length >= 6)
        this.setSecond(date[5]);
    
      if(date.length >= 7)
        this.setMillis(date[6]);
    
      this.setMonth(date[1]);
      this.setYear(date[0]);
    }
  }
	
	
	/**
	 * Cria uma nova instância de SimpleDate,
	 * definindo a data/hora com base na String 
   * e formato informados.
	 * @param date String com a data.
	 * @param format formato da data.
	 * @return nova instância de SimpleDate
	 * representando a data informada ou NULL,
	 * caso o formato não seja reconhecido.
	 */
	public static SimpleDate parseDate(String date, String format) {
		if(date == null) return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			return new SimpleDate(df.parse(date));
		} catch (ParseException ex) {
			return null;
		}
	}


	/**
	 * Cria uma nova instância de SimpleDate,
	 * definindo a data/hora com base na String e informada, 
   * tentando decodificar automaticamente o formato do argumento.
	 * @param date String com a data.
	 * @param loc Idioma.
	 * @return nova instância de <code>SimpleDate</code>
	 * representando a data informada ou NULL,
	 * caso o formato não seja reconhecido.
	 */
	public static SimpleDate parseDate(String date, Locale loc) {
		if(date == null) return null;
		SimpleDateFormat df = null;
		String fm = null;

		// 01234
		// 20-01-2011
		// 2011-01-20
		if(Character.isDigit(date.charAt(4))) {

			// 20-01-2011
			if(date.charAt(2) == '-') {
				// 20-01-2011 13:50:00
				if(date.length() > 10)
					fm = DDMMYYYY_HHMMSS_DASH;
				// 20-01-2011
				else
					fm = DDMMYYYY_DASH;

			// 20/01/2011
			} else if(date.charAt(2) == '/') {
				// 20/01/2011 13:50:00
				if(date.length() > 10)
					fm = DDMMYYYY_HHMMSS_SLASH;
				// 20/01/2011
				else
					fm = DDMMYYYY_SLASH;

			// 20.01.2011
			} else {
				// 20/01/2011 13:50:00
				if(date.length() > 10)
					fm = DDMMYYYY_HHMMSS_DOT;
				// 20.01.2011
				else
					fm = DDMMYYYY_DOT;
			}

		// 01234
		// 2011-01-20
		} else if(!Character.isLetter(date.charAt(2))) {

			// 2011-01-20
			if(date.charAt(4) == '-') {
				// 2011-01-20 13:50:00
				if(date.length() > 10 && date.length() <= 19) {
					fm = YYYYMMDD_HHMMSS_DASH;
				// 2011-01-20 13:50:00.123456789
        } else if(date.length() > 19) {
					fm = SQL_DATE_TIME;
				// 2011-01-20
        } else {
					fm = YYYYMMDD_DASH;
        }
			// 2011/01/20
			} else if(date.charAt(4) == '/') {
				// 2011/01/20 13:50:00
				if(date.length() > 10)
					fm = YYYYMMDD_HHMMSS_SLASH;
				// 2011/01/20
				else
					fm = YYYYMMDD_SLASH;

			// 2011.01.20
			} else {
				// 2011.01.20 13:50:00
				if(date.length() > 10)
					fm = YYYYMMDD_HHMMSS_DOT;
				// 2011.01.20
				else
					fm = YYYYMMDD_DOT;
			}

		// Segunda-feira, 20 de Mar�o de 2011
		} else {

			// Segunda-feira, 20 de Mar�o de 2011, 13:50:00
			if(date.contains(":"))
				fm = LONG_DATE_TIME;
			// Segunda-feira, 20 de Mar�o de 2011
			else
				fm = LONG_DATE;
		}

		try {
			if(loc != null)
				df = new SimpleDateFormat(fm, loc);
			else
				df = new SimpleDateFormat(fm);

			return new SimpleDate(df.parse(date));

		} catch(ParseException ex) {
			throw new IllegalArgumentException(ex);
		}
	}


	/**
	 * Cria uma nova instância de SimpleDate,
	 * definindo a data/hora com base na String informada, 
   * tentando decodificar automaticamente o formato do argumento.
	 * @param date String com a data.
	 * @return nova instância de <code>SimpleDate</code>
	 * representando a data informada ou NULL,
	 * caso o formato não seja reconhecido.
	 */
	public static SimpleDate parseDate(String date) {
		Locale l = null;
		return parseDate(date, l);
	}



	/**
	 * Retorna a zona de fuso-horário utilizada por esta instância.
	 * @return TimeZone zona de fuso-horário
	 */
	public TimeZone getTimeZone() {
		return tzone;
	}


	/**
	 * Define a zona de fuso-horário utilizada para esta instância.
	 * @param tz TimeZone zona de fuso-horário
	 */
	public void setTimeZone(TimeZone tz) {
		if(tz == null) return;
		tzone = tz;
		cal = Calendar.getInstance(tzone, loc);
		cal.setTime(this);
	}
  
  
	/**
	 * Retorna uma instância de <code>java.util.Calendar</code>,
	 * configurada conforme <code>SimpleDate</code>.
	 * @return <code>java.util.Calendar</code>
	 */
	public Calendar getCalendar() {
		return (Calendar) cal.clone();
	}
	
	
  /**
   * Verifica se a data/hora atual de <code>SimpleDate</code> está
   * em definida em horário de verão ou não.
   * @return <code>true</code> se a data/hora de <code>SimpleDate</code>
   * está definida em horário de verão, 
   * <code>false</code> caso contrário.
   */
	public boolean useDaylightTime() {
		return tzone.inDaylightTime(this);
	}
  
  
  public SimpleDate setUseDaylightTime(boolean use) {
    tzone = TimeZone.getDefault();
    if(!use)
      tzone = new SimpleTimeZone(tzone.getRawOffset(), tzone.getID());
    return this;
  }
	
	
	/**
	 * Retorna uma instância de <code>SimpleDate</code> com base no
	 * <code>java.util.Date</code> informado.
	 * @param d <code>java.util.Date</code>
	 * @return <code>SimpleDate</code>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/util/Date.html">java.util.Date</a>
	 */
	public static SimpleDate from(Date d) {
		if(d == null) return null;
		return new SimpleDate(d);
	}
  
  
  /**
   * Retorna uma instância de <code>SimpleDate</code> a
   * partir do container <code>SerialDate</code> informado.
   * @param serialDate container <code>SerialDate</code>.
   * @return instância de <code>SimpleDate</code> a
   * partir do container <code>SerialDate</code> informado.
   * @see us.pserver.date.SerialDate
   */
  public static SimpleDate from(SerialDate serialDate) {
    if(serialDate == null) return null;
    return serialDate.getDate();
  }


	@Override
	public SimpleDate clone() {
		SimpleDate sd = new SimpleDate(this);
		sd.cal = (Calendar) cal.clone();
		sd.format = new String(format);
		sd.loc = (Locale) loc.clone();
		sd.tzone = (TimeZone) tzone.clone();
		return sd;
	}
	
	
	/**
	 * Retorna uma String da data representada por <code>SimpleDate</code>
	 * com base no formato informado.
	 * @param format formato para representação da data.
	 * @return String da data formatada.
	 */
	public String format(String format) {
		this.format = format;
    if(loc == null)
      loc = Locale.getDefault();
		SimpleDateFormat f = new SimpleDateFormat(format, loc);
		return f.format(this);
	}
	
	
	/**
	 * Adiciona o número de dias à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de dias a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addDay(int d) {
		cal.setTime(this);
		cal.add(Calendar.DAY_OF_MONTH, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Adiciona o número de meses à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de meses a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addMonth(int d) {
		cal.setTime(this);
		cal.add(Calendar.MONTH, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Adiciona o número de anos à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de anos a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addYear(int d) {
		cal.setTime(this);
		cal.add(Calendar.YEAR, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Adiciona o número de horas à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de horas a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addHour(int d) {
		cal.setTime(this);
		cal.add(Calendar.HOUR_OF_DAY, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Adiciona o número de minutos à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de minutos a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addMinute(int d) {
		cal.setTime(this);
		cal.add(Calendar.MINUTE, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Adiciona o número de segundos à data 
	 * representada por <code>SimpleDate</code>.
	 * @param d número de segundos a ser adicionado.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate addSecond(int d) {
		cal.setTime(this);
		cal.add(Calendar.SECOND, d);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	

	/**
	 * Diminui o número de dias da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de dias a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subDay(int d) {
		cal.setTime(this);
		cal.add(Calendar.DAY_OF_MONTH, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Diminui o número de meses da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de meses a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subMonth(int d) {
		cal.setTime(this);
		cal.add(Calendar.MONTH, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Diminui o número de anos da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de anos a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subYear(int d) {
		cal.setTime(this);
		cal.add(Calendar.YEAR, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Diminui o número de horas da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de horas a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subHour(int d) {
		cal.setTime(this);
		cal.add(Calendar.HOUR_OF_DAY, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Diminui o número de minutos da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de minutos a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subMinute(int d) {
		cal.setTime(this);
		cal.add(Calendar.MINUTE, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Diminui o número de segundos da data
	 * representada por <code>SimpleDate</code>.
	 * @param d número de segundos a ser diminuido.
	 * @return a instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate subSecond(int d) {
		cal.setTime(this);
		cal.add(Calendar.SECOND, (d * -1));
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	
	
	/**
	 * Retorna <code>true</code> se esta instância
	 * de SimpleDate possuir DATA (DD-MM-YYYY) IGUAL à data informada.
	 * @param d Data a ser testada.
	 * @return <code>true</code> se esta instância
	 * de SimpleDate possuir DATA (DD-MM-YYYY) IGUAL à data informada.
	 */
	public boolean equalsDate(Date d) {
		if(d == null) return false;
		SimpleDate date = new SimpleDate(d);
		return this.getYear() == date.getYear()
				&& this.getMonth() == date.getMonth()
				&& this.getDay() == date.getDay();
	}
	
	
  /**
   * Verifica se o objeto <code>java.util.Date</code> 
   * informado, define uma data/hora (dd/MM/yyyy hh:mm:ss) 
   * igual à representada por <code>SimpleDate</code>, porém comparando 
   * apenas até os segundos da hora definida.
   * @param d <code>java.util.Date</code> a ser comparada.
   * @return <code>true</code> se a data/hora informada
   * for igual à de SimpleDate, com precisão apenas de segundos,
   * <code>false</code> caso contrário.
   */
  public boolean equalsLess(Date d) {
		if(d == null) return false;
		SimpleDate date = SimpleDate.from(d);
		return this.getYear() == date.getYear()
				&& this.getMonth() == date.getMonth()
				&& this.getDay() == date.getDay()
        && this.getHour() == date.getHour()
        && this.getMinute() == date.getMinute()
        && this.getSecond() == date.getSecond();
  }
	
	
	/**
	 * Retorna <code>true</code> se esta instância
	 * de SimpleDate for igual, com precisão de 
   * milisegundos, à data informada.
	 * @param d Data a ser testada.
	 * @return <code>true</code> se esta instância
	 * de SimpleDate for IGUAL à data informada.
	 */
	public boolean equals(Date d) {
		if(d == null) return false;
		return super.equals(d);
	}
  
  
  /**
   * Verifica se a data representada por <code>SimpleDate</code>
   * está situada cronológicamente entre as datas informadas
   * <code>before</code> e <code>after</code>.
   * @param before Data anterior à <code>SimpleDate</code>.
   * @param after Data posterior à <code>SimpleDate</code>.
   * @return <code>true</code> se a data representada por
   * <code>SimpleDate</code> está situada cronologicamente
   * entre as datas <code>before</code> e <code>after</code>,
   * <code>false</code> caso contrário.
   */
  public boolean isBetween(Date before, Date after) {
    if(before == null || after == null) return false;
    return this.after(before) && this.before(after);
  }
  
  
	/**
	 * Retorna <code>true</code> se esta instância
	 * de SimpleDate for igual, com precisão de 
   * milisegundos, à data informada.
	 * @param o Data a ser testada.
	 * @return <code>true</code> se esta instância
	 * de SimpleDate for IGUAL à data informada.
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Date))
			return false;
		return this.equals((Date) o);
	}


	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.tzone != null ? this.tzone.hashCode() : 0);
		hash = 79 * hash + (this.loc != null ? this.loc.hashCode() : 0);
		hash += this.getTime();
		return hash;
	}
	
	
  /**
   * Calcula a diferença de tempo entre esta instância
   * e o objeto <code>java.util.Date</code> informado.
   * @param date objeto do qual será calculado a
   * diferença de tempo.
   * @return <code>DateDiff</code> encapsulando
   * as informações da direfernça de tempo entre as datas.
   */
	public DateDiff diff(Date date) {
		if(date == null) return null;
		return new DateDiff(this, date);
	}
	
	
	/**
	 * Retorna <code>true</code> se esta
	 * instância de <code>SimpleDate</code> possuir ANO, MES, DIA,
	 * HORA, MINUTO E SEGUNDO igual à data e hora ATUAL.
	 * @return <code>true</code> se esta
	 * instância de SimpleDate for IGUAL à data e hora ATUAL.
	 */
	public boolean isOnTime() {
		return this.equalsLess(SimpleDate.now());
	}
	
	
	/**
	 * Retorna <code>true</code> se esta
	 * instância de <code>SimpleDate</code> for MENOR
	 * que a data e hora atual.
	 * @return <code>true</code> se esta
	 * instância de <code>SimpleDate</code> for MENOR
	 * que a data e hora atual.
	 */
	public boolean isPast() {
		Date now = new Date();
		return this.before(now);
	}
	
	
	/**
	 * Retorna <code>true</code> se esta
	 * instância de <code>SimpleDate</code> for MAIOR
	 * que a data e hora atual.
	 * @return <code>true</code> se esta
	 * instância de <code>SimpleDate</code> for MAIOR
	 * que a data e hora atual.
	 */
	public boolean isFuture() {
		Date now = new Date();
		return this.after(now);
	}
	
	
	@Override
	public String toString() {
		return this.format(format);
	}
	
	
	/**
	 * Retorna uma String da data representada
	 * por esta instância de <code>SimpleDate</code>
	 * no formato SQL (YYYY-MM-DD).
	 * @return String no formato SQL da 
	 * data representada por esta instância 
	 * de <code>SimpleDate</code>.
	 */
	public String toSqlString() {
		return this.format(SQL_DATE);
	}
	
	
	/**
	 * Retorna uma String da data e hora representada
	 * por esta instância de <code>SimpleDate</code>
	 * no formato SQL (YYYY-MM-DD hh:mm:ss.fffffffff).
	 * @return String no formato SQL da 
	 * data e hora representada por esta instância 
	 * de <code>SimpleDate</code>.
	 */
	public String toSqlTimeString() {
		return this.format(SQL_DATE_TIME);
	}
	
	
	/**
	 * Converte para java.sql.Date.
	 * @return java.sql.Date
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(this.getTime());
	}
	
	
	/**
	 * Converte para java.sql.Timestamp.
	 * @return java.sql.Timestamp
	 */
	public Timestamp toSqlTimestamp() {
		return new Timestamp(this.getTime());
	}


  /**
   * Retorna um container serializável <code>SerialDate</code>
   * configurado com esta instância de <code>SimpleDate</code>.
   * @return container serializável <code>SerialDate</code>
   * configurado com esta instância de <code>SimpleDate</code>.
   * @see us.pserver.date.SerialDate
   */
  public SerialDate toSerialDate() {
    return new SerialDate(this);
  }
	
	
	/**
	 * Define o idioma utilizado por <code>SimpleDate</code>.
	 * @param l Idioma <code>Locale</code>.
	 * @return esta instância de <code>SimpleDate</code> modificada.
	 */
	public SimpleDate setLocale(Locale l) {
		if(l == null) return this;
		loc = l;
		cal = Calendar.getInstance(tzone, loc);
		cal.setTime(this);
		return this;
	}
  
  
	/**
	 * Retorna o idioma utilizado por <code>SimpleDate</code>.
	 * @return Idioma <code>Locale</code>.
	 */
  public Locale getLocale() {
    return loc;
  }
	
}
