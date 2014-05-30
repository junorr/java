/*
Direitos Autorais Reservados (c) 2011 Juno Roesler
Contato: juno.rr@gmail.com

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
package com.jpower.date.test;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.date.SimpleDate;
import com.jpower.date.DateDiff;
import java.util.Date;


/**
 * <pre>
public class TestDateDiff {
  
  public static void main(String[] args) {
    Date d = new Date(0);
    SimpleDate d1 = SimpleDate.parseDate("14/08/1984 23:45:00");
    SimpleDate d2 = SimpleDate.now();
    
    System.out.println(d1);
    System.out.println(d2);
    
    DateDiff dif = new DateDiff(d1, d2);
		
    dif.setUseDaylightTime(false);
    
    Date start = new Date();
    System.out.println(dif.calculate());
    Date end = new Date();
    System.out.println();
    System.out.println(dif);
    System.out.println(new DateDiff(start, end).calculate());
    
    System.out.println();
    System.out.println("toMillis = "+dif.toMillis());
    System.out.println("toSeconds = "+dif.toSeconds());
    System.out.println("toMinutes = "+dif.toMinutes());
    System.out.println("toHours = "+dif.toHours());
    System.out.println("toDays = "+dif.toDays());
    System.out.println("toMonths = "+dif.toMonths());
    System.out.println(new SimpleDate().time(dif.toMillis()));
    
    System.out.println();
    System.out.println();
    
    SimpleDate now = SimpleDate.now();
    
    SimpleDate june = new SimpleDate().date(2011, 1, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 2, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 3, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 4, 14, 18, 20);
    System.out.println(june);
    
    System.out.println();
    System.out.println(june.month(7));
    System.out.println(june.day(5));
    System.out.println(june.year(2012));
    System.out.println(june.hour(5));
    System.out.println(june.day());
    System.out.println(june.date(2000, 7, 7));
    
    System.out.println("now: "+ now);
    System.out.println("june: "+ june);
    
    dif = new DateDiff(now, june);
    System.out.println(dif);
    
    System.out.println(dif);
  }

}
 * </pre>
 * @author Juno Roesler - juno.rr@gmail.com
 */
@Version (
		value			= "3.0",
    name      = "TestDateDiff",
		date			= "2011.11.16",
		author		= "Juno Roesler",
		synopsis	= "Classe teste de DateDiff",
		old				=	{
      "2.1 [2011.03.29]",
      "2.0 [2011.03.28]", 
      "1.2 [2011.03.28]",
			"1.1 [2011.03.25]", 
      "1.0 [2011.03.25]"
    }
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class TestDateDiff {
  
  public static void main(String[] args) {
    Date d = new Date(0);
    SimpleDate d1 = SimpleDate.parseDate("14/08/1984 23:45:00");
    SimpleDate d2 = SimpleDate.now();
    
    System.out.println(d1);
    System.out.println(d2);
    
    DateDiff dif = new DateDiff(d1, d2);
		
    dif.setUseDaylightTime(false);
    
    Date start = new Date();
    System.out.println(dif.calculate());
    Date end = new Date();
    System.out.println();
    System.out.println(dif);
    System.out.println(new DateDiff(start, end).calculate());
    
    System.out.println();
    System.out.println("toMillis = "+dif.toMillis());
    System.out.println("toSeconds = "+dif.toSeconds());
    System.out.println("toMinutes = "+dif.toMinutes());
    System.out.println("toHours = "+dif.toHours());
    System.out.println("toDays = "+dif.toDays());
    System.out.println("toMonths = "+dif.toMonths());
    System.out.println(new SimpleDate().time(dif.toMillis()));
    
    System.out.println();
    System.out.println();
    
    SimpleDate now = SimpleDate.now();
    
    SimpleDate june = new SimpleDate().date(2011, 1, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 2, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 3, 14, 18, 20);
    System.out.println(june);
    june = new SimpleDate().date(2011, 4, 14, 18, 20);
    System.out.println(june);
    
    System.out.println();
    System.out.println(june.month(7));
    System.out.println(june.day(5));
    System.out.println(june.year(2012));
    System.out.println(june.hour(5));
    System.out.println(june.day());
    System.out.println(june.date(2000, 7, 7));
    
    System.out.println("now: "+ now);
    System.out.println("june: "+ june);
    
    dif = new DateDiff(now, june);
    System.out.println(dif);
    
    System.out.println(dif);
  }

}
