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

package us.pserver.xprops.util;

import java.awt.Color;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class SObject implements Transformer<Object, String> {

  @Override
  public String apply(Object obj) throws IllegalArgumentException {
    Valid.off(obj).testNull("Invalid Object to Transform: ");
    if(Number.class.isAssignableFrom(obj.getClass())) {
      return number(obj);
    }
    else if(Date.class.isAssignableFrom(obj.getClass())) {
      return date(obj);
    }
    else if(List.class.isAssignableFrom(obj.getClass())) {
      return new SList().apply((List)obj);
    }
    else if(Color.class.isAssignableFrom(obj.getClass())) {
      return new SColor().apply((Color)obj);
    }
    else if(File.class.isAssignableFrom(obj.getClass())) {
      return ((File)obj).getAbsolutePath();
    }
    else if(Path.class.isAssignableFrom(obj.getClass())) {
      return ((Path)obj).toAbsolutePath().toString();
    }
    return Objects.toString(obj);
  }
  
  
  private String number(Object obj) {
    if(Double.class.isAssignableFrom(obj.getClass())
        || Float.class.isAssignableFrom(obj.getClass())) {
      System.out.println("! Decimal");
      DecimalFormat nf = new DecimalFormat("0.0#####");
      DecimalFormatSymbols dfs = nf.getDecimalFormatSymbols();
      dfs.setDecimalSeparator('.');
      nf.setDecimalFormatSymbols(dfs);
      return nf.format(obj);
    }
    return Objects.toString(obj);
  }
  
  
  private String date(Object obj) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return df.format(obj);
  }
  
}
