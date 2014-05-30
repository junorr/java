/*
  Direitos Autorais Reservados (c) 2009 Juno Roesler
  Contato com o autor: powernet.de@gmail.com

  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.

  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.

  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/

package com.jpower.utils;

import com.thoughtworks.xstream.XStream;

import java.util.Calendar;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Classe de configuração que encapsula
 * atributos de data para representar um
 * determinado instante no tempo, com precisão
 * de milisegundos.
 * Utilizada na configuração de <code>Timer</code>.
 * @author Juno Roesler - powernet.de@gmail.com
 */
public class Date implements Serializable {

  private static final long serialVersionUID = 27L;

  /**
   * Constante que representa um campo
   * da classe <code>Date</code>.
   */
  public static final int
    ANO = 0, MES = 1, DIA = 2, HORA = 3,
    MIN = 4, SEC = 5, MIL = 6;

  /**
   * Constante que representa o valor maximo
   * assumido pela variavel.
   */
  public static final int
    MAX_MES = 12, MAX_HORA = 23, MAX_MIN = 59,
    MAX_SEC = 59, MAX_MIL = 999;

  /**
   * Campos de configuracao da data representada
   * por Timer.
   */
  private int ano, mes, dia, hora, min, sec, mil;

  /**
   * Frequencia de repeticao para <code>Date</code>.
   */
  private int repeat;

  /**
   * Tipo de frequencia (ano, mês, dia, hora, minuto, segundos).
   */
  private int rtype;

  /**
   * Instante no tempo para o qual
   * <code>Timer</code> aponta/representa.
   */
  private long time;

  private boolean fullEqualsMode;


  /**
   * Construtor principal e sem argumentos, constrói
   * um <code>Date</code> que representa o
   * instante de sua construção.
   */
  public Date() {
    setTime(System.currentTimeMillis());
    repeat = 0;
    rtype = -1;
    this.fullEqualsMode = false;
  }//Date


  /**
   * Construtor que recebe como argumento
   * um <code>long</code>, do instante no
   * tempo a ser encapsulado por <code>Date</code>.
   * @param time Tempo em milisegundos, a ser representado
   * por <code>Date</code>.
   */
  public Date(long time) {
    this();
    setTime(time);
  }//Date()


  /**
   * Adiciona a quantidade de tempo especificada
   * por <code>amount</code>, ao campo <code>field</code>,
   * repeitando os valores maximos de cada campo.
   * @param field Campo a ser incrementado, especificado
   * pelas constantes de <code>DateUtils</code>.
   * @param amount Quantidade de tempo a ser incrementada
   * ao campo.
   */
  public void add(int field, int amount) {
    DateUtils.add(this, field, amount);
  }//add()


  /**
   * Clona esta instência de Date, retornando
   * outro objeto idêntico.
   * @return Um clone do objeto atual.
   */
  @Override
  public Date clone() {
    Date clone = new Date(this.getTime());
    clone.setFrequency(this.getFrequency());
    clone.setFrequencyType(this.getFrequencyType());
    clone.setFullComparationMode(this.isFullComparationMode());
    return clone;
  }//method()


  @Override
  public boolean equals(Object o)
  {
    if(!(o instanceof Date))
      return false;
    return ((Date) o).hashCode() == this.hashCode();
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 13 * hash + this.ano;
    hash = 13 * hash + this.mes;
    hash = 13 * hash + this.dia;
    if(!fullEqualsMode)
      return hash;
    else {
      hash = 13 * hash + this.hora;
      hash = 13 * hash + this.min;
      hash = 13 * hash + this.sec;
      hash = 13 * hash + this.mil;
    }
    return hash;
  }

  public void setFullComparationMode(boolean fullEquals)
  {
    this.fullEqualsMode = fullEquals;
  }

  public boolean isFullComparationMode()
  {
    return fullEqualsMode;
  }

  public boolean isAfter(Date d)
  {
    if(d == null) return false;
    if(fullEqualsMode)
      return this.time > d.getTime();
    else {
      if(ano > d.get(Date.ANO))
        return true;
      else if(mes > d.get(Date.MES))
        return true;
      else
        return dia > d.get(Date.DIA);
    }
  }

  public boolean isBefore(Date d)
  {
    if(d == null) return false;
    if(fullEqualsMode)
      return this.time < d.getTime();
    else {
      if(ano < d.get(Date.ANO))
        return true;
      else if(mes < d.get(Date.MES))
        return true;
      else
        return dia < d.get(Date.DIA);
    }
  }


  /**
   * Subtrai a quantidade de tempo especificada
   * por <code>amount</code>, do campo <code>field</code>,
   * repeitando os valores maximos e mínimos de cada campo.
   * @param field Campo a ser subtraído.
   * @param amount Quantidade de tempo a ser subtraída
   * do campo.
   */
  public void sub(int field, int amount) {
    DateUtils.sub(this, field, amount);
  }//method()


  /**
   * Retorna o campo representado por <code>field</code>.
   * @param field Constante representando o campo a ser obtido.
   * @return Valor do campo.
   */
  public int get(int field) {
    switch(field) {
      case ANO:
        return ano;
      case MES:
        return mes;
      case DIA:
        return dia;
      case HORA:
        return hora;
      case MIN:
        return min;
      case SEC:
        return sec;
      case MIL:
        return mil;
      default:
        return -1;
    }//switch
  }//get()


  /**
   * Retorna uma instância de <code>Date</code> com os valores
   * setados manualmente conforme os parâmetros informados.
   * A ordem dos parâmetros devem ser OBRIGATORIAMENTE
   * conforme segue: ANO, MES, DIA, HORA, MINUTO, SEGUNDO, MILISEGUNDO.
   * Caso a ordem nao seja respeitada, a instância poderá
   * ser configurada incorretamente. Valores não especificados serão
   * setados de acordo com o instante da criação.
   * @param values Valores da instância retornada.
   * @return <code>Date</code> configurado conforme os parâmetros.
   */
  public static Date getInstance(int ... values) {
    Date tc = new Date();
    tc.set(values);
    //for(int i = 0; i < x; i++) {
      //td.set(i, values[i]);
    //}//for
    return tc;
  }//getInstnce()


  /**
   * Retorna uma instância de <code>Date</code> a
   * partir de uma <code>String</code> no formato
   * XML.
   * @param xml <code>String</code> contendo
   * <code>Date</code> no formato XML.
   * @return <code>Date</code> lido da <code>String</code> XML.
   * @throws IOException Caso ocorra erro lendo a <code>String</code>.
   */
  public static Date getInstance(String xml) throws IOException {
    if(xml == null || xml.equals("")) return null;

    XStream x = new XStream();
    x.alias("Date", com.jpower.utils.Date.class);
    return (Date) x.fromXML(xml);
  }//load()


  /**
   * Carrega uma instância de <code>Date</code>
   * de um arquivo XML.
   * @param f Arquivo XML contendo <code>Date</code>.
   * @return <code>Date</code> lido do arquivo XML.
   * @throws IOException Caso ocorra erro lendo o arquivo.
   */
  public static Date getInstance(File f) throws IOException {
    if(f == null) return null;
    if(!f.exists()) return null;

    FileReader fr = new FileReader(f);
    XStream x = new XStream();
    x.alias("Date", com.jpower.utils.Date.class);
    return (Date) x.fromXML(fr);
  }//load()


  /**
   * Retorna a data no formato
   * <b><code>dd.MM.yyyy-hh:mm:ss</code></b>.
   */
  public String getStringFormat()
  {
    return this.getDateStringFormat()+ "-"+ this.getHourStringFormat();
  }//method()


  /**
   * Retorna a data no formato
   * <b><code>dd.MM.yyyy-hh:mm:ss</code></b>.
   */
  public String getStringFormat(String dateSeparator, String dateHourSeparator, String hourSeparator)
  {
    if(dateSeparator == null || dateHourSeparator == null || hourSeparator == null)
      return null;
    
    return this.getDateStringFormat(dateSeparator)+ dateHourSeparator + this.getHourStringFormat(hourSeparator);
  }//method()


  /**
   * Retorna a data como String no formato
   * <b><code>dd.MM.yyyy</code></b>.
   * @return String com a data formatada.
   */
  public String getDateStringFormat()
  {
    return getDateStringFormat(".");
  }


  /**
   * Retorna a data como String no formato
   * <b><code>dd.MM.yyyy</code></b>.
   * @return String com a data formatada.
   */
  public String getDateStringFormat(String separator)
  {
    if(separator == null) separator = ".";
    String d = String.valueOf(dia);
    String m = String.valueOf(mes);
    String a = String.valueOf(ano);
    if(dia < 10)
      d = "0"+ d;
    if(mes < 10)
      m = "0"+ m;
    return d + separator + m + separator + a;
  }


  /**
   * Retorna a hora como String no formato
   * <b><code>hh:mm:ss.ml</code></b>
   * @return String contendo a hora formatada.
   */
  public String getHourStringFormat()
  {
    return getHourStringFormat(":");
  }

  /**
   * Retorna a hora como String no formato
   * <b><code>hh:mm:ss.ml</code></b>
   * @return String contendo a hora formatada.
   */
  public String getHourStringFormat(String separator)
  {
    String h = String.valueOf(hora);
    String m = String.valueOf(min);
    String s = String.valueOf(sec);
    String l = String.valueOf(mil);
    if(hora < 10)
      h = "0"+ h;
    if(min < 10)
      m = "0"+ m;
    if(sec < 10)
      s = "0"+ s;
    if(mil < 10)
      l = "0"+ l;
    return h + separator + m + separator + s + separator + l;
  }


  /**
   * Retorna uma String contendo a
   * frequencia de repetição de
   * <code>Date</code>.
   * @return String contendo a frequencia de
   * repetição de <code>Date</code>.
   */
  public String getRepetitionStringFormat()
  {
    String srept = String.valueOf(
            this.getFrequency());
    int ifreq = this.getFrequencyType();
    switch(ifreq) {
      case Date.ANO:
        srept += "y";
        break;
      case Date.DIA:
        srept += "d";
        break;
      case Date.HORA:
        srept += "h";
        break;
      case Date.MES:
        srept += "M";
        break;
      case Date.MIN:
        srept += "m";
        break;
      case Date.SEC:
        srept += "s";
        break;
      default:
        srept += "x";
        break;
    }//switch
    return srept;
  }


  /**
   * Retorna o instante que <code>Date</code>
   * representa, em milisegundos.
   * @return Instante no tempo para o qual
   * <code>Date</code> aponta.
   */
  public long getTime() {
    return time;
  }//getDeadline()


  /**
   * Retorna a frequência de repetição
   * para este <code>Date</code>.
   * @return Frequência de repetição.
   */
  public int getFrequency() {
    return repeat;
  }//getFrequency()


  /**
   * Retorna o tipo de frequência (HORA, DIA, MES, etc.)
   * @return <code>ANO, MES, DIA,
   * HORA, MIN, SEC</code>, ou '-1',
   * caso nao tenha sido definido.
   */
  public int getFrequencyType() {
    return rtype;
  }//getFrequencyType()


  /**
   * Verifica se o instante de tempo representado
   * por <code>Date</code> é passado em relação
   * ao momento atual.
   * @return <code>boolean</code> indicando se o instante de
   * <code>Date</code> é passado.
   */
  public boolean isOld() {
    Date atual = new Date();
    return (DateUtils.getSecondsBetween(atual, this) <= 0);
  }//isOld()


  /**
   * Verifica se o instante representado
   * por <code>Date</code> é o segundo atual.
   * Diferenças de milisegundos são desconsiderados.
   * @return <code>boolean</code> indicando se o segundo atual é
   * o mesmo representado por <code>Date</code>.
   */
  public boolean isOnTime() {
    Date atual = new Date();
    return DateUtils.equals(atual, this);
  }//isOnTime()


  /**
   * Retorna uma instância de Date
   * de acordo com a string informada,
   * que deve obedecer obrigatoriamente
   * aos formatos:
   * <b><code>dd.MM.yyyy-hh:mm:ss</code></b>,
   * quando possui especificação de horário, ou
   * <b><code>dd.MM.yyyy</code></b>,
   * quando for especficada somente a data.
   * @param dateString Data no formato String.
   */
  public static Date parseDate(String dateString, String dateSeparator, String dateHourSeparator, String hourSeparator) {
    if(dateString == null || dateString.equals(""))
      return null;
    if(dateSeparator == null || dateSeparator.equals(""))
      return null;
    if(dateHourSeparator == null || dateHourSeparator.equals(""))
      return null;
    if(hourSeparator == null || hourSeparator.equals(""))
      return null;

    if(hourSeparator.equals("."))
      hourSeparator = "\\".concat(hourSeparator);
    if(dateSeparator.equals("."))
      dateSeparator = "\\".concat(dateSeparator);
    if(dateHourSeparator.equals("."))
      dateHourSeparator = "\\".concat(dateHourSeparator);

    Date d = null;
    try {
      String[] values = dateString.split(dateHourSeparator);
      String date = values[0];

      String[] dvals = date.split(dateSeparator);

      if(values.length > 1) {

        String hour = values[1];

        String[] hvals = hour.split(hourSeparator);

        d = Date.getInstance(
          Integer.parseInt(dvals[2]),
          Integer.parseInt(dvals[1]),
          Integer.parseInt(dvals[0]),
          Integer.parseInt(hvals[0]),
          Integer.parseInt(hvals[1]),
          Integer.parseInt(hvals[2]));

      } else {
        d = Date.getInstance(
          Integer.parseInt(dvals[2]),
          Integer.parseInt(dvals[1]),
          Integer.parseInt(dvals[0]));
      }

    } catch(Exception ex) { ex.printStackTrace(); }

    return d;
  }//method()


  /**
   * Retorna uma instância de Date
   * de acordo com a string informada,
   * que deve obedecer obrigatoriamente
   * aos formatos:
   * <b><code>dd.MM.yyyy-hh:mm:ss</code></b>,
   * quando possui especificação de horário, ou
   * <b><code>dd.MM.yyyy</code></b>,
   * quando for especficada somente a data.
   * @param dateString Data no formato String.
   */
  public static Date parseDate(String dateString) {
    return parseDate(dateString, ".", "-", ":");
  }//method()


  /**
   * Interpreta e seta a frequencia de repetição
   * de <code>Date</code> a partir da String
   * informada, que deve obedecer o padrão
   * [qtd + tipo], onde:<br>
   * - [qtd].: int da fequencia de repetição;<br>
   * - [tipo]: Identificação do tempo da frequencia,
   * que deve ser [ y | M | d | h | m | s ], para
   * anos, meses, dias, horas, minutos e segundos,
   * respectivamente.
   * @param srept String no formato especificado,
   * contendo a frequencia de repetição de
   * <code>Date</code>.
   */
  public void parseRepetition(String srept)
  {
    if(srept == null ||
        ( srept.equals("") ||
          srept.endsWith("x")))
      return;

    try
    {
      this.setFrequency(
          Integer.parseInt(
          srept.substring(0, srept.length()-1)));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      return;
    }

    if(srept.substring(srept.length()-1)
        .equals("y"))
      this.setFrequencyType(Date.ANO);
    else if(srept.substring(srept.length()-1)
        .equals("M"))
      this.setFrequencyType(Date.MES);
    else if(srept.substring(srept.length()-1)
        .equals("d"))
      this.setFrequencyType(Date.DIA);
    else if(srept.substring(srept.length()-1)
        .equals("h"))
      this.setFrequencyType(Date.HORA);
    else if(srept.substring(srept.length()-1)
        .equals("m"))
      this.setFrequencyType(Date.MIN);
    else if(srept.substring(srept.length()-1)
        .equals("s"))
      this.setFrequencyType(Date.SEC);
    else
      return;
  }


  /**
   * Reconfigura <code>Date</code> de acordo
   * com a configuração de frequência existente.
   */
  public void reconfigure() {
    if(repeat == 0 || rtype < 0)
      return;
    this.add(rtype, repeat);
  }//reconfigure()


  /**
   * Seta manualmente os campos de <code>Date</code>, sendo que
   * a ordem dos parâmetros deve ser NECESSARIAMENTE como
   * segue: ANO, MES, DIA, HORA, MINUTO, SEGUNDO, MILISEGUNDO.
   * Caso esta ordem na seja respeitada, não é garantido
   * que o valor especificado será setado corretamente.
   * @param values Valores a serem setados manualmente.
   */
  public void set(int ... values) {
    int max = 7;
    if(values.length <= 7) max = values.length;

    for(int i = 0; i < max; i++) {
      this.set(i, values[i]);
    }//for
  }//set()


  /**
   * Seta o campo especificado, com o valor especificado.
   * @param field Campo a ser setado.
   * @param value Valor a ser setado para o campo.
   */
  public void set(int field, int value) {
    if(value < 0 || field < 0) return;
    switch(field) {
      case ANO:
        ano = value;
        break;
      case MES:
        if(value <= MAX_MES)
          mes = value;
        break;
      case DIA:
        int max = DateUtils.DIAS_MES[mes-1];
        if(max == 0)
          max = DateUtils.diasFevereiro(ano);
        if(value <= max)
          dia = value;
        break;
      case HORA:
        if(value <= MAX_HORA)
          hora = value;
        break;
      case MIN:
        if(value <= MAX_MIN)
          min = value;
        break;
      case SEC:
        if(value <= MAX_SEC)
          sec = value;
        break;
      case MIL:
        if(value <= MAX_MIL)
          mil = value;
        break;
      default:
        return;
    }//switch
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, ano);
    c.set(Calendar.MONTH, mes-1);
    c.set(Calendar.DAY_OF_MONTH, dia);
    c.set(Calendar.HOUR_OF_DAY, hora);
    c.set(Calendar.MINUTE, min);
    c.set(Calendar.SECOND, sec);
    c.set(Calendar.MILLISECOND, mil);
    time = c.getTimeInMillis();
  }//set()


  /**
   * Seta o instante que <code>Date</code> representa.
   * @param time Instante para o qual <code>Date</code>
   * aponta.
   */
  public void setTime(long time) {
    Calendar cal = Calendar.getInstance();
    this.time = time;
    cal.setTimeInMillis(time);
    ano = cal.get(Calendar.YEAR);
    mes = cal.get(Calendar.MONTH) + 1;
    dia = cal.get(Calendar.DAY_OF_MONTH);
    hora = cal.get(Calendar.HOUR_OF_DAY);
    min = cal.get(Calendar.MINUTE);
    sec = cal.get(Calendar.SECOND);
    mil = cal.get(Calendar.MILLISECOND);
  }//setTime()


  /**
   * Seta a frequência de repetição de <code>Date</code>.
   * @param fq Frequência de repetição.
   */
  public void setFrequency(int fq) {
    repeat = fq;
  }//setFrequency()


  /**
   * Seta o tipo de frequência de repetição de
   * <code>Date (ANO, MES,
   * DIA, HORA, MIN, SEC)
   * </code>.
   * @param type Tipo de frequência de repetição definido
   * em <code>DateUtils</code>.
   */
  public void setFrequencyType(int type) {
    if(type > 6) return;
    rtype = type;
  }//setFrequencyType()


  /**
   * Armazena esta instância de <code>Date</code>
   * em um arquivo XML.
   * @param f Arquivo onde sera armazenado as configurações
   * de <code>Date</code>.
   * @throws IOException Caso ocorra erro gravando
   * <code>Date</code> no arquivo XML.
   */
  public void store(File f) throws IOException {
    if(f == null) return;
    if(!f.exists()) f.createNewFile();

    FileWriter fw = new FileWriter(f);
    XStream x = new XStream();
    x.alias("Date", com.jpower.utils.Date.class);
    x.toXML(this, fw);
  }//store()


  /**
   * Transforma esta instancia de <code>Date</code>
   * em uma <code>String</code> formatada em
   * <code>XML</code>.
   * @return <code>String</code> no formato XML com
   * a instancia de <code>Date</code>.
   * @throws IOException Caso ocorra erro convertendo
   * <code>Date</code> em XML.
   */
  public String toXML() throws IOException {
    XStream x = new XStream();
    x.alias("Date", com.jpower.utils.Date.class);
    return x.toXML(this);
  }//store()


  /**
   * Retorna uma String representando a instancia
   * de <code>Date</code>.
   * @return <code>String</code> com as configuracoes
   * de <code>Date</code>.
   */
  @Override
  public String toString() {
    String s =
      " Date: "+ dia+ "-"+ mes+ "-"+ ano+
      "  "+ hora+ ":"+ min+ ":"+ sec+ "."+ mil;
    return s;
  }//toString()


  public static void main(String[] args)
  {
    System.out.println( Date.parseDate("05.02.2010") );
    Date d1 = Date.parseDate("17.02.2010");
    Date d2 = Date.parseDate("17.02.2010-12:50:00");
    System.out.println( "d1 : " + d1 );
    System.out.println( "d2 : " + d2 );
    System.out.println( "d1.setFullComparationMode(false);" );
    d1.setFullComparationMode(false);
    System.out.println( "d1.isBefore(d2) : " + d1.isBefore(d2) );
    System.out.println( "d1.isAfter(d2) : " + d1.isAfter(d2) );
    System.out.println( "d1.equals(d2) : " + d1.equals(d2) );
    System.out.println( "d1.setFullComparationMode(true);" );
    d1.setFullComparationMode(true);
    System.out.println( "d1.isBefore(d2) : " + d1.isBefore(d2) );
    System.out.println( "d1.isAfter(d2) : " + d1.isAfter(d2) );
    System.out.println( "d1.equals(d2) : " + d1.equals(d2) );
  }

}//class

