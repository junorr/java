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

package us.pserver.psf.func;

import java.util.ArrayList;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;
import us.pserver.date.DateDiff;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/01/2014
 */
public class DateLib implements FSFunctionExtension {
  
  public static final String
      ADDDAY = "addday",
      ADDHOUR = "addhour",
      ADDMIN = "addmin",
      ADDMON = "addmon",
      ADDSEC = "addsec",
      ADDYEAR = "addyear",
      AFTER = "after",
      BEFORE = "before",
      DATE = "date",
      DAYOFW = "dayofw",
      DIFF = "diff",
      EQDATE = "eqdate",
      EQTIME = "eqtime",
      GETDAY = "getday",
      GETHOUR = "gethour",
      GETMIN =  "getmin",
      GETMON = "getmon",
      GETSEC = "getsec",
      GETYEAR = "getyear",
      SETDAY = "setday",
      SETHOUR = "sethour",
      SETMIN = "setmin",
      SETMON = "setmon",
      SETSEC = "setsec",
      SETYEAR = "setyear";
  
  
  public int eqdate(SimpleDate d1, SimpleDate d2) {
    if(d1 == null || d2 == null) return 0;
    if(d1.equalsDate(d2)) return 1;
    return 0;
  }


  public int eqtime(SimpleDate d1, SimpleDate d2) {
    if(d1 == null || d2 == null) return 0;
    if(d1.equalsLess(d2)) return 1;
    return 0;
  }


  public int after(SimpleDate d1, SimpleDate d2) {
    if(d1 == null || d2 == null) return 0;
    if(d1.after(d2)) return 1;
    return 0;
  }


  public int before(SimpleDate d1, SimpleDate d2) {
    if(d1 == null || d2 == null) return 0;
    if(d1.before(d2)) return 1;
    return 0;
  }
  
  
  public SimpleDate addday(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addDay(n);
  }


  public SimpleDate addmon(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addMonth(n);
  }


  public SimpleDate addyear(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addYear(n);
  }


  public SimpleDate addsec(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addSecond(n);
  }


  public SimpleDate addmin(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addMinute(n);
  }


  public SimpleDate addhour(SimpleDate d, int n) {
    if(d == null) return d;
    return d.addHour(n);
  }
  
  
  public int dayofw(SimpleDate d) {
    if(d == null) return -1;
    return d.dayOfWeek();
  }
  
  
  public int getday(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).days();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).day();
    else return -1;
  }


  public int getmon(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).months();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).month();
    else return -1;
  }


  public int getyear(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).years();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).year();
    else return -1;
  }


  public int getsec(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).seconds();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).second();
    else return -1;
  }


  public int getmin(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).minutes();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).minute();
    else return -1;
  }


  public int gethour(Object d) {
    if(d == null) return -1;
    if(d instanceof DateDiff)
      return ((DateDiff) d).hours();
    else if(d instanceof SimpleDate)
      return ((SimpleDate) d).hour();
    else return -1;
  }
  
  
  public SimpleDate setday(SimpleDate d, int n) {
    if(d == null) return d;
    return d.day(n);
  }


  public SimpleDate setmon(SimpleDate d, int n) {
    if(d == null) return d;
    return d.month(n);
  }


  public SimpleDate setyear(SimpleDate d, int n) {
    if(d == null) return d;
    return d.year(n);
  }


  public SimpleDate setsec(SimpleDate d, int n) {
    if(d == null) return d;
    return d.second(n);
  }


  public SimpleDate setmin(SimpleDate d, int n) {
    if(d == null) return d;
    return d.minute(n);
  }


  public SimpleDate sethour(SimpleDate d, int n) {
    if(d == null) return d;
    return d.hour(n);
  }


  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case DATE:
        return FUtils.date((al.size() > 0 ? al.get(0) : null));
      case DIFF:
        FUtils.checkLen(al, 2);
        return diff(al.get(0), al.get(1));
      case ADDDAY:
        FUtils.checkLen(al, 2);
        return addday(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case ADDMON:
        FUtils.checkLen(al, 2);
        return addmon(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case ADDYEAR:
        FUtils.checkLen(al, 2);
        return addyear(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case ADDSEC:
        FUtils.checkLen(al, 2);
        return addsec(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case ADDMIN:
        FUtils.checkLen(al, 2);
        return addmin(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case ADDHOUR:
        FUtils.checkLen(al, 2);
        return addhour(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case GETDAY:
        FUtils.checkLen(al, 1);
        return getday(FUtils.date(al.get(0)));
      case GETMON:
        FUtils.checkLen(al, 1);
        return getmon(FUtils.date(al.get(0)));
      case GETYEAR:
        FUtils.checkLen(al, 1);
        return getyear(FUtils.date(al.get(0)));
      case GETSEC:
        FUtils.checkLen(al, 1);
        return getsec(FUtils.date(al.get(0)));
      case GETMIN:
        FUtils.checkLen(al, 1);
        return getmin(FUtils.date(al.get(0)));
      case GETHOUR:
        FUtils.checkLen(al, 1);
        return gethour(FUtils.date(al.get(0)));
      case SETDAY:
        FUtils.checkLen(al, 1);
        return setday(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case SETMON:
        FUtils.checkLen(al, 1);
        return setmon(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case SETYEAR:
        FUtils.checkLen(al, 1);
        return setyear(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case SETSEC:
        FUtils.checkLen(al, 1);
        return setsec(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case SETMIN:
        FUtils.checkLen(al, 1);
        return setmin(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case SETHOUR:
        FUtils.checkLen(al, 1);
        return sethour(FUtils.date(al.get(0)), FUtils._int(al, 1));
      case EQDATE:
        FUtils.checkLen(al, 2);
        return eqdate(FUtils.date(al.get(0)), FUtils.date(al.get(1)));
      case EQTIME:
        FUtils.checkLen(al, 2);
        return eqtime(FUtils.date(al.get(0)), FUtils.date(al.get(1)));
      case AFTER:
        FUtils.checkLen(al, 2);
        return after(FUtils.date(al.get(0)), FUtils.date(al.get(1)));
      case BEFORE:
        FUtils.checkLen(al, 2);
        return before(FUtils.date(al.get(0)), FUtils.date(al.get(1)));
      default:
        throw new FSUnsupportedException();
    }
  }
  
  
  public DateDiff diff(Object o1, Object o2) throws FSException {
    SimpleDate d1 = FUtils.date(o1);
    SimpleDate d2 = FUtils.date(o2);
    return new DateDiff(d1, d2);
  }
  
  
  public void addTo(FSFastExtension fs) {
    if(fs == null) return;
    fs.addFunctionExtension(ADDDAY, this);
    fs.addFunctionExtension(ADDHOUR, this);
    fs.addFunctionExtension(ADDMIN, this);
    fs.addFunctionExtension(ADDMON, this);
    fs.addFunctionExtension(ADDSEC, this);
    fs.addFunctionExtension(ADDYEAR, this);
    fs.addFunctionExtension(AFTER, this);
    fs.addFunctionExtension(BEFORE, this);
    fs.addFunctionExtension(DATE, this);
    fs.addFunctionExtension(DAYOFW, this);
    fs.addFunctionExtension(DIFF, this);
    fs.addFunctionExtension(EQDATE, this);
    fs.addFunctionExtension(EQTIME, this);
    fs.addFunctionExtension(GETDAY, this);
    fs.addFunctionExtension(GETHOUR, this);
    fs.addFunctionExtension(GETMIN, this);
    fs.addFunctionExtension(GETMON, this);
    fs.addFunctionExtension(GETSEC, this);
    fs.addFunctionExtension(GETYEAR, this);
    fs.addFunctionExtension(SETDAY, this);
    fs.addFunctionExtension(SETHOUR, this);
    fs.addFunctionExtension(SETMIN, this);
    fs.addFunctionExtension(SETMON, this);
    fs.addFunctionExtension(SETSEC, this);
    fs.addFunctionExtension(SETYEAR, this);
  }

}
