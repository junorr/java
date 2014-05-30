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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Container para armazenamento serializável de datas.
 * 
 * @version 3.0 - 18/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class SerialDate implements Serializable {

  private long time;
  
  private String localeLanguage;
  
  private String localeCountry;
  
  private String timezone;
  
  
  /**
   * Construtor padrão e sem argumentos,
   * cria uma data vazia.
   */
  public SerialDate() {
    time = 0;
    localeLanguage = null;
    localeCountry = null;
    timezone = null;
  }
  
  
  /**
   * Construtor que recebe a data a ser armazenada.
   * @param date Data a ser armazenada.
   */
  public SerialDate(Date date) {
    this();
    this.setDate(date);
  }
  
  
  /**
   * Construtor que recebe a data a ser armazenada.
   * @param date Data a ser armazenada.
   */
  public SerialDate(SimpleDate date) {
    this();
    this.setDate(date);
  }
  
  
  /**
   * Define a data a ser armazenada.
   * @param date Data a ser armazenada.
   * @return Esta instância modificada de <code>SerialDate</code>.
   */
  public SerialDate setDate(Date date) {
    if(date != null) {
      time = date.getTime();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      timezone = c.getTimeZone().getID();
    }
    return this;
  }
  
  
  /**
   * Define a data a ser armazenada.
   * @param date Data a ser armazenada.
   * @return Esta instância modificada de <code>SerialDate</code>.
   */
  public SerialDate setDate(SimpleDate date) {
    if(date != null) {
      time = date.getTime();
      localeLanguage = date.getLocale().getLanguage();
      localeCountry = date.getLocale().getCountry();
      timezone = date.getTimeZone().getID();
    }
    return this;
  }
  
  
  /**
   * Retorna a data armazenada por esta instância de
   * <code>SerialDate</code>.
   * @return data <code>SimpleDate</code> armazenada por 
   * esta instância de <code>SerialDate</code>.
   */
  public SimpleDate getDate() {
    if(time <= 0) return null;
    SimpleDate date = new SimpleDate();
    if(localeLanguage != null && localeCountry != null) {
      date.setLocale(new Locale(localeLanguage, localeCountry));
    }
    if(timezone != null) {
      date.setTimeZone(TimeZone.getTimeZone(timezone));
    }
    if(time > 0) {
      date = new SimpleDate().time(time);
    }
    return date;
  }
  
}
