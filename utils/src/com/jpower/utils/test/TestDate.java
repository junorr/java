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

package com.jpower.utils.test;

import com.jpower.utils.*;
import java.io.File;

/**
 *
 * @author Juno Roesler - powernet.de@gmail.com
 */
public class TestDate {

  public static void main(String[] args) {
    Date tc1 = Date.getInstance(2009, 02, 06, 16, 38, 30, 1);
    System.out.println(tc1);
    Date tc2 = Date.getInstance(2010, 07, 07, 0, 0, 0, 0);
    System.out.println(tc2);
    System.out.println("DateUtils.equals(tc1, tc2) = "+ DateUtils.equals(tc1, tc2));
    System.out.println("DateUtils.compare(tc1, tc2) = "+ DateUtils.compare(tc1, tc2));
    System.out.println("DateUtils.getYearsBetween(tc1, tc2) = "+ DateUtils.getYearsBetween(tc1, tc2));
    System.out.println("DateUtils.getMonthsBetween(tc1, tc2) = "+ DateUtils.getMonthsBetween(tc1, tc2));
    System.out.println("DateUtils.getDaysBetween(tc1, tc2) = "+ DateUtils.getDaysBetween(tc1, tc2));
    System.out.println("DateUtils.getHoursBetween(tc1, tc2) = "+ DateUtils.getHoursBetween(tc1, tc2));
    System.out.println("DateUtils.getMinutesBetween(tc1, tc2) = "+ DateUtils.getMinutesBetween(tc1, tc2));
    System.out.println("DateUtils.getSecondsBetween(tc1, tc2) = "+ DateUtils.getSecondsBetween(tc1, tc2));
    System.out.println("DateUtils.getTimeBetween(tc1, tc2) = "+ DateUtils.getTimeBetween(tc1, tc2));
    System.out.println("-------------------------------");
    System.out.println("tc1.get(DateUtils.MES) = "+ tc1.get(DateUtils.MES));
    System.out.println("tc2.get(DateUtils.MES) = "+ tc2.get(DateUtils.MES));
    System.out.println("tc1.getTime() = "+ tc1.getTime());
    System.out.println("tc2.getTime() = "+ tc2.getTime());
    System.out.println("tc1.isOld() = "+ tc1.isOld());
    System.out.println("tc2.isOld() = "+ tc2.isOld());
    System.out.println("tc1.isOnTime() = "+ tc1.isOnTime());
    System.out.println("tc2.isOnTime() = "+ tc1.isOnTime());
    tc1.set(2010, 3, 6);
    System.out.println("tc1.set(2010, 3, 6) = "+ tc1);
    tc2.set(2010, 7, 7, 8);
    System.out.println("tc2.set(2010, 7, 7, 8)"+ tc2);
    tc1.setTime(tc2.getTime());
    System.out.println("tc1.setTime(tc2.getTime())");
    System.out.println("tc1: "+ tc1);
    System.out.println("tc2: "+ tc2);
    System.out.println("DateUtils.equals(tc1, tc2) = "+ DateUtils.equals(tc1, tc2));
    try {
      tc1.store(new File("tc1.xml"));
      System.out.println("tc1.store(new File(\"tc1.xml\"));");
      tc2.store(new File("tc2.xml"));
      System.out.println("tc2.store(new File(\"tc2.xml\"));");
      tc1 = Date.getInstance(new File("tc1.xml"));
      System.out.println("tc1.load(new File(\"tc1.xml\"))");
      System.out.println(tc1);
      tc2 = Date.getInstance(new File("tc2.xml"));
      System.out.println("tc2.load(new File(\"tc2.xml\"))");
      System.out.println(tc2);

      String tc1xml = tc1.toXML();
      System.out.println(tc1xml);
      System.out.println("Date.getInstance(tc1xml)");
      System.out.println(Date.getInstance(tc1xml));

      tc1.setFrequency(47);
      System.out.println("tc1.setFrequency(47)");
      tc1.setFrequencyType(DateUtils.SEC);
      System.out.println("tc1.setFrequencyType(DateUtils.SEC)");
      tc1.reconfigure();
      System.out.println("tc1.reconfigure()");
      System.out.println(tc1);
      tc1.reconfigure();
      System.out.println("tc1.reconfigure()");
      System.out.println(tc1);

      tc1.setFrequency(27);
      System.out.println("tc1.setFrequency(27)");
      tc1.setFrequencyType(DateUtils.DIA);
      System.out.println("tc1.setFrequencyType(DateUtils.DIA)");
      tc1.reconfigure();
      System.out.println("tc1.reconfigure()");
      System.out.println(tc1);

      System.out.println("tc1.sub(Date.HORA, 12.000)");
      tc1.sub(Date.HORA, 12000);
      System.out.println(tc1);

      System.out.println("tc2.sub(Date.HORA, 12.000)");
      tc2.sub(Date.HORA, 12000);
      System.out.println(tc2);

    } catch(Exception ex) {
      ex.printStackTrace();
    }//try-catch

  }
}
