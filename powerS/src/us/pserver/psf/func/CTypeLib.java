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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/01/2014
 */
public class CTypeLib implements FSFunctionExtension {
  
  public static final String 
      ISNUMBER = "isnumber",
      TOINT = "toint",
      TODOUBLE = "todouble";


  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case ISNUMBER:
        FUtils.checkLen(al, 1);
        return isNumber(al.get(0));
      case TODOUBLE:
        FUtils.checkLen(al, 1);
        return FUtils._double(al, 0);
      case TOINT:
        FUtils.checkLen(al, 1);
        return (int) FUtils._double(al, 0);
      default:
        throw new FSUnsupportedException();
    }
  }
  
  
  public int isNumber(Object o) throws FSException {
    if(o == null) return 0;
    String s = FUtils.str(o);
    try {
      if(s.contains(","))
        s = s.replaceAll(",", ".");
      Double.parseDouble(s);
      return 1;
    } catch(NumberFormatException e) {
      return 0;
    }
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(ISNUMBER, this);
    ext.addFunctionExtension(TODOUBLE, this);
    ext.addFunctionExtension(TOINT, this);
  }
  
}
