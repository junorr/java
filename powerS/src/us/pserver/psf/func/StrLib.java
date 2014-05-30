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

import java.io.PrintStream;
import java.util.ArrayList;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public class StrLib implements FSFunctionExtension {
  
  public static final String
      STR = "str",
      PSTR = "print",
      CONCAT = "concat",
      EQ = "eq",
      EQICS = "eqics",
      CONTAINSICS = "containsics",
      CONTAINS = "contains",
      LOWER = "lower",
      UPPER = "upper",
      SIDX = "findstr",
      SUBS = "extract",
      REPLACE = "replace",
      STRSIZE = "strsize",
      SIDXLAST = "findlast";

  
  private PrintStream ps;
  
  
  public StrLib() {
    ps = System.out;
  }
  
  
  public void setStdOut(PrintStream ps) {
    if(ps != null)
      this.ps = ps;
  }
  

  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case STR:
        this.checkMinLength(al, 1);
        return str(al.get(0));
      case PSTR:
        this.checkMinLength(al, 1);
        pstr(al.get(0));
        return null;
      case CONCAT:
        return concat(al);
      case EQ:
        this.checkMinLength(al, 2);
        return eq(str(al, 0), str(al, 1));
      case EQICS:
        this.checkMinLength(al, 2);
        return eqics(str(al, 0), str(al, 1));
      case CONTAINSICS:
        this.checkMinLength(al, 2);
        return containsics(str(al, 0), str(al, 1));
      case CONTAINS:
        this.checkMinLength(al, 2);
        return contains(str(al, 0), str(al, 1));
      case LOWER:
        this.checkMinLength(al, 1);
        return lower(str(al, 0));
      case UPPER:
        this.checkMinLength(al, 1);
        return upper(str(al, 0));
      case SUBS:
        this.checkMinLength(al, 3);
        int off = FUtils.cast(al, 1);
        int len = FUtils.cast(al, 2);
        return subs(str(al, 0), off, len);
      case SIDX:
        this.checkMinLength(al, 2);
        return sidx(str(al, 0), str(al, 1));
      case SIDXLAST:
        this.checkMinLength(al, 2);
        return sidxlast(str(al, 0), str(al, 1));
      case REPLACE:
        FUtils.checkLen(al, 2);
        return replace(FUtils.str(al, 0), 
            FUtils.str(al, 1), FUtils.str(al, 2));
      case STRSIZE:
        FUtils.checkLen(al, 1);
        return FUtils.str(al, 0).length();
      default:
        throw new FSUnsupportedException();
    }
  }
  
  
  private void checkMinLength(ArrayList al, int len) throws FSException {
    int sz = 0;
    if(al != null)
      sz = al.size();
    if(sz < len)
      throw new FSException(
          "Insuficient arguments ["
          + sz+ "], expected ["+ len+ "]");
  }
  
  
  public String replace(String orig, String str, String rep) {
    if(orig == null || orig.isEmpty()
        || str == null || str.isEmpty()
        || rep == null) return orig;
    return orig.replace(str, rep);
  }

  
  public String concat(ArrayList al) throws FSException {
    this.checkMinLength(al, 1);
    StringBuilder sb = new StringBuilder();
    for(Object o : al) {
      sb.append(str(o));
    }
    return sb.toString();
  }
  
  
  public String str(ArrayList al, int idx) throws FSException {
    return FUtils.str(al, idx);
  }
  
  
  public String str(Object obj) throws FSException {
    return FUtils.str(obj);
  }
  
  
  public void pstr(Object obj) throws FSException {
    ps.println(str(obj));
  }
  
  
  public int eq(String str1, String str2) {
    if(str1 == null && str2 == null)
      return 1;
    else if(str1 == null || str2 == null)
      return 0;
    else if(str1.equals(str2))
      return 1;
    else
      return 0;
  }

  
  public int eqics(String str1, String str2) {
    if(str1 == null && str2 == null)
      return 1;
    else if(str1 == null || str2 == null)
      return 0;
    else if(str1.equalsIgnoreCase(str2))
      return 1;
    else
      return 0;
  }
  
  
  public int contains(String str1, String str2) {
    if(str1 == null || str2 == null)
      return 0;
    else if(str1.contains(str2))
      return 1;
    else
      return 0;
  }

  
  public String lower(String str) {
    if(str == null) return str;
    return str.toLowerCase();
  }

  
  public String upper(String str) {
    if(str == null) return str;
    return str.toUpperCase();
  }

  
  public int containsics(String str1, String str2) {
    if(str1 == null || str2 == null)
      return 0;
    
    String s1 = lower(str1);
    String s2 = lower(str2);
    
    if(s1.contains(s2))
      return 1;
    else
      return 0;
  }
  
  
  public String subs(String str, int off, int len) throws FSException {
    if(str == null) return str;
    if(off < 0 || off >= str.length())
      throw new FSException("Invalid offset ["+ off+ "]");
    if(len < 1 || (str.length() - off) < len)
      throw new FSException("Invalid length ["+ len+ "]");
    return str.substring(off, len);
  }
  
  
  public int sidx(String str, String srch) {
    if(str == null || srch == null)
      return -1;
    return str.indexOf(srch);
  }

  
  public int sidxlast(String str, String srch) {
    if(str == null || srch == null)
      return -1;
    return str.lastIndexOf(srch);
  }

  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(STR, this);
    ext.addFunctionExtension(PSTR, this);
    ext.addFunctionExtension(CONCAT, this);
    ext.addFunctionExtension(CONTAINS, this);
    ext.addFunctionExtension(CONTAINSICS, this);
    ext.addFunctionExtension(EQ, this);
    ext.addFunctionExtension(EQICS, this);
    ext.addFunctionExtension(SIDX, this);
    ext.addFunctionExtension(SIDXLAST, this);
    ext.addFunctionExtension(LOWER, this);
    ext.addFunctionExtension(SUBS, this);
    ext.addFunctionExtension(UPPER, this);
    ext.addFunctionExtension(REPLACE, this);
    ext.addFunctionExtension(STRSIZE, this);
  }

}
