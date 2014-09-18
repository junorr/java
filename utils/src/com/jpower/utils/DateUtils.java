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

import java.util.Calendar;

/**
 * Classe abstrata que encapsula constantes
 * e metodos de manipulacao de datas.
 * @author Juno Roesler - powernet.de@gmail.com
 */
public abstract class DateUtils {

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
   * Array contendo o valor maximo de dias permitido
   * para determinado mês, com excecao para fevereiro
   * (<code>DIAS_MES[1]</code>), que
   * deve ser obtido atraves do metodo <br>
   * <code>DateUtils.diasFevereiro(int ano)</code>.
   */
  public static final int[] DIAS_MES = {
    31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
  };


  /**
   * Adiciona a quantidade de tempo <code>amount</code>
   * especificada, no campo <code>field</code>,
   * ao <code>Date</code> informado.
   * @param time <code>Date</code> aa ser adicionado o
   * tempo especificado.
   * @param field Campo a ser adicionado.
   * @param amount Quantidade de tempo a ser adicionada.
   * @return <code>Date</code> modificado ou <code>null</code>,
   * caso algum valor seja invalido.
   */
  public static Date add(Date time, int field, int amount) {
    if(field < 0 || field > 6) return null;
    if(time == null || amount <= 0) return null;

    if(field == ANO)
      time.set(field, time.get(field)+amount);
    else {
      switch(field) {
        case MES:
          addMonth(time, amount);
          break;
        case DIA:
          addDay(time, amount);
          break;
        case HORA:
          addHour(time, amount);
          break;
        case MIN:
          addMin(time, amount);
          break;
        case SEC:
          addSec(time, amount);
          break;
        case MIL:
          addMillis(time, amount);
          break;
        default:
          return null;
      }//switch
    }//else
    return time;
  }//add()

  /**
   * Adiciona a quantidade de meses especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addMonth(Date time, int amount) {
    if(time == null || amount <= 0)
      return;
    if((time.get(MES) + amount) > MAX_MES) {
      int mm = time.get(MES);
      time.set(ANO, time.get(ANO)+1);
      time.set(MES, 1);
      amount += mm;
      amount -= (12 + 1);
      addMonth(time, amount);
    } else {
      time.set(MES, time.get(MES) + amount);
    }//if-else
  }//addMonth()

  /**
   * Adiciona a quantidade de dias especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addDay(Date time, int amount) {
    if(time == null || amount <= 0)
      return;
    int maxdia = DIAS_MES[time.get(MES) - 1];
    if(maxdia == 0)
      maxdia = diasFevereiro(time.get(ANO));
    if((time.get(DIA) + amount) > maxdia) {
      int dd = time.get(DIA);
      addMonth(time, 1);
      time.set(DIA, 1);
      amount += dd;
      amount -= (maxdia + 1);
      addDay(time, amount);
    } else {
      time.set(DIA, time.get(DIA) + amount);
    }//if-else
  }//addDay()

  /**
   * Adiciona a quantidade de horas especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addHour(Date time, int amount) {
    if(time == null || amount <= 0)
      return;
    if((time.get(HORA) + amount) > MAX_HORA) {
      int hh = time.get(HORA);
      addDay(time, 1);
      time.set(HORA, 0);
      amount += hh;
      amount -= 24;
      addHour(time, amount);
    } else {
      time.set(HORA, time.get(HORA) + amount);
    }//if-else
  }//addHour()

  /**
   * Adiciona a quantidade de minutos especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addMin(Date time, int amount) {
    if(time == null || amount <= 0)
      return;
    if((time.get(MIN) + amount) > MAX_MIN) {
      int mi = time.get(MIN);
      addHour(time, 1);
      time.set(MIN, 0);
      amount += mi;
      amount -= 60;
      addMin(time, amount);
    } else {
      time.set(MIN, time.get(MIN) + amount);
    }//if-else
  }//addMin()

  /**
   * Adiciona a quantidade de segundos especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addSec(Date time, int amount) {
    if(time == null || amount <= 0)
      return;
    if((time.get(SEC) + amount) > MAX_SEC) {
      int ss = time.get(SEC);
      addMin(time, 1);
      time.set(SEC, 0);
      amount += ss;
      amount -= 60;
      addSec(time, amount);
    } else {
      time.set(SEC, time.get(SEC) + amount);
    }//if-else
  }//addHour()


  /**
   * Adiciona a quantidade de milisegundos especificado
   * por <code>amount</code> ao <code>Date</code>
   * especificado por <code>time</code>.
   */
  private static void addMillis(Date time, int amount) {
    if(time == null || amount <= 0)
      return;

    if((time.get(MIL) + amount) > MAX_MIL) {
      int ml = time.get(MIL);
      addSec(time, 1);
      time.set(MIL, 0);
      amount += ml;
      amount -= MAX_MIL;
      addMillis(time, amount);
    } else {
      time.set(MIL, time.get(MIL) + amount);
    }//if-else
  }//addSec()


  /**
   * Subtrai a quantidade de tempo especificada
   * por <code>amount</code>, do campo <code>field</code>
   * da data informada, respeitando os valores maximos
   * e mínimos de cada campo.
   * @param date Data a ser modificada.
   * @param field Campo a ser subtraído.
   * @param amount Quantidade de tempo a ser subtraída
   * do campo.
   */
  public static void sub(Date date, int field, int amount) {
    switch(field) {
      case ANO:
        subYear(date, amount);
        break;
      case MES:
        subMonth(date, amount);
        break;
      case DIA:
        subDay(date, amount);
        break;
      case HORA:
        subHour(date, amount);
        break;
      case MIN:
        subMin(date, amount);
        break;
      case SEC:
        subSec(date, amount);
        break;
      case MIL:
        subMillis(date, amount);
        break;
      default:
        return;
    }//switch
  }//method()


  /**
   * Subtrai a quantidade de anos especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subYear(Date date, int amount) {
    if(date == null || amount <= 0)
      return;

    int y = date.get(ANO);
    y--;
    date.set(ANO, y);
  }//method()


  /**
   * Subtrai a quantidade de meses especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subMonth(Date date, int amount) {
    if(date == null || amount <= 0)
      return;

    int M = date.get(MES);
    int d = date.get(DIA);
    int y = date.get(ANO);

    while(amount > 0) {
      M--;
      if(M < 1) {
        M = MAX_MES;
        subYear(date, 1);
      }//if
      amount--;
    }//while

    int md = DateUtils.getMaxDias(M, y);
    if(d > md)
      d = md;

    date.set(MES, M);
    date.set(DIA, d);
  }//method()


  /**
   * Subtrai a quantidade de dias especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subDay(Date date, int amount) {
    if(date == null || amount < 0) return;

    int d = date.get(DIA);
    int M;
    int y;

    while(amount > 0) {
      d--;
      if(d < 1) {
        subMonth(date, 1);
        M = date.get(MES);
        y = date.get(ANO);
        d = getMaxDias(M, y);
      }//if
      amount--;
    }//while

    date.set(DIA, d);
  }//method()


  /**
   * Subtrai a quantidade de horas especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subHour(Date date, int amount) {
    if(date == null || amount < 0) return;

    int h = date.get(HORA);

    while(amount > 0) {
      h--;
      if(h < 0) {
        subDay(date, 1);
        h = MAX_HORA;
      }//if
      amount--;
    }//while

    date.set(HORA, h);
  }//method()


  /**
   * Subtrai a quantidade de minutos especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subMin(Date date, int amount) {
    if(date == null || amount < 0) return;

    int m = date.get(MIN);

    while(amount > 0) {
      m--;
      if(m < 0) {
        subHour(date, 1);
        m = MAX_MIN;
      }//if
      amount--;
    }//while

    date.set(MIN, m);
  }//method()


  /**
   * Subtrai a quantidade de segundos especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subSec(Date date, int amount) {
    if(date == null || amount < 0) return;

    int s = date.get(SEC);

    while(amount > 0) {
      s--;
      if(s < 0) {
        s = MAX_SEC;
        subMin(date, 1);
      }//if
      amount--;
    }//while

    date.set(SEC, s);
  }//method()


  /**
   * Subtrai a quantidade de milisegundos especificado
   * por <code>amount</code> do <code>Date</code>
   * especificado por <code>date</code>.
   */
  private static void subMillis(Date date, int amount) {
    if(date == null || amount < 0) return;

    int ml = date.get(MIL);

    if(ml - amount < 0) {
      ml = MAX_MIL;
      subSec(date, 1);
      amount -= ml;
      date.set(MIL, ml);
      if(amount > 0)
        subMillis(date, amount);
    } else
      ml -= amount;

    date.set(MIL, ml);
  }//method()


  /**
   * Retorna o número máximo de dias existentes
   * para o mês e o ano informados.
   * @param mes Mês de referência.
   * @param ano Ano de referência.
   * @return Quantidade máxima de dias existentes
   * para o mês e ano informados.
   */
  public static int getMaxDias(int mes, int ano) {
    if(mes-1 < 0 || mes > MAX_MES) return -1;
    if(ano < 0) return -1;

    int max = 0;
    max = DIAS_MES[mes-1];
    if(max == 0)
      max = DateUtils.diasFevereiro(ano);

    return max;
  }//method()


  /**
   * Retorna a quantidade máxima de dias existentes no
   * mês de fevereiro do ano especificado.
   * @param ano Ano ao qual refere-se o cálculo solicitado.
   * @return Quantidade de dias para mês de fevereiro do
   * ano especificado.
   */
  public static int diasFevereiro(int ano) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.YEAR, ano);
    cal.set(Calendar.MONTH, Calendar.FEBRUARY);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }//diasFevereiro()

  /**
   * Retorna o tempo em milisegundos restante
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (<code>long</code>) restante entre
   * os instantes.
   */
  public static long getTimeBetween(Date tc1, Date tc2) {
    if(tc1 == null || tc2 == null)
      return -1;
    return (tc2.getTime() - tc1.getTime());
  }//getTimeBetween()

  /**
   * Retorna o tempo, em anos, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em anos) restante entre
   * os instantes.
   */
  public static int getYearsBetween(Date tc1, Date tc2) {
    if(tc1 == null || tc2 == null)
      return -1;
    int ajuste = 0;
    if(tc2.get(MES) < tc1.get(MES))
      ajuste = 1;
    return tc2.get(ANO) - tc1.get(ANO) - ajuste;
  }//getYearsBetween()

  /**
   * Retorna o tempo, em meses, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em meses) restante entre
   * os instantes.
   */
  public static int getMonthsBetween(Date tc1, Date tc2) {
    int days = getDaysBetween(tc1, tc2);
    int mesatual = tc1.get(MES);
    int maxdays = DIAS_MES[mesatual-1];
    int anoatual = tc1.get(ANO);
    int rem = 0;
    if(maxdays == 0)
      maxdays = diasFevereiro(anoatual);
    while(days > maxdays) {
      days -= maxdays;
      rem++;
      mesatual++;
      if(mesatual > MAX_MES) {
        mesatual = 1;
        anoatual++;
      }//if
      maxdays = DIAS_MES[mesatual-1];
      if(maxdays == 0)
        maxdays = diasFevereiro(anoatual);
    }//while
    return rem;
  }//getMonthsBetween()

  /**
   * Retorna o tempo, em dias, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em dias) restante entre
   * os instantes.
   */
  public static int getDaysBetween(Date tc1, Date tc2) {
    int hours = getHoursBetween(tc1, tc2);
    return hours / 24;
  }//getDaysBetween()

  /**
   * Retorna o tempo, em horas, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em horas) restante entre
   * os instantes.
   */
  public static int getHoursBetween(Date tc1, Date tc2) {
    int minutes = getMinutesBetween(tc1, tc2);
    return minutes / 60;
  }//getHoursBetween()

  /**
   * Retorna o tempo, em minutos, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em minutos) restante entre
   * os instantes.
   */
  public static int getMinutesBetween(Date tc1, Date tc2) {
    int seconds = getSecondsBetween(tc1, tc2);
    return seconds / 60;
  }//getMinutesBetween()

  /**
   * Retorna o tempo, em segundos, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em segundos) restante entre
   * os instantes.
   */
  public static int getSecondsBetween(Date tc1, Date tc2) {
    long millis = getMillisBetween(tc1, tc2);
    return (int) (millis / 1000);
  }//getSecondsBetween()

  /**
   * Retorna o tempo, em milisegundos, entre
   * entre dois <code>Date</code>.
   * @param tc1 Primeiro Date, o mais antigo.
   * @param tc2 Segundo Date, o mais novo.
   * @return Tempo (em milisegundos) restante entre
   * os instantes.
   */
  public static long getMillisBetween(Date tc1, Date tc2) {
    return getTimeBetween(tc1, tc2);
  }//getRemMillis()

  /**
   * Compara se dois <code>Date</code> possuem configurações de
   * tempo iguais, desconsiderando a diferença de
   * milisegundos.
   * @param tc1 Primeiro Date a ser comparado.
   * @param tc2 Segundo Date a ser comparado.
   * @return boolean indicando a igualdade das
   * instâncias.
   */
  public static boolean equals(Date tc1, Date tc2) {
    if(tc1 == null || tc2 == null)
      return false;
    return ( (getSecondsBetween(tc1, tc2) == 0) ||
      (getSecondsBetween(tc2, tc1) == 0) );
  }//equals()

  /**
   * Compara duas instâncias de <code>Date</code>,
   * retornando <code>-1</code> se <code>tc1 < tc2</code>,
   * <code>0 (zero)</code> se <code>tc1 == tc2</code>
   * e <code>1</code> se <code>tc1 > tc2</code>.
   * Caso algum dos objetos Date seja igual a null,
   * serah retornado -99.
   * @param tc1 Primeiro <code>Date</code> a ser comparado.
   * @param tc2 Segundo <code>Date</code> a ser comparado.
   * @return <code>(tc1 < tc2) = -1; (tc1 == tc2) = 0; (tc1 > tc2) = 1</code>.
   */
  public static int compare(Date tc1, Date tc2) {
    if(tc1 == null || tc2 == null)
      return -99;
    int sb = getSecondsBetween(tc1, tc2);
    if(sb > 0) return -1;
    else if(sb < 0) return 1;
    else return 0;
  }//compare()

}//class
